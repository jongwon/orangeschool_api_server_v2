package com.orangeschool.schedule.scheduleTemp.repository;

import com.orangeschool.schedule.scheduleTemp.dto.ScheduleTempDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleTempRepositoryCustom {

    Page<ScheduleTempDto> searchByParentIdOrReferralCode(Pageable pageable, Long parentId, String referralCode);

    Page<ScheduleTempDto> searchByChildId(Pageable pageable, Long childId);

    Long countByParentIdOrReferralCode(Long parentId, String referralCode);

    Long countByChildId(Long childId);
}
