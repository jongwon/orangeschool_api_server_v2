package com.orangeschool.orangeschoolapiserver.domain.location.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.orangeschool.orangeschoolapiserver.common.dto.request.KeywordSearchDto;
import com.orangeschool.orangeschoolapiserver.domain.location.dto.LocationDto;

public interface LocationRepositoryCustom {

    Page<LocationDto> search(Pageable pageable, KeywordSearchDto keywordSearchDto);
}
