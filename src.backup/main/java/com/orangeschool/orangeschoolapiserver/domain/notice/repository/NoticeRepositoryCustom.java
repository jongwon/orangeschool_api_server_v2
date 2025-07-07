package com.orangeschool.orangeschoolapiserver.domain.notice.repository;

import com.orangeschool.orangeschoolapiserver.common.dto.request.KeywordSearchDto;
import com.orangeschool.orangeschoolapiserver.domain.notice.dto.NoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {

    Page<NoticeDto> search(Pageable pageable, KeywordSearchDto keywordSearchDto);
}
