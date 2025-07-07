package com.orangeschool.support.alarm.repository;

import com.orangeschool.support.alarm.dto.AlarmDto;
import com.orangeschool.support.alarm.dto.AlarmFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlarmRepositoryCustom {

    Page<AlarmDto> search(Pageable pageable, AlarmFilterDto alarmFilterDto);
}
