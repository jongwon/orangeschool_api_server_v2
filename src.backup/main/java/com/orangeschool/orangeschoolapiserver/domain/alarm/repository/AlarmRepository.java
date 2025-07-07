package com.orangeschool.orangeschoolapiserver.domain.alarm.repository;


import com.orangeschool.orangeschoolapiserver.domain.alarm.entity.Alarm;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AlarmRepository extends PagingAndSortingRepository<Alarm, Long>, AlarmRepositoryCustom {

}
