package com.orangeschool.schedule.schedule.repository;


import com.orangeschool.schedule.schedule.entity.DayMessage;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DayMessageRepository extends PagingAndSortingRepository<DayMessage, Long> {

    Optional<DayMessage> findByDayDateAndCommonMemberId(LocalDate dayDate, Long commonMemberId);
}
