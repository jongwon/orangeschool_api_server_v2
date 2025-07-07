package com.orangeschool.orangeschoolapiserver.domain.challenge.dto;

import com.orangeschool.orangeschoolapiserver.common.enums.ChallengeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ChallengeFilterDto {

    @Schema(description = "챌린지 상태", example = "NONE")
    private ChallengeStatus challengeStatus = ChallengeStatus.NONE;
}
