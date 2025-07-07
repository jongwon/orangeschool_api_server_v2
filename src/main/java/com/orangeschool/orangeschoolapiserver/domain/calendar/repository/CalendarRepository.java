package com.orangeschool.orangeschoolapiserver.domain.calendar.repository;


import com.orangeschool.orangeschoolapiserver.domain.calendar.entity.Calendar;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;

public interface CalendarRepository extends PagingAndSortingRepository<Calendar, Long>, CalendarRepositoryCustom {

    void deleteByScheduleId(Long scheduleId);

    int countByScheduleId(Long scheduleId);

    void deleteByScheduleIdAndStartDateGreaterThanEqual(Long scheduleId, LocalDate updateStandardDate);
}
