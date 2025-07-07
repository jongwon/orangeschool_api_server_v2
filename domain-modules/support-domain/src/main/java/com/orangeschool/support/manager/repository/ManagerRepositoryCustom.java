package com.orangeschool.support.manager.repository;

import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.support.manager.dto.ManagerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ManagerRepositoryCustom {

    Page<ManagerDto> search(Pageable pageable, KeywordSearchDto keywordSearchDto);
}
