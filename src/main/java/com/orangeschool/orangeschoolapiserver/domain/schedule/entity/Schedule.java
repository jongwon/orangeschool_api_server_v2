package com.orangeschool.orangeschoolapiserver.domain.schedule.entity;

import com.orangeschool.orangeschoolapiserver.common.entity.CommonEntity;
import com.orangeschool.orangeschoolapiserver.common.enums.*;
import com.orangeschool.orangeschoolapiserver.domain.calendar.entity.Calendar;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
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
public class Schedule extends CommonEntity {

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

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private Set<Calendar> calendars = new HashSet<>();

    public void update(String title, LocalDate startDate, LocalTime startTime, LocalDate endDate,
                       LocalTime endTime, Boolean isAllDay, ScheduleType scheduleType,
                       long academyId, String academyName, String color, String memo, CycleType cycleType,
                       String cycleDays, CalendarCycle calendarCycle, LocalDate cycleEndDate,
                       Boolean usePay, LocalDate payDate, PayCycle payCycle, LocalDate payCycleEndDate,
                       Long amount, Boolean isSingle, ScheduleAlarmType scheduleAlarmType, ScheduleAlarmType scheduleAlarmTypeForParent, Boolean usePaymentAlarm, PayAlarmType payAlarmType, Boolean isImportant) {
        this.title = title;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.isAllDay = isAllDay;
        this.scheduleType = scheduleType;
        this.academyId = academyId;
        this.academyName = academyName;
        this.color = color;
        this.memo = memo;
        this.cycleType = cycleType;
        this.cycleDays = cycleDays;
        this.calendarCycle = calendarCycle;
        this.cycleEndDate = cycleEndDate;
        this.usePay = usePay;
        this.payDate = payDate;
        this.payCycle = payCycle;
        this.payCycleEndDate = payCycleEndDate;
        this.amount = amount;
        this.isSingle = isSingle;
        this.scheduleAlarmType = scheduleAlarmType;
        this.scheduleAlarmTypeForParent = scheduleAlarmTypeForParent;
        this.usePaymentAlarm = usePaymentAlarm;
        this.payAlarmType = payAlarmType;
        this.isImportant = isImportant;
    }

    public void updatePayment(String title, LocalDate startDate, LocalDate endDate, Boolean isAllDay,
                              ScheduleType scheduleType, String color, String memo, LocalDate payDate,
                              PayCycle payCycle, LocalDate payCycleEndDate, Long amount, Boolean isSingle,
                              Boolean usePaymentAlarm, PayAlarmType payAlarmType) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isAllDay = isAllDay;
        this.scheduleType = scheduleType;
        this.color = color;
        this.memo = memo;
        this.payDate = payDate;
        this.payCycle = payCycle;
        this.payCycleEndDate = payCycleEndDate;
        this.amount = amount;
        this.isSingle = isSingle;
        this.usePaymentAlarm = usePaymentAlarm;
        this.payAlarmType = payAlarmType;
    }
}
