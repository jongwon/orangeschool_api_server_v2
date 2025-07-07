package com.orangeschool.schedule.schedule.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.schedule.calendar.entity.Calendar;
import com.orangeschool.member.commonMember.entity.CommonMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SleepInfo extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commonMemberId")
    private CommonMember commonMember;
    private LocalTime wakeTime;
    private LocalTime sleepTime;

    public void update(LocalTime wakeTime, LocalTime sleepTime) {
        this.wakeTime = wakeTime;
        this.sleepTime = sleepTime;
    }
}
