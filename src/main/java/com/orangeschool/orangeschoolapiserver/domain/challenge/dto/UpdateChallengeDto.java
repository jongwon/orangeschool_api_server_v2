package com.orangeschool.orangeschoolapiserver.domain.challenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

//학생유저 회원가입
@Data
public class UpdateChallengeDto {

    @Schema(description = "챌린지 미션", example = "챌린지 미션1", required = true)
    @NotBlank
    private String mission = "";
    @Schema(description = "오렌지 개수", example = "3", required = true)
    private int requiredOrangeCount = 0;
    @Schema(description = "챌린지 보상", example = "챌린지 보상", required = true)
    @NotBlank
    private String reward;
    @Schema(description = "챌린지 공개 여부", example = "true", required = true)
    private Boolean isShow = true;
}