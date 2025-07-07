package com.orangeschool.orangeschoolapiserver.domain.schedule.entity;

import com.orangeschool.orangeschoolapiserver.common.entity.CommonEntity;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class DayMessage extends CommonEntity {

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
