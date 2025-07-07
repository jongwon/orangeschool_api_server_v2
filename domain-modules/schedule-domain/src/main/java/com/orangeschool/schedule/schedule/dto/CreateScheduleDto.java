package com.orangeschool.schedule.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orangeschool.common.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;

//학생유저 회원가입
@Data
@Builder
public class CreateScheduleDto {

    @Schema(description = "회원 고유 아이디", example = "1", required = true)
    private Long commonMemberId = 0L;
    @Schema(description = "제목", example = "일정 제목", required = true)
    @NotBlank
    private String title;
    @Schema(description = "일정 시작일", example = "2023-05-01", pattern = "yyyy-MM-dd", type = "string", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;
    @Schema(description = "일정 시작 시간", example = "10:00:00", pattern = "HH:mm:ss", type = "string", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private LocalTime startTime;
    @Schema(description = "일정 종료일", example = "2023-06-02", pattern = "yyyy-MM-dd", type = "string", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;
    @Schema(description = "일정 종료 시간", example = "20:00:00", pattern = "HH:mm:ss", type = "string", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private LocalTime endTime;
    @Schema(description = "하루종일", example = "false", required = true)
    private Boolean isAllDay = false;
    @Schema(description = "일정 구분", example = "ACADEMY", required = true)
    private ScheduleType scheduleType = ScheduleType.NONE;
    @Schema(description = "학원 고유 아이디", example = "1")
    private Long academyId = 0L;
    @Schema(description = "학원 이름", example = "대성수학학원")
    private String academyName = "";
    @Schema(description = "컬러", example = "#FA8431", required = true)
    @NotBlank
    private String color = "";
    @Schema(description = "메모", example = "#FA8431")
    private String memo = "";
    @Schema(description = "반복 타입", example = "DAY")
    private CycleType cycleType = CycleType.NONE;
    @Schema(description = "반복 요일(1:월, 2:화, 3:수, 4:목, 5:금, 6:토, 7:일", example = "1,3,5,7")
    private String cycleDays = "";
    @Schema(description = "반복 주기", example = "EVERY_DAY")
    private CalendarCycle calendarCycle = CalendarCycle.NONE;
    @Schema(description = "반복 종료일", example = "2023-06-02", pattern = "yyyy-MM-dd", type = "string", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate cycleEndDate;
    @Schema(description = "자녀 알림 타입", example = "FIVE_MINUTES_AGO")
    private ScheduleAlarmType scheduleAlarmType = ScheduleAlarmType.NONE;
    @Schema(description = "부모 알림 타입", example = "FIVE_MINUTES_AGO")
    private ScheduleAlarmType scheduleAlarmTypeForParent = ScheduleAlarmType.NONE;

    @Schema(description = "결제일 유무", example = "true")
    private Boolean usePay = false;
    @Schema(description = "결제일", example = "2023-05-01", pattern = "yyyy-MM-dd", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate payDate;
    @Schema(description = "결제 주기", example = "ONE_MONTH")
    private PayCycle payCycle = PayCycle.NONE;
    @Schema(description = "결제 종료일", example = "2023-05-01", pattern = "yyyy-MM-dd", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate payCycleEndDate;
    @Schema(description = "결제금액", example = "10000")
    private Long amount = 0L;
    @Schema(description = "결제 알림 사용유무", example = "false")
    private Boolean usePaymentAlarm = false;
    @Schema(description = "결제 알림 타입", example = "YESTERDAY_9AM")
    private PayAlarmType payAlarmType = PayAlarmType.NONE;

    @Schema(description = "중요일정 유무", example = "true")
    private Boolean isImportant = false;
}