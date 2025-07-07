package com.orangeschool.schedule.schedule.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.member.commonMember.entity.CommonMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class DayMessage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commonMemberId")
    private CommonMember commonMember;
    private LocalDate dayDate;
    private String parentMessage;
    private String childMessage;

    public void updateChildMessage(String childMessage) {
        this.childMessage = childMessage;
    }

    public void updateParentMessage(String parentMessage) {
        this.parentMessage = parentMessage;
    }
}
