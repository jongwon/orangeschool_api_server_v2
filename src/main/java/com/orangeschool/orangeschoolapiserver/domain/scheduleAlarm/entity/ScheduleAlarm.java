package com.orangeschool.orangeschoolapiserver.domain.scheduleAlarm.entity;

import com.orangeschool.orangeschoolapiserver.common.entity.CommonEntity;
import com.orangeschool.orangeschoolapiserver.domain.calendar.entity.Calendar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ScheduleAlarm extends CommonEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendarId")
    private Calendar calendar;
    private Long scheduleId;
    private Long commonMemberId;

    private LocalDateTime alarmDateTime;
    private Boolean isPay;
}
