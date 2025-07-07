package com.orangeschool.support.location.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.support.location.dto.LocationDto;

public interface LocationRepositoryCustom {

    Page<LocationDto> search(Pageable pageable, KeywordSearchDto keywordSearchDto);
}
