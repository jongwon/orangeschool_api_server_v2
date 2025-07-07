package com.orangeschool.orangeschoolapiserver.domain.scheduleTemp.entity;

import com.orangeschool.orangeschoolapiserver.common.entity.CommonEntity;
import com.orangeschool.orangeschoolapiserver.common.enums.*;
import com.orangeschool.orangeschoolapiserver.domain.calendar.entity.Calendar;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ScheduleTemp extends CommonEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commonMemberId")
    private CommonMember commonMember;
    private String title;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private Boolean isAllDay;
    private ScheduleType scheduleType;
    private long academyId;
    private String academyName;
    private String color;
    @Column(columnDefinition = "TEXT")
    private String memo;
    // 반복 정보
    private CycleType cycleType;
    private String cycleDays;
    private CalendarCycle calendarCycle;
    private LocalDate cycleEndDate;
    // 알림 정보
    private ScheduleAlarmType scheduleAlarmType;
    private ScheduleAlarmType scheduleAlarmTypeForParent;

    // 결제 정보
    private Boolean usePay;
    private LocalDate payDate;
    private PayCycle payCycle;
    private LocalDate payCycleEndDate;
    private Long amount;
    private Boolean isSingle;
    private Boolean usePaymentAlarm;
    private PayAlarmType payAlarmType;

    private Boolean isImportant;

    // 확인상태
    private ConfirmStatus confirmStatus;
    private Long scheduleId;
    private ScheduleUpdateType scheduleUpdateType;
    private LocalDate updateStandardDate;
    private ScheduleRequestType scheduleRequestType;
    private Boolean isPay;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private Set<Calendar> calendars = new HashSet<>();

    public void updateConfirmStatus(ConfirmStatus confirmStatus) {
        this.confirmStatus = confirmStatus;
    }
}
