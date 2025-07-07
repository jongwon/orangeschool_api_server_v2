package com.orangeschool.member.commonMember.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Email;


@Data
public class CheckEmailDto {

    @Schema(description = "이메일", example = "user@naver.com", required = true)
    private String email;
}
