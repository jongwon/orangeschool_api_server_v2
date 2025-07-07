package com.orangeschool.community.follow.repository;

import com.orangeschool.member.commonMember.dto.CommonMemberProfileDto;
import com.orangeschool.community.follow.entity.Follow;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.community.Page;
import org.springframework.data.community.PageImpl;
import org.springframework.data.community.Pageable;
import org.springframework.data.community.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.orangeschool.community.follow.entity.QFollow.follow;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CommonMemberProfileDto> search(Long followingMemberId, Pageable pageable) {

        List<Follow> follows = queryFactory
                .select(follow)
                .from(follow)
                .where(
                        follow.followingMember.id.eq(followingMemberId),
                        follow.followerMember.challengeProgress.isTrue()

                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        long totalSize = queryFactory
                .select(follow)
                .from(follow)
                .where(
                        follow.followingMember.id.eq(followingMemberId),
                        follow.followerMember.challengeProgress.isTrue()
                )
                .fetch()
                .size();

        return new PageImpl<CommonMemberProfileDto>(follows.stream().map(
                follow -> CommonMemberProfileDto.create(follow.getFollowerMember())).collect(Collectors.toList()), pageable, totalSize);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Follow.class, "follow");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }
}
