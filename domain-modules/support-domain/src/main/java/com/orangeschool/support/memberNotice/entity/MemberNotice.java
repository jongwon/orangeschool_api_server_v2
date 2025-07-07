package com.orangeschool.support.memberNotice.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.education.memberAcademy.entity.MemberAcademy;
import com.orangeschool.notice.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class MemberNotice extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commonMemberId")
    private CommonMember commonMember;



    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private Boolean isRead;
    private Long noticeId;

    public void read(){
        this.isRead = true;
    }
}

