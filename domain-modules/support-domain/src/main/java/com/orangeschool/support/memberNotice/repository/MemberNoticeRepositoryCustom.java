package com.orangeschool.support.memberNotice.repository;

import com.orangeschool.support.memberNotice.dto.MemberNoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberNoticeRepositoryCustom {

    Page<MemberNoticeDto> search(Pageable pageable, Long commonMemberId);
}
