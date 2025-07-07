package com.orangeschool.orangeschoolapiserver.domain.calendar.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaymentResponseDto {

    private List<CalendarDto> paySchedule;
    private Long paySum;
    private Long previousMonth;
}
