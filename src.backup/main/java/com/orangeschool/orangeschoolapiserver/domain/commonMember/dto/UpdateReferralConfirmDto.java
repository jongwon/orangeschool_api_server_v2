package com.orangeschool.orangeschoolapiserver.domain.commonMember.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orangeschool.orangeschoolapiserver.common.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

//학생유저 회원가입
@Data
public class UpdateReferralConfirmDto {

    @Schema(description = "승인 or 반려 or 삭제", example = "COMPLETE", required = true)
    @NotNull
    private ConfirmStatus confirmStatus;
}