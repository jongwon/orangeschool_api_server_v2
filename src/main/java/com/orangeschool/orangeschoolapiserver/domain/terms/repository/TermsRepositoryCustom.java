package com.orangeschool.orangeschoolapiserver.domain.terms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.orangeschool.orangeschoolapiserver.domain.terms.dto.TermsDto;

public interface TermsRepositoryCustom {

    Page<TermsDto> search(Pageable pageable);
}
