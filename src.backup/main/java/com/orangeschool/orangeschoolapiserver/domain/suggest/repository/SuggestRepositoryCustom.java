package com.orangeschool.orangeschoolapiserver.domain.suggest.repository;


import com.orangeschool.orangeschoolapiserver.common.dto.request.KeywordSearchDto;
import com.orangeschool.orangeschoolapiserver.domain.suggest.dto.SuggestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SuggestRepositoryCustom {

    Page<SuggestDto> search(Pageable pageable, KeywordSearchDto keywordSearchDto);
}
