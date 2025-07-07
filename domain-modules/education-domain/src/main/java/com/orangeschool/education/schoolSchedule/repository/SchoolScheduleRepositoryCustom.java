package com.orangeschool.education.schoolSchedule.repository;

import com.orangeschool.education.schoolSchedule.dto.SchoolScheduleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SchoolScheduleRepositoryCustom {

    Page<SchoolScheduleDto> search(Pageable pageable);
}
