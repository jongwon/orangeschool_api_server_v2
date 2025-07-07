package com.orangeschool.orangeschoolapiserver.domain.schedule.repository;


import com.orangeschool.orangeschoolapiserver.domain.schedule.entity.DayMessage;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DayMessageRepository extends PagingAndSortingRepository<DayMessage, Long> {

    Optional<DayMessage> findByDayDateAndCommonMemberId(LocalDate dayDate, Long commonMemberId);
}
