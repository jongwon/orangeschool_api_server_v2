package com.orangeschool.education.schoolSchedule.repository;

import com.orangeschool.education.schoolSchedule.entity.SchoolSchedule;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface SchoolScheduleRepository extends PagingAndSortingRepository<SchoolSchedule, Long>, SchoolScheduleRepositoryCustom {

    List<SchoolSchedule> findByCommonMemberId(Long commonMemberId);

    Optional<SchoolSchedule> findByCommonMemberIdAndKeyStringValue(Long commonMemberId, String keyStringValue);


}
