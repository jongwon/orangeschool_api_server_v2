package com.orangeschool.support.visitor.repository;


import com.orangeschool.common.enums.MemberType;
import com.orangeschool.support.visitor.dto.VisitorDto;
import com.orangeschool.support.visitor.entity.Visitor;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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

import static com.orangeschool.support.visitor.entity.QVisitor.visitor;


@Repository
@RequiredArgsConstructor
public class VisitorRepositoryImpl implements VisitorRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<VisitorDto> search(Pageable pageable, MemberType memberType) {
        List<Visitor> visitorList = queryFactory
                .select(visitor)
                .from(visitor)
                .where(
                        visitor.memberType.eq(memberType)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<Visitor> countQuery = queryFactory
                .select(visitor)
                .from(visitor)
                .where(
                        visitor.memberType.eq(memberType)
                );

        return PageableExecutionUtils.getPage(visitorList.stream().map(VisitorDto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Visitor.class, "visitor");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }
}
