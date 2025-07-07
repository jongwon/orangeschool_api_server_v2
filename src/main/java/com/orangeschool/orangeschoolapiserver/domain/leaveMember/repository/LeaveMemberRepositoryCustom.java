package com.orangeschool.orangeschoolapiserver.domain.leaveMember.repository;

import com.orangeschool.orangeschoolapiserver.common.dto.request.KeywordSearchDto;
import com.orangeschool.orangeschoolapiserver.domain.leaveMember.dto.LeaveMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LeaveMemberRepositoryCustom {
    Page<LeaveMemberDto> search(Pageable pageable, KeywordSearchDto keywordSearchDto);
}
