package com.orangeschool.orangeschoolapiserver.domain.scheduleAlarm.repository;


import com.orangeschool.orangeschoolapiserver.domain.scheduleAlarm.entity.ScheduleAlarm;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ScheduleAlarmRepository extends PagingAndSortingRepository<ScheduleAlarm, Long>, ScheduleAlarmRepositoryCustom {

    void deleteByCalendarId(Long calendarId);
    void deleteByScheduleId(Long scheduleId);
}
