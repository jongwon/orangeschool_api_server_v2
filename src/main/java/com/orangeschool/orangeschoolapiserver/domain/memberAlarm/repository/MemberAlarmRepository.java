package com.orangeschool.orangeschoolapiserver.domain.memberAlarm.repository;


import com.orangeschool.orangeschoolapiserver.domain.memberAlarm.entity.MemberAlarm;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MemberAlarmRepository extends PagingAndSortingRepository<MemberAlarm, Long>, MemberAlarmRepositoryCustom {

    int countByCommonMemberIdAndIsRead(Long commonMemberId, Boolean isRead);
}
