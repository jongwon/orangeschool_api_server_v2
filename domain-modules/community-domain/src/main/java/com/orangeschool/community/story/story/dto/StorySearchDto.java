package com.orangeschool.community.story.story.dto;


import com.orangeschool.common.dto.request.KeywordSearchDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StorySearchDto extends KeywordSearchDto {

    @Schema(description = "시군구 value 배열", example = "#11680#51150#11305", required = false)
    private String regionCodeTag = "";

    @Schema(description = "내가 쓴 글 조회여부", example = "false")
    private Boolean myFlag= false;

    @Schema(description = "처음 조회 여부", example = "false")
    private Boolean isFirst= false;

    @Schema(description = "검색인지 여부", example = "false")
    private Boolean isSearch= false;
}
