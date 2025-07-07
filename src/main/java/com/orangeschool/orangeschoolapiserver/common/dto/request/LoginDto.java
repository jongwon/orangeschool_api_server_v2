package com.orangeschool.orangeschoolapiserver.common.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDto {

    @Schema(description = "아이디", example = "admin@naver.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String account;
    @Schema(description = "비밀번호", example = "1q2w3e4r!@#", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String password;

    @Schema(description = "push 토큰", example = "123456789")
    private String pushToken = "";
}
