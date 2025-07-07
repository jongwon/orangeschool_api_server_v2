package com.orangeschool.orangeschoolapiserver.domain.alarm.repository;

import com.orangeschool.orangeschoolapiserver.domain.alarm.dto.AlarmDto;
import com.orangeschool.orangeschoolapiserver.domain.alarm.dto.AlarmFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlarmRepositoryCustom {

    Page<AlarmDto> search(Pageable pageable, AlarmFilterDto alarmFilterDto);
}
