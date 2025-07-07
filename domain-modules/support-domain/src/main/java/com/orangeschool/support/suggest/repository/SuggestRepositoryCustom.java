package com.orangeschool.support.suggest.repository;


import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.support.suggest.dto.SuggestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SuggestRepositoryCustom {

    Page<SuggestDto> search(Pageable pageable, KeywordSearchDto keywordSearchDto);
}
