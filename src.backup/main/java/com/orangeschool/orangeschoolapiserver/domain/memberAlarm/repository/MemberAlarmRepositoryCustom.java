package com.orangeschool.orangeschoolapiserver.domain.memberAlarm.repository;

import com.orangeschool.orangeschoolapiserver.domain.memberAlarm.dto.MemberAlarmDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberAlarmRepositoryCustom {

    Page<MemberAlarmDto> search(Pageable pageable, Long commonMemberId);
}
