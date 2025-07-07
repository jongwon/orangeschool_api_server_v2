package com.orangeschool.support.report.repository;


import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.support.report.dto.ReportDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportRepositoryCustom {

    Page<ReportDto> search(Pageable pageable, KeywordSearchDto keywordSearchDto);
}
