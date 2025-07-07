package com.orangeschool.orangeschoolapiserver.common.dto.request;

import com.orangeschool.orangeschoolapiserver.common.enums.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class ExcelRequestDto {

    @Schema(description = "id 목록 - 전체가 아닌 경우")
    List<Long> idList;

    @Schema(description = "전체 여부")
    Boolean isAll = false;

    @Schema(description = "회원 엑셀 조회일 경우 회원 유형")
    MemberType memberType = MemberType.NONE;
}
