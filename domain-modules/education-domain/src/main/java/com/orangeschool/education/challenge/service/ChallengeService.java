package com.orangeschool.education.challenge;

import com.google.firebase.messaging.Notification;
import com.orangeschool.common.enums.ChallengeStatus;
import com.orangeschool.common.enums.MemberType;
import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.education.challenge.dto.ChallengeDto;
import com.orangeschool.education.challenge.dto.ChallengeFilterDto;
import com.orangeschool.education.challenge.dto.CreateChallengeDto;
import com.orangeschool.education.challenge.dto.UpdateChallengeDto;
import com.orangeschool.education.challenge.entity.Challenge;
import com.orangeschool.education.challenge.repository.ChallengeRepository;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.member.commonMember.repository.CommonMemberRepository;
import com.orangeschool.support.api.MemberAlarmProvider;
import com.orangeschool.support.api.dto.AlarmNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final CommonMemberRepository commonMemberRepository;
    private final MemberAlarmProvider memberAlarmProvider;

    @Transactional
    public void create(Long parentId, CreateChallengeDto createChallengeDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(parentId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND_MEMBER);
        }

        if (commonMemberOptional.get().getMemberType() == MemberType.CHILD) {
            throw new CustomException(ResponseCode.BAD_REQUEST);
        }

        Optional<CommonMember> childOptional = commonMemberRepository.findById(createChallengeDto.getChildId());

        if (childOptional.isEmpty()) {
            throw new CustomException(ResponseCode.BAD_REQUEST);
        }

        CommonMember child = childOptional.get();

        Challenge challenge = Challenge.builder()
                .commonMember(child)
                .challengeStatus(ChallengeStatus.PROGRESS)
                .mission(createChallengeDto.getMission())
                .currentStampCount(0)
                .currentOrangeCount(0)
                .requiredOrangeCount(createChallengeDto.getRequiredOrangeCount())
                .reward(createChallengeDto.getReward())
                .isShow(createChallengeDto.getIsShow())
                .build();

        challengeRepository.save(challenge);

        // 회원정보에 공개여부 적용
        child.updateChallengeProgress(createChallengeDto.getIsShow());
        commonMemberRepository.save(child);
    }

    @Transactional
    public void challengeStamp(Long childId, Long challengeId) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(childId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND_MEMBER);
        }

        if (commonMemberOptional.get().getMemberType() == MemberType.PARENT) {
            throw new CustomException(ResponseCode.BAD_REQUEST);
        }

        Optional<Challenge> challengeOptional = challengeRepository.findById(challengeId);

        if (challengeOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Challenge challenge = challengeOptional.get();

        if (challenge.getChallengeStatus() == ChallengeStatus.END) {
            throw new CustomException(ResponseCode.BAD_REQUEST_CHALLENGE_END);
        }

        Optional<CommonMember> parentOptional = commonMemberRepository.findById(commonMemberOptional.get().getParentId());

        if (parentOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND_MEMBER);
        }

        CommonMember parent = parentOptional.get();

        Notification notification = Notification.builder()
                .setTitle("챌린지알림")
                .setBody(commonMemberOptional.get().getName() + "이 도장을 달라고 요청했어요!")
                .build();

        memberAlarmProvider.createSystemAlarm(AlarmNotificationRequest.builder()
                .memberId(parent.getId())
                .title("챌린지알림")
                .content(commonMemberOptional.get().getName() + "이 도장을 달라고 요청했어요!")
                .notification(notification)
                .build());
    }

    @Transactional
    public void challengeAddStamp(Long challengeId) throws Exception {

        Optional<Challenge> challengeOptional = challengeRepository.findById(challengeId);

        if (challengeOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Challenge challenge = challengeOptional.get();

        if (challenge.getChallengeStatus() == ChallengeStatus.END || challenge.getCurrentOrangeCount() == challenge.getRequiredOrangeCount()) {
            throw new CustomException(ResponseCode.BAD_REQUEST);
        }

        CommonMember child = challenge.getCommonMember();

        // 5번째 도장인 경우 오렌지 획득
        if (challenge.getCurrentStampCount() == 4) {
            challenge.updateCurrentStampCount(0);
            challenge.updateCurrentOrangeCount(challenge.getCurrentOrangeCount() + 1);
            challengeRepository.save(challenge);

            // 회원정보에 토탈오렌지 개수 적용
            child.updateTotalOrange(child.getTotalOrange() + 1);
            commonMemberRepository.save(child);
        } else {
            challenge.updateCurrentStampCount(challenge.getCurrentStampCount() + 1);
        }

        Notification notification = Notification.builder()
                .setTitle("챌린지알림")
                .setBody("요청한 도장을 받았어요!")
                .build();

        memberAlarmProvider.createSystemAlarm(AlarmNotificationRequest.builder()
                .memberId(child.getId())
                .title("챌린지알림")
                .content("요청한 도장을 받았어요!")
                .notification(notification)
                .build());
    }

    @Transactional
    public void challengeRemoveStamp(Long challengeId) throws Exception {

        Optional<Challenge> challengeOptional = challengeRepository.findById(challengeId);

        if (challengeOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Challenge challenge = challengeOptional.get();

        if (challenge.getChallengeStatus() == ChallengeStatus.END || challenge.getCurrentOrangeCount() == challenge.getRequiredOrangeCount()
                || challenge.getCurrentStampCount() == 0
        ) {
            throw new CustomException(ResponseCode.BAD_REQUEST);
        }

        challenge.updateCurrentStampCount(challenge.getCurrentStampCount() - 1);


        challengeRepository.save(challenge);

        CommonMember child = challenge.getCommonMember();

        Notification notification = Notification.builder()
                .setTitle("챌린지알림")
                .setBody("도장이 회수되었어요!")
                .build();

        memberAlarmProvider.createSystemAlarm(AlarmNotificationRequest.builder()
                .memberId(child.getId())
                .title("챌린지알림")
                .content("도장이 회수되었어요!")
                .notification(notification)
                .build());
    }

    @Transactional
    public void challengeComplete(Long challengeId) throws Exception {

        Optional<Challenge> challengeOptional = challengeRepository.findById(challengeId);

        if (challengeOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Challenge challenge = challengeOptional.get();
        challenge.updateChallengeStatus(ChallengeStatus.END);
        challengeRepository.save(challenge);

        // 공개안함으로 전환
        CommonMember child = challenge.getCommonMember();

        child.updateChallengeProgress(false);
        commonMemberRepository.save(child);
    }

    @Transactional(readOnly = true)
    public Page<ChallengeDto> get(Long parentId, Long childId, Pageable pageable, ChallengeFilterDto challengeFilterDto) throws Exception {

        if (!parentId.equals(childId)) {
            Optional<CommonMember> commonMember = commonMemberRepository.findById(parentId);

            if (commonMember.isEmpty()) {
                throw new CustomException(ResponseCode.NOT_FOUND_MEMBER);
            }

            Optional<CommonMember> childOptional = commonMemberRepository.findById(childId);

            if (childOptional.isEmpty()) {
                throw new CustomException(ResponseCode.BAD_REQUEST);
            }
        }

        return challengeRepository.search(childId, pageable, challengeFilterDto);
    }

    @Transactional(readOnly = true)
    public ChallengeDto getById(Long challengeId) throws Exception {

        Optional<Challenge> challengeOptional = challengeRepository.findById(challengeId);

        if (challengeOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        return ChallengeDto.create(challengeOptional.get());
    }

    @Transactional
    public void put(Long challengeId, UpdateChallengeDto updateChallengeDto) throws Exception {

        Optional<Challenge> challengeOptional = challengeRepository.findById(challengeId);

        if (challengeOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Challenge challenge = challengeOptional.get();
        challenge.update(
                updateChallengeDto.getMission(),
                updateChallengeDto.getIsShow(),
                updateChallengeDto.getReward(),
                updateChallengeDto.getRequiredOrangeCount()
        );

        challengeRepository.save(challenge);

        // 회원정보에 공개여부 적용
        CommonMember child = challenge.getCommonMember();

        child.updateChallengeProgress(updateChallengeDto.getIsShow());
        commonMemberRepository.save(child);
    }

    @Transactional
    public void delete(Long challengeId) throws Exception {

        Optional<Challenge> challengeOptional = challengeRepository.findById(challengeId);

        if (challengeOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Challenge challenge = challengeOptional.get();
        CommonMember child = challenge.getCommonMember();

        // 회원정보에 토탈오렌지 개수 적용
        child.updateTotalOrange(child.getTotalOrange() - challenge.getCurrentOrangeCount());
        commonMemberRepository.save(child);

        challengeRepository.deleteById(challengeId);
    }
}
