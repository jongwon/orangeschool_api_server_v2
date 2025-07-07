package com.orangeschool.orangeschoolapiserver.common.dto.request;

import com.orangeschool.orangeschoolapiserver.common.enums.JoinType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class SocialLoginDto {
    @Schema(description = "소셜로그인타입", example = "KAKAO")
    private JoinType joinType;

    @Schema(description = "소셜토큰", example = "1234")
    @NotBlank
    private String socialToken;

    @Schema(description = "push 토큰", example = "123456789")
    private String pushToken = "";
}
