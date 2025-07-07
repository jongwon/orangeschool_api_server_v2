package com.orangeschool.orangeschoolapiserver.domain.pick.comment.repository;


import com.orangeschool.orangeschoolapiserver.domain.pick.comment.dto.PickCommentDto;
import com.orangeschool.orangeschoolapiserver.domain.pick.comment.dto.PickCommentSearchDto;
import com.orangeschool.orangeschoolapiserver.domain.pick.comment.entity.PickComment;
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

import static com.orangeschool.orangeschoolapiserver.domain.pick.comment.entity.QPickComment.pickComment;


@Repository
@RequiredArgsConstructor
public class PickCommentRepositoryImpl implements PickCommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PickCommentDto> search(Pageable pageable, Long pickId, PickCommentSearchDto pickCommentSearchDto) {

        List<PickComment> pickComments = queryFactory
                .select(pickComment)
                .from(pickComment)
                .where(
                        keywordContains(pickCommentSearchDto.getKeyword()),
                        pickComment.pick.id.eq(pickId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<PickComment> countQuery = queryFactory
                .select(pickComment)
                .from(pickComment)
                .where(
                        keywordContains(pickCommentSearchDto.getKeyword()),
                        pickComment.pick.id.eq(pickId)
                );

        return PageableExecutionUtils.getPage(pickComments.stream().map(
                PickCommentDto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(PickComment.class, "pickComment");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }

    public BooleanExpression keywordContains(String keyword) {

        if (keyword == null) {
            keyword = "";
        }

        return pickComment.content.contains(keyword);
    }
}
