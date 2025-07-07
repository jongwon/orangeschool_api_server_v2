package com.orangeschool.orangeschoolapiserver.domain.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orangeschool.orangeschoolapiserver.common.enums.PayAlarmType;
import com.orangeschool.orangeschoolapiserver.common.enums.PayCycle;
import com.orangeschool.orangeschoolapiserver.common.enums.ScheduleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

//학생유저 회원가입
@Data
@Builder
public class CreateSchedulePaymentDto {

    @Schema(description = "회원 고유 아이디", example = "1", required = true)
    private Long commonMemberId = 0L;
    @Schema(description = "제목", example = "일정 제목", required = true)
    @NotBlank
    private String title;
    @Schema(description = "일정 시작일", example = "2023-05-01", pattern = "yyyy-MM-dd", type = "string", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;
    @Schema(description = "일정 종료일", example = "2023-06-02", pattern = "yyyy-MM-dd", type = "string", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;
    @Schema(description = "하루종일", example = "false", required = true)
    private Boolean isAllDay = false;
    @Schema(description = "일정 구분", example = "ACADEMY", required = true)
    private ScheduleType scheduleType = ScheduleType.NONE;
    @Schema(description = "컬러", example = "#FA8431", required = true)
    @NotBlank
    private String color = "";
    @Schema(description = "메모", example = "#FA8431")
    private String memo = "";

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
}