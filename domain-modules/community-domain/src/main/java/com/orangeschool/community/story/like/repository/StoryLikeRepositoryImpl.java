package com.orangeschool.community.story.like.repository;


import com.orangeschool.community.story.like.dto.StoryLikeDto;
import com.orangeschool.community.story.like.dto.StoryLikeSearchDto;
import com.orangeschool.community.story.like.entity.StoryLike;
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

import static com.orangeschool.community.story.like.entity.QStoryLike.storyLike;


@Repository
@RequiredArgsConstructor
public class StoryLikeRepositoryImpl implements StoryLikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<StoryLikeDto> search(Pageable pageable, Long storyId, StoryLikeSearchDto storyLikeSearchDto) {

        List<StoryLike> storyLikes = queryFactory
                .select(storyLike)
                .from(storyLike)
                .where(
                        storyLike.story.id.eq(storyId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<StoryLike> countQuery = queryFactory
                .select(storyLike)
                .from(storyLike)
                .where(
                        storyLike.story.id.eq(storyId)
                );

        return PageableExecutionUtils.getPage(storyLikes.stream().map(
                StoryLikeDto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(StoryLike.class, "storyLike");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }
}
