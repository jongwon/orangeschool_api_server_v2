package com.orangeschool.orangeschoolapiserver.domain.memberNotice.entity;

import com.orangeschool.orangeschoolapiserver.common.entity.CommonEntity;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import com.orangeschool.orangeschoolapiserver.domain.memberAcademy.entity.MemberAcademy;
import com.orangeschool.orangeschoolapiserver.domain.notice.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class MemberNotice extends CommonEntity {

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

