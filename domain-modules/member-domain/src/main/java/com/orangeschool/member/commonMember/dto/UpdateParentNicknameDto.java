package com.orangeschool.member.commonMember.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class UpdateParentNicknameDto {

    @Schema(description = "부모 닉네임", example = "")
    private String parentNickName = "";
}