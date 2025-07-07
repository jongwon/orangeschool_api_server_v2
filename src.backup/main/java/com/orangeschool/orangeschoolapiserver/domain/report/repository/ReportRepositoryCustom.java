package com.orangeschool.orangeschoolapiserver.domain.report.repository;


import com.orangeschool.orangeschoolapiserver.common.dto.request.KeywordSearchDto;
import com.orangeschool.orangeschoolapiserver.domain.report.dto.ReportDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportRepositoryCustom {

    Page<ReportDto> search(Pageable pageable, KeywordSearchDto keywordSearchDto);
}
