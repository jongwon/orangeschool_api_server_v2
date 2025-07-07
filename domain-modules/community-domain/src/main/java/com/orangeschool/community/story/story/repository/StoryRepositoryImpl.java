package com.orangeschool.community.story.story.repository;


import com.orangeschool.community.story.story.dto.StoryDto;
import com.orangeschool.community.story.story.dto.StorySearchDto;
import com.orangeschool.community.story.story.entity.Story;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.orangeschool.community.story.story.entity.QStory.story;


@Repository
@RequiredArgsConstructor
public class StoryRepositoryImpl implements StoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<StoryDto> searchToUser(Pageable pageable, StorySearchDto storySearchDto, Long commonMemberId) {

        String regionCodeTag = storySearchDto.getRegionCodeTag();

        String[] regionTags = regionCodeTag != null
                ? Arrays.stream(regionCodeTag.split("#"))
                .filter(keyword -> !keyword.trim().isEmpty())
                .toArray(String[]::new)
                : new String[0];

        List<Story> storys = queryFactory
                .select(story)
                .from(story)
                .where(
                        keywordContains(storySearchDto.getKeyword()),
                        regionCodeTagContains(regionTags),
                        storySearchDto.getMyFlag() ? story.commonMember.id.eq(commonMemberId) : null,
                        story.isActive.isTrue()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<Story> countQuery = queryFactory
                .select(story)
                .from(story)
                .where(
                        keywordContains(storySearchDto.getKeyword()),
                        regionCodeTagContains(regionTags),
                        storySearchDto.getMyFlag() ? story.commonMember.id.eq(commonMemberId) : null,
                        story.isActive.isTrue()
                );

        return PageableExecutionUtils.getPage(
                storys.stream()
                        .map(story -> StoryDto.create(story, commonMemberId))
                        .collect(Collectors.toList()),
                pageable,
                countQuery::fetchCount
        );
    }

    @Override
    public Page<StoryDto> search(Pageable pageable, StorySearchDto storySearchDto) {

        List<Story> storys = queryFactory
                .select(story)
                .from(story)
                .where(
                        keywordContains(storySearchDto.getKeyword())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<Story> countQuery = queryFactory
                .select(story)
                .from(story)
                .where(
                        keywordContains(storySearchDto.getKeyword())
                );

        return PageableExecutionUtils.getPage(
                storys.stream()
                        .map(StoryDto::create)
                        .collect(Collectors.toList()),
                pageable,
                countQuery::fetchCount
        );
    }

    @Override
    public Story findFirstByOrderByTodayViewCountDesc(StorySearchDto storySearchDto, Long commonMemberId) {

        String regionCodeTag = storySearchDto.getRegionCodeTag();

        String[] regionTags = regionCodeTag != null
                ? Arrays.stream(regionCodeTag.split("#"))
                .filter(keyword -> !keyword.trim().isEmpty())
                .toArray(String[]::new)
                : new String[0];

        Story storyEntity = queryFactory
                .select(story)
                .from(story)
                .where(
                        keywordContains(storySearchDto.getKeyword()),
                        regionCodeTagContains(regionTags),
                        storySearchDto.getMyFlag() ? story.commonMember.id.eq(commonMemberId) : null,
                        story.isActive.isTrue()
                )
                .orderBy(story.todayViewCount.desc(), story.id.desc())
                .limit(1)
                .fetchOne();

        return storyEntity;
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Story.class, "story");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }

    public BooleanExpression keywordContains(String keyword) {

        if (keyword == null) {
            keyword = "";
        }

        return story.title.contains(keyword).or(story.content.contains(keyword).or(story.commonMember.parentNickName.contains(keyword)));
    }

    private BooleanExpression regionCodeTagContains(String[] regionTags) {
        if (regionTags == null || regionTags.length == 0) {
            return null;
        }

        BooleanExpression predicate = null;

        for (String tag : regionTags) {
            if (!tag.isEmpty()) {
                BooleanExpression tagCondition = story.regionCodeTag.contains("#" + tag + "#")
                        .or(story.regionCodeTag.startsWith(tag + "#"))
                        .or(story.regionCodeTag.endsWith("#" + tag))
                        .or(story.regionCodeTag.eq(tag));

                if (predicate == null) {
                    predicate = tagCondition;
                } else {
                    predicate = predicate.or(tagCondition);
                }
            }
        }

        return predicate;
    }
}
