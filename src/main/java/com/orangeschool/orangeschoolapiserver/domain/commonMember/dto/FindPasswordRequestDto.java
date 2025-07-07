package com.orangeschool.orangeschoolapiserver.domain.commonMember.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class FindPasswordRequestDto {

    @Schema(description = "이메일", example = "user@naver.com", required = true)
    @Email
    private String email;
    @Schema(description = "이름", example = "홍길동", required = true)
    @NotBlank
    private String name;
    @Schema(description = "휴대폰번호", example = "01012345678", required = true)
    @NotBlank
    private String phoneNumber;
}
