package com.orangeschool.support.suggest.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateSuggestDto {
    @Schema(description = "제목", example = "ㅁㅊㄴㅁㅊㅁㅊ", required = true)
    private String title;
    @Schema(description = "내용", example = "ㅁㅊㄴㅁㅊㅁㅊ", required = true)
    private String content;
}
