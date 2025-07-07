package com.orangeschool.support.manager.repository;

import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.support.manager.dto.ManagerDto;
import com.orangeschool.support.manager.entity.Manager;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.orangeschool.support.manager.entity.QManager.manager;

@Repository
@RequiredArgsConstructor
public class ManagerRepositoryImpl implements ManagerRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ManagerDto> search(Pageable pageable, KeywordSearchDto keywordSearchDto) {

        List<Manager> managers = queryFactory
                .select(manager)
                .from(manager)
                .where(
                        keywordContains(keywordSearchDto.getKeyword()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        long totalSize = queryFactory
                .select(manager)
                .from(manager)
                .where(
                        keywordContains(keywordSearchDto.getKeyword()))
                .fetch()
                .size();

        return new PageImpl<ManagerDto>(managers.stream().map(
                ManagerDto::create).collect(Collectors.toList()), pageable, totalSize);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Manager.class, "manager");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }

    private BooleanExpression keywordContains(String keyword) {

        if (keyword == null) {
            keyword = "";
        }

        return manager.email.contains(keyword).or(manager.name.contains(keyword));
    }
}
