package com.orangeschool.schedule.schedule.repository;


import com.orangeschool.schedule.schedule.entity.Schedule;
import com.orangeschool.schedule.schedule.repository.ScheduleRepositoryCustom;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ScheduleRepository extends PagingAndSortingRepository<Schedule, Long>, ScheduleRepositoryCustom {

}
