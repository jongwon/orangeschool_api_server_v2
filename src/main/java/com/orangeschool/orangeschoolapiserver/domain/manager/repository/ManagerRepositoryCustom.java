package com.orangeschool.orangeschoolapiserver.domain.manager.repository;

import com.orangeschool.orangeschoolapiserver.common.dto.request.KeywordSearchDto;
import com.orangeschool.orangeschoolapiserver.domain.manager.dto.ManagerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ManagerRepositoryCustom {

    Page<ManagerDto> search(Pageable pageable, KeywordSearchDto keywordSearchDto);
}
