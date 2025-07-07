package com.orangeschool.orangeschoolapiserver.domain.academy.repository;

import com.orangeschool.orangeschoolapiserver.domain.academy.dto.AcademyDto;
import com.orangeschool.orangeschoolapiserver.domain.academy.dto.AcademySearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AcademyRepositoryCustom {

    Page<AcademyDto> search(Pageable pageable, AcademySearchDto academySearchDto);
}
