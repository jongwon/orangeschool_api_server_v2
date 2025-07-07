package com.orangeschool.orangeschoolapiserver.domain.report.entity;

import com.orangeschool.orangeschoolapiserver.common.entity.CommonEntity;
import com.orangeschool.orangeschoolapiserver.common.enums.ReportReason;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Report extends CommonEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporterId")
    private CommonMember reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportedMemberId")
    private CommonMember reportedMember;

    private ReportReason reportReason;
    private String reportReasonDetail;

    private String boardTitle;
    private String boardContent;
    private String comment;
    private Boolean isStory;
}
