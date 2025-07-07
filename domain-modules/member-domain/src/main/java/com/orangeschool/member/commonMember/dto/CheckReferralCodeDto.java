package com.orangeschool.member.commonMember.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class CheckReferralCodeDto {

    @Schema(description = "추천인 코드", example = "A1B2C3D4")
    private String referralCode = "";
}
