package com.orangeschool.support.alarm.repository;


import com.orangeschool.support.alarm.entity.Alarm;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AlarmRepository extends PagingAndSortingRepository<Alarm, Long>, AlarmRepositoryCustom {

}
