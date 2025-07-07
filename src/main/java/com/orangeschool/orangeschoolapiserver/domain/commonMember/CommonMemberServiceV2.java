package com.orangeschool.orangeschoolapiserver.domain.commonMember;

import com.google.firebase.messaging.Notification;
import com.orangeschool.orangeschoolapiserver.common.enums.ConfirmStatus;
import com.orangeschool.orangeschoolapiserver.common.response.CustomException;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.utils.FileManagement;
import com.orangeschool.orangeschoolapiserver.common.utils.JwtTokenProvider;
import com.orangeschool.orangeschoolapiserver.common.utils.SmsManagement;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.*;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.repository.CommonMemberRepository;
import com.orangeschool.orangeschoolapiserver.domain.location.LocationService;
import com.orangeschool.orangeschoolapiserver.domain.memberAlarm.MemberAlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommonMemberServiceV2 {

    private final CommonMemberRepository commonMemberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final FileManagement fileManagement;
    private final LocationService locationService;
    private final SmsManagement smsManagement;

    private final MemberAlarmService memberAlarmService;

    @Transactional
    public void putSettingRegion(Long commonMemberId, UpdateSettingRegionDto updateSettingRegionDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember commonMember = commonMemberOptional.get();
        commonMember.updateSettingRegion(
                updateSettingRegionDto.getRegionNameTag(),
                updateSettingRegionDto.getRegionCodeTag()
        );

        commonMemberRepository.save(commonMember);
    }

    @Transactional
    public void putParentNickname(Long commonMemberId, UpdateParentNicknameDto updateParentNicknameDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember commonMember = commonMemberOptional.get();
        commonMember.updateParentNickname(
                updateParentNicknameDto.getParentNickName()
        );

        commonMemberRepository.save(commonMember);
    }

    @Transactional(readOnly = true)
    public List<CommonMemberDto> getReferralRequest(Long commonMemberId) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember commonMember = commonMemberOptional.get();

        List<CommonMember> commonMemberList = commonMemberRepository.findByMatchingReferralCodes(commonMember.getMyReferralCode());

        return commonMemberList.stream().map(CommonMemberDto::create).collect(Collectors.toList());
    }

    @Transactional
    public void postReferralRequestConfirm(Long myId, Long commonMemberId, UpdateReferralConfirmDto updateReferralConfirmDto) throws Exception {
        Optional<CommonMember> myOptional = commonMemberRepository.findById(myId);

        if (myOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember my = myOptional.get();

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember commonMember = commonMemberOptional.get();

        // 반려일 경우
        if (updateReferralConfirmDto.getConfirmStatus() == ConfirmStatus.REJECT) {
            commonMember.updateReferralCode(null, null);
        }

        // 승인일 경우
        if (updateReferralConfirmDto.getConfirmStatus() == ConfirmStatus.COMPLETE) {
            commonMember.updateReferralCode(my.getMyReferralCode(), null);
        }

        // 삭제일 경우
        if (updateReferralConfirmDto.getConfirmStatus() == ConfirmStatus.DELETE) {
            commonMember.updateReferralCode(null, null);
        }
    }

    @Transactional(readOnly = true)
    public void checkReferralCode(String referralCode) throws Exception {
        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findByMyReferralCode(referralCode);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOTFOUND_REFERRAL_CODE);
        }

        Long referralCount = commonMemberRepository.countByReferralCode(referralCode);

        if (referralCount >= 3) {
            throw new CustomException(ResponseCode.EXPIRED_REFERRAL_CODE);
        }
    }

    @Transactional
    public void putSettingreferralCode(Long commonMemberId, UpdateSettingReferralCodeDto updateSettingReferralCodeDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember commonMember = commonMemberOptional.get();
        commonMember.updateReferralCode(null, updateSettingReferralCodeDto.getReferralCode());

        if (updateSettingReferralCodeDto.getReferralCode() != null && updateSettingReferralCodeDto.getReferralCode() != "") {
            Optional<CommonMember> referralMemberOptional = commonMemberRepository.findByMyReferralCode(updateSettingReferralCodeDto.getReferralCode());

            if (referralMemberOptional.isEmpty()) {
                throw new CustomException(ResponseCode.NOTFOUND_REFERRAL_CODE);
            }

            Notification notification = Notification.builder()
                    .setTitle("구성원알림")
                    .setBody("새로운 구성원이 참여 요청하였습니다. 마이>구성원 관리에서 승인해 주세요.")
                    .build();

            memberAlarmService.createToSystem("구성원알림", "새로운 구성원이 참여 요청하였습니다. 마이>구성원 관리에서 승인해 주세요.", notification, referralMemberOptional.get());
        }

        commonMemberRepository.save(commonMember);
    }

    @Transactional
    public void putSettingTimetable(UpdateSettingTimetableDto updateSettingTimetableDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(updateSettingTimetableDto.getCommonMemberId());

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember commonMember = commonMemberOptional.get();
        commonMember.updatTimetableEdit(updateSettingTimetableDto.getTimetableEdit());

        commonMemberRepository.save(commonMember);
    }

    public String getTimeTable(Long commonMemberId) throws Exception {
        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (!commonMemberOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        return commonMemberOptional.get().getTimetableEdit();
    }
}
