package com.orangeschool.community.pick.reply.repository;


import com.orangeschool.community.pick.reply.dto.PickReplyDto;
import com.orangeschool.community.pick.reply.dto.PickReplySearchDto;
import com.orangeschool.community.pick.reply.entity.PickReply;
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

import static com.orangeschool.community.pick.reply.entity.QPickReply.pickReply;


@Repository
@RequiredArgsConstructor
public class PickReplyRepositoryImpl implements PickReplyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PickReplyDto> search(Pageable pageable, Long pickCommentId, PickReplySearchDto pickReplySearchDto) {

        List<PickReply> pickReplies = queryFactory
                .select(pickReply)
                .from(pickReply)
                .where(
                        keywordContains(pickReplySearchDto.getKeyword()),
                        pickReply.pickComment.id.eq(pickCommentId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<PickReply> countQuery = queryFactory
                .select(pickReply)
                .from(pickReply)
                .where(
                        keywordContains(pickReplySearchDto.getKeyword()),
                        pickReply.pickComment.id.eq(pickCommentId)
                );

        return PageableExecutionUtils.getPage(pickReplies.stream().map(
                PickReplyDto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(PickReply.class, "pickReply");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }

    public BooleanExpression keywordContains(String keyword) {

        if (keyword == null) {
            keyword = "";
        }

        return pickReply.content.contains(keyword);
    }
}
