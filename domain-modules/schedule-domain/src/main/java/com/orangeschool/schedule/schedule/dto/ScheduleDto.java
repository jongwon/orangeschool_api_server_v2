package com.orangeschool.schedule.schedule.dto;


import com.orangeschool.common.dto.response.CommonDto;
import com.orangeschool.common.enums.*;
import com.orangeschool.education.academy.dto.AcademyDto;
import com.orangeschool.schedule.schedule.entity.Schedule;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ScheduleDto extends CommonDto {

    private String title;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private Boolean isAllDay;
    private ScheduleType scheduleType;
    private String scheduleTypeTitle;
    private String color;
    private String memo;
    private CycleType cycleType;
    private String cycleTypeTitle;
    private String cycleDays;
    private CalendarCycle calendarCycle;
    private String calendarCycleTitle;
    private LocalDate cycleEndDate;
    private ScheduleAlarmType scheduleAlarmType;
    private String scheduleAlarmTypeTitle;
    private ScheduleAlarmType scheduleAlarmTypeForParent;
    private String scheduleAlarmTypeForParentTitle;
    private Boolean usePay;
    private LocalDate payDate;
    private PayCycle payCycle;
    private String payCycleTitle;
    private LocalDate payCycleEndDate;
    private Long amount;
    private Boolean isSingle;
    private Boolean usePaymentAlarm;
    private PayAlarmType payAlarmType;
    private String payAlarmTypeTitle;
    private Long commonMemberId;
    private String commonMemberName;
    private String fileUrl;
    private Long academyId;
    private String academyName;

    // 단일 조회에서만 사용
    private AcademyDto academy;

    private Boolean isImportant;

    public static ScheduleDto create(Schedule schedule) {

        ScheduleDto scheduleDto = ScheduleDto.builder()
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .startTime(schedule.getStartTime())
                .endDate(schedule.getEndDate())
                .endTime(schedule.getEndTime())
                .isAllDay(schedule.getIsAllDay())
                .scheduleType(schedule.getScheduleType())
                .scheduleTypeTitle(schedule.getScheduleType().getTitle())
                .color(schedule.getColor())
                .memo(schedule.getMemo())
                .cycleType(schedule.getCycleType())
                .cycleTypeTitle(schedule.getCycleType() != null ? schedule.getCycleType().getTitle() : "")
                .cycleDays(schedule.getCycleDays())
                .calendarCycle(schedule.getCalendarCycle())
                .calendarCycleTitle(schedule.getCalendarCycle() != null ? schedule.getCalendarCycle().getTitle() : "")
                .cycleEndDate(schedule.getCycleEndDate())
                .scheduleAlarmType(schedule.getScheduleAlarmType())
                .scheduleAlarmTypeTitle(schedule.getScheduleAlarmType().getTitle())
                .scheduleAlarmTypeForParent(schedule.getScheduleAlarmTypeForParent())
                .scheduleAlarmTypeForParentTitle(schedule.getScheduleAlarmTypeForParent() == null ? "" : schedule.getScheduleAlarmTypeForParent().getTitle())
                .usePay(schedule.getUsePay())
                .payDate(schedule.getPayDate())
                .payCycle(schedule.getPayCycle())
                .payCycleTitle(schedule.getPayCycle() == null ? "" : schedule.getPayCycle().getTitle())
                .payCycleEndDate(schedule.getPayCycleEndDate())
                .amount(schedule.getAmount())
                .isSingle(schedule.getIsSingle())
                .usePaymentAlarm(schedule.getUsePaymentAlarm())
                .payAlarmType(schedule.getPayAlarmType())
                .payAlarmTypeTitle(schedule.getPayAlarmType() == null ? "" : schedule.getPayAlarmType().getTitle())
                .commonMemberId(schedule.getCommonMember().getId())
                .commonMemberName(schedule.getCommonMember().getName())
                .fileUrl(schedule.getCommonMember().getFileUrl())
                .academyId(schedule.getAcademyId())
                .academyName(schedule.getAcademyName())
                .isImportant(schedule.getIsImportant())
                .build();

        scheduleDto.setCreatedAt(schedule.getCreatedAt());
        scheduleDto.setUpdatedAt(schedule.getUpdatedAt());
        scheduleDto.setId(schedule.getId());

        return scheduleDto;
    }

    public void setAcademy(AcademyDto academy) {
        this.academy = academy;
    }
}
