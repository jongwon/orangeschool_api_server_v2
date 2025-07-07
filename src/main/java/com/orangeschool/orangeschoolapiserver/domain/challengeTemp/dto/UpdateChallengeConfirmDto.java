package com.orangeschool.orangeschoolapiserver.domain.challengeTemp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

//학생유저 회원가입
@Data
public class UpdateChallengeConfirmDto {

    @Schema(description = "수정 승인 여부", example = "true", required = true)
    private Boolean confirm = true;
}