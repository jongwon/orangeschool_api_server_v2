package com.orangeschool.orangeschoolapiserver.domain.commonMember.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class CheckPhoneNumberDto {

    @Schema(description = "휴대폰번호", example = "01012345678", required = true)
    @NotBlank
    private String phoneNumber;

    private Boolean isAdmin;
}
