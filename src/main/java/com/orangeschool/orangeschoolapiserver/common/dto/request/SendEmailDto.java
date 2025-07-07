package com.orangeschool.orangeschoolapiserver.common.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SendEmailDto {
    @Schema(description = "이메일", example = "user01@naver.com")
    String email;
    @Schema(description = "제목", example = "메일 제목")
    String subject;
    @Schema(description = "내용", example = "메일 내용입니다.")
    String content;
}
