package com.orangeschool.member.commonMember.dto;

import com.orangeschool.common.dto.request.KeywordSearchDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TownFriendFilterDto extends KeywordSearchDto {

    @Schema(description = "이름", example = "")
    String name = "";
    @Schema(description = "닉네임", example = "")
    String nickName = "";
}
