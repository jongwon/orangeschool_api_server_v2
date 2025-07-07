package com.orangeschool.orangeschoolapiserver.domain.commonMember.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class ResetPasswordRequestDto {

    @Schema(description = "이메일", example = "user@naver.com", required = true)
    @NotBlank
    private String email;
    @Schema(description = "비밀번호", example = "1q2w3e4r!@#")
    @NotBlank
    private String password;
}
