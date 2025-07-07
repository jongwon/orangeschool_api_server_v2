package com.orangeschool.schedule.scheduleAlarm.repository;

import com.orangeschool.schedule.scheduleAlarm.entity.ScheduleAlarm;

import javax.transaction.Transactional;
import java.util.List;

public interface ScheduleAlarmRepositoryCustom {

    List<ScheduleAlarm> searchByNow();

    @Transactional
    void deleteToDay();
}
