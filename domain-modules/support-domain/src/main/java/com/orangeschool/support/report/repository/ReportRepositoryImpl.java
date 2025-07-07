package com.orangeschool.support.report.repository;


import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.support.report.dto.ReportDto;
import com.orangeschool.support.report.entity.Report;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.orangeschool.support.report.entity.QReport.report;


@Repository
@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ReportDto> search(Pageable pageable, KeywordSearchDto keywordSearchDto) {

        List<Report> reports = queryFactory
                .select(report)
                .from(report)
                .where(
                        keywordContains(keywordSearchDto.getKeyword())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<Report> countQuery = queryFactory
                .select(report)
                .from(report)
                .where(
                        keywordContains(keywordSearchDto.getKeyword())
                );

        return PageableExecutionUtils.getPage(reports.stream().map(
                ReportDto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }


    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Report.class, "report");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }
    private BooleanExpression keywordContains(String keyword) {

        if (keyword == null) {
            keyword = "";
        }

        return report.reporter.email.contains(keyword);
    }


}
