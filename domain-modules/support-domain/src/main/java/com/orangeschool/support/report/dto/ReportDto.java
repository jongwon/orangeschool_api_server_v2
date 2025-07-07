package com.orangeschool.support.report.dto;

import com.orangeschool.common.dto.response.CommonDto;
import com.orangeschool.common.enums.ReportReason;
import com.orangeschool.member.commonMember.dto.CommonMemberDto;
import com.orangeschool.support.report.entity.Report;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportDto extends CommonDto {

    private CommonMemberDto reporter;
    private CommonMemberDto reportedMember;

    private ReportReason reportReason;
    private String reportReasonTitle;
    private String reportReasonDetail;

    private String boardTitle;
    private String boardContent;
    private String comment;
    private Boolean isStory;

    public static ReportDto create(Report report) {

        ReportDto reportDto = ReportDto.builder()
                .reporter(CommonMemberDto.create(report.getReporter()))
                .reportedMember(CommonMemberDto.create(report.getReportedMember()))
                .reportReason(report.getReportReason())
                .reportReasonTitle(report.getReportReason().getTitle())
                .reportReasonDetail(report.getReportReasonDetail())
                .boardTitle(report.getBoardTitle())
                .boardContent(report.getBoardContent())
                .comment(report.getComment())
                .isStory(report.getIsStory())
                .build();

        reportDto.setCreatedAt(report.getCreatedAt());
        reportDto.setUpdatedAt(report.getUpdatedAt());
        reportDto.setId(report.getId());

        return reportDto;
    }



}
