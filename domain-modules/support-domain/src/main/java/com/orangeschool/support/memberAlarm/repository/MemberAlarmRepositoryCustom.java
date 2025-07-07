package com.orangeschool.support.memberAlarm.repository;

import com.orangeschool.support.memberAlarm.dto.MemberAlarmDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberAlarmRepositoryCustom {

    Page<MemberAlarmDto> search(Pageable pageable, Long commonMemberId);
}
