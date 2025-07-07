package com.orangeschool.orangeschoolapiserver.domain.calendar.repository;

import com.orangeschool.orangeschoolapiserver.domain.calendar.dto.CalendarDto;
import com.orangeschool.orangeschoolapiserver.domain.calendar.dto.CalendarSearchDto;
import com.orangeschool.orangeschoolapiserver.domain.calendar.dto.GraphDto;

import java.util.List;

public interface CalendarRepositoryCustom {

    List<CalendarDto> searchMonth(Long commonMemberId, CalendarSearchDto calendarSearchDto);

    List<CalendarDto> searchWeek(Long commonMemberId, CalendarSearchDto calendarSearchDto);

    List<CalendarDto> searchWeekByIsAllDay(Long commonMemberId, CalendarSearchDto calendarSearchDto);

    List<CalendarDto> searchDay(Long commonMemberId, CalendarSearchDto calendarSearchDto);

    List<CalendarDto> searchTimeTable(CalendarSearchDto calendarSearchDto);

    List<CalendarDto> searchPayment(List<Long> childList, CalendarSearchDto calendarSearchDto);

    List<GraphDto> searchGraphInfo(List<Long> childList, CalendarSearchDto calendarSearchDto);

//    void deleteByScheduleIdAndStandardDateAfter(Long scheduleId, LocalDate updateStandardDate);
}
