package com.orangeschool.orangeschoolapiserver.domain.challenge.repository;

import com.orangeschool.orangeschoolapiserver.common.enums.ChallengeStatus;
import com.orangeschool.orangeschoolapiserver.domain.challenge.dto.ChallengeDto;
import com.orangeschool.orangeschoolapiserver.domain.challenge.dto.ChallengeFilterDto;
import com.orangeschool.orangeschoolapiserver.domain.challenge.entity.Challenge;
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

import static com.orangeschool.orangeschoolapiserver.domain.challenge.entity.QChallenge.challenge;


@Repository
@RequiredArgsConstructor
public class ChallengeRepositoryImpl implements ChallengeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChallengeDto> search(Long childId, Pageable pageable, ChallengeFilterDto challengeFilterDto) {

        List<Challenge> challenges = queryFactory
                .select(challenge)
                .from(challenge)
                .where(
                        challenge.commonMember.id.eq(childId),
                        challengeStatusFilter(challengeFilterDto.getChallengeStatus())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<Challenge> countQuery = queryFactory
                .select(challenge)
                .from(challenge)
                .where(
                        challengeStatusFilter(challengeFilterDto.getChallengeStatus())
                );

        return PageableExecutionUtils.getPage(challenges.stream().map(
                ChallengeDto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Challenge.class, "challenge");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }

    private BooleanExpression challengeStatusFilter(ChallengeStatus challengeStatus) {

        if (challengeStatus == null || challengeStatus == ChallengeStatus.NONE) {
            return null;
        }

        return challenge.challengeStatus.eq(challengeStatus);
    }
}
