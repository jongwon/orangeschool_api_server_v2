package com.orangeschool.orangeschoolapiserver.domain.challenge.dto;

import com.orangeschool.orangeschoolapiserver.common.dto.response.CommonDto;
import com.orangeschool.orangeschoolapiserver.common.enums.ChallengeStatus;
import com.orangeschool.orangeschoolapiserver.domain.challenge.entity.Challenge;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.CommonMemberDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChallengeDto extends CommonDto {

    private CommonMemberDto commonMember;
    private ChallengeStatus challengeStatus;
    private String challengeStatusTitle;
    private String mission;
    private int currentStampCount;
    private int currentOrangeCount;
    private int requiredOrangeCount;
    private String reward;
    private Boolean isShow;

    public static ChallengeDto create(Challenge challenge) {

        ChallengeDto challengeDto = ChallengeDto.builder()
                .commonMember(CommonMemberDto.create(challenge.getCommonMember()))
                .challengeStatus(challenge.getChallengeStatus())
                .challengeStatusTitle(challenge.getChallengeStatus().getTitle())
                .mission(challenge.getMission())
                .currentStampCount(challenge.getCurrentStampCount())
                .currentOrangeCount(challenge.getCurrentOrangeCount())
                .requiredOrangeCount(challenge.getRequiredOrangeCount())
                .reward(challenge.getReward())
                .isShow(challenge.getIsShow())
                .build();

        challengeDto.setCreatedAt(challenge.getCreatedAt());
        challengeDto.setUpdatedAt(challenge.getUpdatedAt());
        challengeDto.setId(challenge.getId());

        return challengeDto;
    }
}
