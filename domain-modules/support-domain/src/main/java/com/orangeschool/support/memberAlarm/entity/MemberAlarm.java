package com.orangeschool.support.memberAlarm.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.common.enums.AlarmType;
import com.orangeschool.member.commonMember.entity.CommonMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class MemberAlarm extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commonMemberId")
    private CommonMember commonMember;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private Boolean isRead;
    private Long alarmId;
    private AlarmType alarmType;

    public void read(){
        this.isRead = true;
    }
}

