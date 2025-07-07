package com.orangeschool.support.memberAlarm.repository;


import com.orangeschool.support.memberAlarm.entity.MemberAlarm;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MemberAlarmRepository extends PagingAndSortingRepository<MemberAlarm, Long>, MemberAlarmRepositoryCustom {

    int countByCommonMemberIdAndIsRead(Long commonMemberId, Boolean isRead);
}
