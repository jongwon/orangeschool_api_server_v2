package com.orangeschool.support.manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class CreateManagerDto {
    @Schema(description = "이메일", example = "admin@naver.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Email
    private String email;
    @Schema(description = "이름", example = "최고관리자", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String name;
    @Schema(description = "비밀번호", example = "1q2w3e4r!@#", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String password;
    @Schema(description = "접근 메뉴", example = "[]")
    private String accessMenu = "[]";
}
