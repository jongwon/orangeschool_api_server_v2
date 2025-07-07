package com.orangeschool.support.terms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.orangeschool.support.terms.dto.TermsDto;

public interface TermsRepositoryCustom {

    Page<TermsDto> search(Pageable pageable);
}
