package com.orangeschool.orangeschoolapiserver.domain.challengeTemp.dto;

import com.orangeschool.orangeschoolapiserver.common.dto.response.CommonDto;
import com.orangeschool.orangeschoolapiserver.domain.challengeTemp.entity.ChallengeTemp;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChallengeTempDto extends CommonDto {

    private String mission;
    private int requiredOrangeCount;
    private String reward;
    private Boolean isShow;

    public static ChallengeTempDto create(ChallengeTemp challengeTemp) {

        ChallengeTempDto challengeDto = ChallengeTempDto.builder()
                .mission(challengeTemp.getMission())
                .requiredOrangeCount(challengeTemp.getRequiredOrangeCount())
                .reward(challengeTemp.getReward())
                .isShow(challengeTemp.getIsShow())
                .build();

        challengeDto.setCreatedAt(challengeTemp.getCreatedAt());
        challengeDto.setUpdatedAt(challengeTemp.getUpdatedAt());
        challengeDto.setId(challengeTemp.getId());

        return challengeDto;
    }
}
