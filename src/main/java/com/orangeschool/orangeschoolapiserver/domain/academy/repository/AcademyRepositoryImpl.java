package com.orangeschool.orangeschoolapiserver.domain.academy.repository;

import com.orangeschool.orangeschoolapiserver.domain.academy.dto.AcademyDto;
import com.orangeschool.orangeschoolapiserver.domain.academy.dto.AcademySearchDto;
import com.orangeschool.orangeschoolapiserver.domain.academy.entity.Academy;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.orangeschool.orangeschoolapiserver.domain.academy.entity.QAcademy.academy;


@Repository
@RequiredArgsConstructor
public class AcademyRepositoryImpl implements AcademyRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AcademyDto> search(Pageable pageable, AcademySearchDto academySearchDto) {

        String addressKeyword = academySearchDto.getAddressKeyword();

        String[] addressKeywords = addressKeyword != null
                ? Arrays.stream(addressKeyword.split("#"))
                .filter(keyword -> !keyword.trim().isEmpty())
                .toArray(String[]::new)
                : new String[0];

        List<Academy> academys = queryFactory
                .select(academy)
                .from(academy)
                .where(
                        addressKeywords.length > 0 ?
                                addressKeywordContains(addressKeywords).and(keywordContains(academySearchDto.getKeyword())) : keywordContains(academySearchDto.getKeyword())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<Academy> countQuery = queryFactory
                .select(academy)
                .from(academy)
                .where(
                        addressKeywords.length > 0 ?
                                addressKeywordContains(addressKeywords).and(keywordContains(academySearchDto.getKeyword())) : keywordContains(academySearchDto.getKeyword())
                );

        return PageableExecutionUtils.getPage(academys.stream().map(
                AcademyDto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Academy.class, "academy");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }

    private BooleanExpression keywordContains(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return null;
        }

        // 공백을 기준으로 키워드 분리
        String[] keywords = keyword.trim().replaceAll(" +", " ").split(" ");

        // 분리된 키워드를 모두 포함하는 AND 조건 생성
        return Arrays.stream(keywords)
                .map(kw -> Expressions.stringTemplate("replace({0}, ' ', '')", academy.academyName).contains(kw.replaceAll(" ", ""))
                        .or(Expressions.stringTemplate("replace({0}, ' ', '')", academy.address).contains(kw.replaceAll(" ", ""))))
                .reduce(BooleanExpression::and)
                .orElse(null);
    }

    private BooleanExpression addressKeywordContains(String[] addressKeywords) {
        if (addressKeywords == null || addressKeywords.length == 0) {
            return null;
        }

        BooleanExpression addressCondition = null;

        for (String keyword : addressKeywords) {
            BooleanExpression currentCondition =
                    Expressions.stringTemplate("replace({0}, ' ', '')", academy.address).contains(keyword.replaceAll(" ", ""));

            if (addressCondition == null) {
                addressCondition = currentCondition;
            } else {
                addressCondition = addressCondition.or(currentCondition);
            }
        }

        return addressCondition;
    }
}
