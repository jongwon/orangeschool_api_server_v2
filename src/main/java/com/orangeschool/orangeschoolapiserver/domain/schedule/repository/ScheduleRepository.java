package com.orangeschool.orangeschoolapiserver.domain.schedule.repository;


import com.orangeschool.orangeschoolapiserver.domain.schedule.entity.Schedule;
import com.orangeschool.orangeschoolapiserver.domain.schedule.repository.ScheduleRepositoryCustom;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ScheduleRepository extends PagingAndSortingRepository<Schedule, Long>, ScheduleRepositoryCustom {

}
