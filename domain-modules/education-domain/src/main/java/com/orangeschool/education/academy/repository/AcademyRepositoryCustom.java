package com.orangeschool.education.academy.repository;

import com.orangeschool.education.academy.dto.AcademyDto;
import com.orangeschool.education.academy.dto.AcademySearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AcademyRepositoryCustom {

    Page<AcademyDto> search(Pageable pageable, AcademySearchDto academySearchDto);
}
