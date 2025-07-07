package com.orangeschool.orangeschoolapiserver.domain.commonMember.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class CheckNickNameDto {

    @Schema(description = "부모 닉네임", example = "", required = true)
    private String parentNickName = "";
}
