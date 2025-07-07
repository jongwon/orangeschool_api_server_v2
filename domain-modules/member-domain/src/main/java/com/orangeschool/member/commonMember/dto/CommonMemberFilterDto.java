package com.orangeschool.member.commonMember.dto;

import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.common.enums.MemberFilter;
import com.orangeschool.common.enums.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommonMemberFilterDto extends KeywordSearchDto {

    @Schema(description = "키워드", example = "")
    String keyword = "";
    @Schema(description = "필터 종류", example = "")
    MemberFilter memberFilter = MemberFilter.NONE;
    @Schema(description = "회원 종류", example = "")
    MemberType memberType = MemberType.NONE;
    @Schema(description = "부모 회원 고유 아이디", example = "1L")
    Long parentId;
}
