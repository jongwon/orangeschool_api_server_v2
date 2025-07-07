package com.orangeschool.orangeschoolapiserver.domain.report.repository;

import com.orangeschool.orangeschoolapiserver.domain.report.entity.Report;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReportRepository extends PagingAndSortingRepository<Report, Long> , ReportRepositoryCustom {
}
