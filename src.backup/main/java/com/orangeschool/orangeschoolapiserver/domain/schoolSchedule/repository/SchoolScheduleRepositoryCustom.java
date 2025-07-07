package com.orangeschool.orangeschoolapiserver.domain.schoolSchedule.repository;

import com.orangeschool.orangeschoolapiserver.domain.schoolSchedule.dto.SchoolScheduleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SchoolScheduleRepositoryCustom {

    Page<SchoolScheduleDto> search(Pageable pageable);
}
