package com.orangeschool.orangeschoolapiserver.domain.calendar.entity;

import com.orangeschool.orangeschoolapiserver.common.entity.CommonEntity;
import com.orangeschool.orangeschoolapiserver.common.enums.ScheduleType;
import com.orangeschool.orangeschoolapiserver.domain.schedule.entity.Schedule;
import com.orangeschool.orangeschoolapiserver.domain.scheduleAlarm.entity.ScheduleAlarm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Calendar extends CommonEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheduleId")
    private Schedule schedule;
    private String title;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private Boolean isAllDay;
    private ScheduleType scheduleType;
    private String color;
    // 2일 이상으로 인해 생긴 일정
    private Boolean isBetween;
    private String memo;
    // 결제 일정일 경우
    private Long amount;

    // 하루일정여부
    private Boolean isSingle;
    private Boolean isImportant;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private Set<ScheduleAlarm> scheduleAlarms = new HashSet<>();
}
