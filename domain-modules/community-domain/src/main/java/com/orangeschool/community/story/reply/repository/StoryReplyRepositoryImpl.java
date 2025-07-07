package com.orangeschool.community.story.reply.repository;


import com.orangeschool.community.story.reply.dto.StoryReplyDto;
import com.orangeschool.community.story.reply.dto.StoryReplySearchDto;
import com.orangeschool.community.story.reply.entity.StoryReply;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
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

import static com.orangeschool.community.story.reply.entity.QStoryReply.storyReply;


@Repository
@RequiredArgsConstructor
public class StoryReplyRepositoryImpl implements StoryReplyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<StoryReplyDto> search(Pageable pageable, Long storyCommentId, StoryReplySearchDto storyReplySearchDto) {

        List<StoryReply> storyReplies = queryFactory
                .select(storyReply)
                .from(storyReply)
                .where(
                        keywordContains(storyReplySearchDto.getKeyword()),
                        storyReply.storyComment.id.eq(storyCommentId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<StoryReply> countQuery = queryFactory
                .select(storyReply)
                .from(storyReply)
                .where(
                        keywordContains(storyReplySearchDto.getKeyword()),
                        storyReply.storyComment.id.eq(storyCommentId)
                );

        return PageableExecutionUtils.getPage(storyReplies.stream().map(
                StoryReplyDto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(StoryReply.class, "storyReply");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }

    public BooleanExpression keywordContains(String keyword) {

        if (keyword == null) {
            keyword = "";
        }

        return storyReply.content.contains(keyword);
    }
}
