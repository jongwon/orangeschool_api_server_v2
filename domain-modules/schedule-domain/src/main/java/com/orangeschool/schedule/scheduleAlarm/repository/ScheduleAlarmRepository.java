package com.orangeschool.schedule.scheduleAlarm.repository;


import com.orangeschool.schedule.scheduleAlarm.entity.ScheduleAlarm;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ScheduleAlarmRepository extends PagingAndSortingRepository<ScheduleAlarm, Long>, ScheduleAlarmRepositoryCustom {

    void deleteByCalendarId(Long calendarId);
    void deleteByScheduleId(Long scheduleId);
}
