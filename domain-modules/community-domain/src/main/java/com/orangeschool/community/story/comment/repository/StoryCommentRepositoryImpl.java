package com.orangeschool.community.story.comment.repository;


import com.orangeschool.community.story.comment.dto.StoryCommentDto;
import com.orangeschool.community.story.comment.dto.StoryCommentSearchDto;
import com.orangeschool.community.story.comment.entity.StoryComment;
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

import static com.orangeschool.community.story.comment.entity.QStoryComment.storyComment;


@Repository
@RequiredArgsConstructor
public class StoryCommentRepositoryImpl implements StoryCommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<StoryCommentDto> search(Pageable pageable, Long storyId, StoryCommentSearchDto storyCommentSearchDto) {

        List<StoryComment> storyComments = queryFactory
                .select(storyComment)
                .from(storyComment)
                .where(
                        keywordContains(storyCommentSearchDto.getKeyword()),
                        storyComment.story.id.eq(storyId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<StoryComment> countQuery = queryFactory
                .select(storyComment)
                .from(storyComment)
                .where(
                        keywordContains(storyCommentSearchDto.getKeyword()),
                        storyComment.story.id.eq(storyId)
                );

        return PageableExecutionUtils.getPage(storyComments.stream().map(
                StoryCommentDto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(StoryComment.class, "storyComment");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }

    public BooleanExpression keywordContains(String keyword) {

        if (keyword == null) {
            keyword = "";
        }

        return storyComment.content.contains(keyword);
    }
}
