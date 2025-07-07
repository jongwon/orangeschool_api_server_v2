package com.orangeschool.orangeschoolapiserver.domain.schedule.repository;

import com.orangeschool.orangeschoolapiserver.domain.schedule.dto.ScheduleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepositoryCustom {

    List<ScheduleDto> searchWeekSchedules(Long commonMemberId, LocalDate weekStartDate);

    Page<ScheduleDto> searchByCommonMemberIdAndAcademy(Pageable pageable, Long commonMemberId);
}
