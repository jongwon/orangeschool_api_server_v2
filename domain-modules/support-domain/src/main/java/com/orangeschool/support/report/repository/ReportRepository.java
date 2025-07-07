package com.orangeschool.support.report.repository;

import com.orangeschool.support.report.entity.Report;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReportRepository extends PagingAndSortingRepository<Report, Long> , ReportRepositoryCustom {
}
