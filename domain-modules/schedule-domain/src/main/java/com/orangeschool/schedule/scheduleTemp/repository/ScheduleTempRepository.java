package com.orangeschool.schedule.scheduleTemp.repository;


import com.orangeschool.schedule.scheduleTemp.entity.ScheduleTemp;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ScheduleTempRepository extends PagingAndSortingRepository<ScheduleTemp, Long>, ScheduleTempRepositoryCustom {

}
