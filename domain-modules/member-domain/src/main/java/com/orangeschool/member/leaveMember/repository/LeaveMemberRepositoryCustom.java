package com.orangeschool.member.leaveMember.repository;

import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.member.leaveMember.dto.LeaveMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LeaveMemberRepositoryCustom {
    Page<LeaveMemberDto> search(Pageable pageable, KeywordSearchDto keywordSearchDto);
}
