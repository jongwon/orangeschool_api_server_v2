package com.orangeschool.orangeschoolapiserver.domain.scheduleAlarm.repository;

import com.orangeschool.orangeschoolapiserver.domain.scheduleAlarm.entity.ScheduleAlarm;

import javax.transaction.Transactional;
import java.util.List;

public interface ScheduleAlarmRepositoryCustom {

    List<ScheduleAlarm> searchByNow();

    @Transactional
    void deleteToDay();
}
