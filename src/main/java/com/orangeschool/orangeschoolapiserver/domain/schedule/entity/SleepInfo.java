package com.orangeschool.orangeschoolapiserver.domain.schedule.entity;

import com.orangeschool.orangeschoolapiserver.common.entity.CommonEntity;
import com.orangeschool.orangeschoolapiserver.domain.calendar.entity.Calendar;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SleepInfo extends CommonEntity {

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
