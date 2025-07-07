package com.orangeschool.orangeschoolapiserver.domain.scheduleTemp.repository;


import com.orangeschool.orangeschoolapiserver.domain.scheduleTemp.entity.ScheduleTemp;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ScheduleTempRepository extends PagingAndSortingRepository<ScheduleTemp, Long>, ScheduleTempRepositoryCustom {

}
