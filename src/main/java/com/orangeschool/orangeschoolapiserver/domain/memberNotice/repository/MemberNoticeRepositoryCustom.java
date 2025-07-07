package com.orangeschool.orangeschoolapiserver.domain.memberNotice.repository;

import com.orangeschool.orangeschoolapiserver.domain.memberNotice.dto.MemberNoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberNoticeRepositoryCustom {

    Page<MemberNoticeDto> search(Pageable pageable, Long commonMemberId);
}
