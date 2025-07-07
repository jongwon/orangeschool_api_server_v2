package com.orangeschool.support.report.dto;


import com.orangeschool.common.enums.ReportReason;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class CreateReportDto {
    @Schema(description = "신고사유", example = "LIE", required = true)
    @NotBlank
    private ReportReason reportReason;
    @Schema(description = "신고사유상세", example = "ㅁㅊㄴㅁㅊㅁㅊ", required = false)
    private String reportReasonDetail;

    @Schema(description = "게시글 아이디", example = "1", required = false)
    private Long storyId = 0L;
    @Schema(description = "댓글 아이디", example = "1", required = false)
    private Long commentId = 0L;
    @Schema(description = "답글 아이디", example = "1", required = false)
    private Long replyId = 0L;

    @Schema(description = "스토리 매거진 구분", example = "true")
    private Boolean isStory = true;
}
