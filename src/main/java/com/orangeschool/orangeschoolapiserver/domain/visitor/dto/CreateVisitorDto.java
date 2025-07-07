package com.orangeschool.orangeschoolapiserver.domain.visitor.dto;

import com.orangeschool.orangeschoolapiserver.common.enums.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateVisitorDto {

    @Schema(description = "유저 타입", example = "PARENT", required = true)
    private MemberType memberType;
}
