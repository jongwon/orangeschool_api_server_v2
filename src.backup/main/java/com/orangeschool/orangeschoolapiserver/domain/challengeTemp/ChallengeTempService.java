package com.orangeschool.orangeschoolapiserver.domain.challengeTemp;

import com.orangeschool.orangeschoolapiserver.common.enums.ConfirmStatus;
import com.orangeschool.orangeschoolapiserver.common.response.CustomException;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.domain.challenge.dto.UpdateChallengeDto;
import com.orangeschool.orangeschoolapiserver.domain.challenge.entity.Challenge;
import com.orangeschool.orangeschoolapiserver.domain.challenge.repository.ChallengeRepository;
import com.orangeschool.orangeschoolapiserver.domain.challengeTemp.dto.ChallengeTempDto;
import com.orangeschool.orangeschoolapiserver.domain.challengeTemp.dto.UpdateChallengeConfirmDto;
import com.orangeschool.orangeschoolapiserver.domain.challengeTemp.entity.ChallengeTemp;
import com.orangeschool.orangeschoolapiserver.domain.challengeTemp.repository.ChallengeTempRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChallengeTempService {

    private final ChallengeTempRepository challengeTempRepository;
    private final ChallengeRepository challengeRepository;
//    private final CommonMemberRepository commonMemberRepository;

    @Transactional
    public void put(Long challengeId, UpdateChallengeDto updateChallengeDto) throws Exception {

        Optional<Challenge> challengeOptional = challengeRepository.findById(challengeId);

        if (challengeOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<ChallengeTemp> challengeTempOptional = challengeTempRepository.findByChallengeId(challengeId);

        ChallengeTemp challengeTemp = null;

        if (challengeTempOptional.isPresent()) {
            challengeTemp = challengeTempOptional.get();

            challengeTemp.update(
                    updateChallengeDto.getMission(),
                    updateChallengeDto.getRequiredOrangeCount(),
                    updateChallengeDto.getReward(),
                    updateChallengeDto.getIsShow()
            );
        }

        if (challengeTempOptional.isEmpty()) {
            Challenge challenge = challengeOptional.get();

            challengeTemp = ChallengeTemp.builder()
                    .commonMember(challenge.getCommonMember())
                    .mission(updateChallengeDto.getMission())
                    .requiredOrangeCount(updateChallengeDto.getRequiredOrangeCount())
                    .reward(updateChallengeDto.getReward())
                    .isShow(updateChallengeDto.getIsShow())
                    .confirmStatus(ConfirmStatus.WAIT)
                    .challengeId(challenge.getId())
                    .build();
        }

        challengeTempRepository.save(challengeTemp);
    }

    @Transactional(readOnly = true)
    public ChallengeTempDto getById(Long challengeId) throws Exception {

        Optional<ChallengeTemp> challengeTempOptional = challengeTempRepository.findByChallengeId(challengeId);

        if (challengeTempOptional.isEmpty()) {
            return ChallengeTempDto.builder().build();
        }

        return ChallengeTempDto.create(challengeTempOptional.get());
    }

    @Transactional
    public void challengeConfirm(Long challengeId, UpdateChallengeConfirmDto updateChallengeConfirmDto) throws Exception {

        if (updateChallengeConfirmDto.getConfirm()) {
            Optional<Challenge> challengeOptional = challengeRepository.findById(challengeId);

            if (challengeOptional.isEmpty()) {
                throw new CustomException(ResponseCode.NOT_FOUND);
            }

            Challenge challenge = challengeOptional.get();

            Optional<ChallengeTemp> challengeTempOptional = challengeTempRepository.findByChallengeId(challengeId);

            if (challengeTempOptional.isEmpty()) {
                throw new CustomException(ResponseCode.BAD_REQUEST);
            }

            ChallengeTemp challengeTemp = challengeTempOptional.get();

            challenge.update(
                    challengeTemp.getMission(),
                    challengeTemp.getIsShow(),
                    challengeTemp.getReward(),
                    challengeTemp.getRequiredOrangeCount()
            );

            challengeRepository.save(challenge);
        }

        challengeTempRepository.deleteByChallengeId(challengeId);
    }
}
