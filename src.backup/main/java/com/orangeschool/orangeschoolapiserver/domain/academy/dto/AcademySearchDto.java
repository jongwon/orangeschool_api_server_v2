package com.orangeschool.orangeschoolapiserver.domain.academy.dto;


import com.orangeschool.orangeschoolapiserver.common.dto.request.KeywordSearchDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AcademySearchDto extends KeywordSearchDto {

    @Schema(description = "주소 키워드 배열", example = "#서울 강남구#강원 강릉시#서울 강북구", required = false)
    private String addressKeyword = "";
}
