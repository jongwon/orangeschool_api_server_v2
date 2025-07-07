package com.orangeschool.community.pick.like.repository;


import com.orangeschool.community.pick.like.dto.PickLikeDto;
import com.orangeschool.community.pick.like.dto.PickLikeSearchDto;
import com.orangeschool.community.pick.like.entity.PickLike;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.community.Page;
import org.springframework.data.community.Pageable;
import org.springframework.data.community.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.orangeschool.community.pick.like.entity.QPickLike.pickLike;


@Repository
@RequiredArgsConstructor
public class PickLikeRepositoryImpl implements PickLikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PickLikeDto> search(Pageable pageable, Long pickId, PickLikeSearchDto pickLikeSearchDto) {

        List<PickLike> pickLikes = queryFactory
                .select(pickLike)
                .from(pickLike)
                .where(
                        pickLike.pick.id.eq(pickId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<PickLike> countQuery = queryFactory
                .select(pickLike)
                .from(pickLike)
                .where(
                        pickLike.pick.id.eq(pickId)
                );

        return PageableExecutionUtils.getPage(pickLikes.stream().map(
                PickLikeDto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(PickLike.class, "pickLike");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }
}
