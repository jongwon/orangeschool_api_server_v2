package com.orangeschool.schedule.scheduleTemp.dto;


import com.orangeschool.common.dto.response.CommonDto;
import com.orangeschool.common.enums.*;
import com.orangeschool.education.academy.dto.AcademyDto;
import com.orangeschool.schedule.scheduleTemp.entity.ScheduleTemp;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ScheduleTempDto extends CommonDto {

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

    private ConfirmStatus confirmStatus;
    private Long scheduleId;
    private ScheduleUpdateType scheduleUpdateType;
    private LocalDate updateStandardDate;
    private ScheduleRequestType scheduleRequestType;
    private Boolean isPay;

    public static ScheduleTempDto create(ScheduleTemp scheduleTemp) {

        ScheduleTempDto scheduleDto = ScheduleTempDto.builder()
                .title(scheduleTemp.getTitle())
                .startDate(scheduleTemp.getStartDate())
                .startTime(scheduleTemp.getStartTime())
                .endDate(scheduleTemp.getEndDate())
                .endTime(scheduleTemp.getEndTime())
                .isAllDay(scheduleTemp.getIsAllDay())
                .scheduleType(scheduleTemp.getScheduleType())
                .scheduleTypeTitle(scheduleTemp.getScheduleType().getTitle())
                .color(scheduleTemp.getColor())
                .memo(scheduleTemp.getMemo())
                .cycleType(scheduleTemp.getCycleType())
                .cycleTypeTitle(scheduleTemp.getCycleType()!=null ? scheduleTemp.getCycleType().getTitle() : "")
                .cycleDays(scheduleTemp.getCycleDays())
                .calendarCycle(scheduleTemp.getCalendarCycle())
                .calendarCycleTitle(scheduleTemp.getCalendarCycle()!=null ?scheduleTemp.getCalendarCycle().getTitle() : "")
                .cycleEndDate(scheduleTemp.getCycleEndDate())
                .scheduleAlarmType(scheduleTemp.getScheduleAlarmType())
                .scheduleAlarmTypeTitle(scheduleTemp.getScheduleAlarmType().getTitle())
                .scheduleAlarmTypeForParent(scheduleTemp.getScheduleAlarmTypeForParent())
                .scheduleAlarmTypeForParentTitle(scheduleTemp.getScheduleAlarmTypeForParent().getTitle())
                .usePay(scheduleTemp.getUsePay())
                .payDate(scheduleTemp.getPayDate())
                .payCycle(scheduleTemp.getPayCycle())
                .payCycleTitle(scheduleTemp.getPayCycle() == null ? "" : scheduleTemp.getPayCycle().getTitle())
                .payCycleEndDate(scheduleTemp.getPayCycleEndDate())
                .amount(scheduleTemp.getAmount())
                .isSingle(scheduleTemp.getIsSingle())
                .usePaymentAlarm(scheduleTemp.getUsePaymentAlarm())
                .payAlarmType(scheduleTemp.getPayAlarmType())
                .payAlarmTypeTitle(scheduleTemp.getPayAlarmType() == null ? "" : scheduleTemp.getPayAlarmType().getTitle())
                .commonMemberId(scheduleTemp.getCommonMember().getId())
                .commonMemberName(scheduleTemp.getCommonMember().getName())
                .fileUrl(scheduleTemp.getCommonMember().getFileUrl())
                .academyId(scheduleTemp.getAcademyId())
                .academyName(scheduleTemp.getAcademyName())
                .isImportant(scheduleTemp.getIsImportant())

                .confirmStatus(scheduleTemp.getConfirmStatus())
                .scheduleId(scheduleTemp.getScheduleId())
                .scheduleUpdateType(scheduleTemp.getScheduleUpdateType())
                .updateStandardDate(scheduleTemp.getUpdateStandardDate())
                .scheduleRequestType(scheduleTemp.getScheduleRequestType())
                .isPay(scheduleTemp.getIsPay())
                .build();

        scheduleDto.setCreatedAt(scheduleTemp.getCreatedAt());
        scheduleDto.setUpdatedAt(scheduleTemp.getUpdatedAt());
        scheduleDto.setId(scheduleTemp.getId());

        return scheduleDto;
    }

    public void setAcademy(AcademyDto academy) {
        this.academy = academy;
    }
}
