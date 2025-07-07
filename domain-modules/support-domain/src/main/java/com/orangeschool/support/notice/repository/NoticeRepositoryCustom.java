package com.orangeschool.support.notice.repository;

import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.support.notice.dto.NoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {

    Page<NoticeDto> search(Pageable pageable, KeywordSearchDto keywordSearchDto);
}
