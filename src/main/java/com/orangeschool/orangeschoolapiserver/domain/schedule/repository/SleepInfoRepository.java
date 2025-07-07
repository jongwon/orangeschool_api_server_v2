package com.orangeschool.orangeschoolapiserver.domain.schedule.repository;


import com.orangeschool.orangeschoolapiserver.domain.schedule.entity.SleepInfo;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface SleepInfoRepository extends PagingAndSortingRepository<SleepInfo, Long> {

    Optional<SleepInfo> findByCommonMemberId(Long commonMemberId);
}
