package com.orangeschool.orangeschoolapiserver.domain.calendar.dto;


import com.orangeschool.orangeschoolapiserver.common.dto.response.CommonDto;
import com.orangeschool.orangeschoolapiserver.common.enums.PayCycle;
import com.orangeschool.orangeschoolapiserver.common.enums.ScheduleType;
import com.orangeschool.orangeschoolapiserver.domain.calendar.entity.Calendar;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class CalendarDto extends CommonDto {

    private Long scheduleId;
    private String name;
    private PayCycle payCycle;
    private String payCycleTitle;
    private String title;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private Boolean isAllDay;
    private ScheduleType scheduleType;
    private String scheduleTypeTitle;
    private String color;
    private Boolean isBetween;
    private String memo;
    private Long amount;
    private Boolean isSingle;
    private Boolean isImportant;

    public static CalendarDto create(Calendar calendar) {

        CalendarDto calendarDto = CalendarDto.builder()
                .scheduleId(calendar.getSchedule().getId())
                .name(calendar.getSchedule().getCommonMember().getName())
                .payCycle(calendar.getSchedule().getPayCycle())
                .payCycleTitle(calendar.getSchedule().getPayCycle().getTitle())
                .title(calendar.getTitle())
                .startDate(calendar.getStartDate())
                .startTime(calendar.getStartTime())
                .endDate(calendar.getEndDate())
                .endTime(calendar.getEndTime())
                .isAllDay(calendar.getIsAllDay())
                .scheduleType(calendar.getScheduleType())
                .scheduleTypeTitle(calendar.getScheduleType().getTitle())
                .color(calendar.getColor())
                .isBetween(calendar.getIsBetween())
                .memo(calendar.getMemo())
                .amount(calendar.getAmount())
                .isSingle(calendar.getIsSingle())
                .isImportant(calendar.getIsImportant())
                .build();

        calendarDto.setCreatedAt(calendar.getCreatedAt());
        calendarDto.setUpdatedAt(calendar.getUpdatedAt());
        calendarDto.setId(calendar.getId());

        return calendarDto;
    }
}
