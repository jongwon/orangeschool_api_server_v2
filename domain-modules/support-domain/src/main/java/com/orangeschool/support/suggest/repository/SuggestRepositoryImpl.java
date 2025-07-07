package com.orangeschool.support.suggest.repository;


import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.support.suggest.dto.SuggestDto;
import com.orangeschool.support.suggest.entity.Suggest;
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

import static com.orangeschool.support.suggest.entity.QSuggest.suggest;


@Repository
@RequiredArgsConstructor
public class SuggestRepositoryImpl implements SuggestRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<SuggestDto> search(Pageable pageable, KeywordSearchDto keywordSearchDto) {

        List<Suggest> suggests = queryFactory
                .select(suggest)
                .from(suggest)
                .where(
                        keywordContains(keywordSearchDto.getKeyword())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<Suggest> countQuery = queryFactory
                .select(suggest)
                .from(suggest)
                .where(
                        keywordContains(keywordSearchDto.getKeyword())
                );

        return PageableExecutionUtils.getPage(suggests.stream().map(
                SuggestDto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }


    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Suggest.class, "suggest");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }
    private BooleanExpression keywordContains(String keyword) {

        if (keyword == null) {
            keyword = "";
        }

        return suggest.commonMember.email.contains(keyword);
    }


}
