package com.orangeschool.schedule.scheduleTemp.repository;

import com.orangeschool.common.enums.ConfirmStatus;
import com.orangeschool.member.commonMember.entity.QCommonMember;
import com.orangeschool.schedule.entity.Schedule;
import com.orangeschool.schedule.scheduleTemp.dto.ScheduleTempDto;
import com.orangeschool.schedule.scheduleTemp.entity.ScheduleTemp;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
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

import static com.orangeschool.schedule.scheduleTemp.entity.QScheduleTemp.scheduleTemp;


@Repository
@RequiredArgsConstructor
public class ScheduleTempRepositoryImpl implements ScheduleTempRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ScheduleTempDto> searchByParentIdOrReferralCode(Pageable pageable, Long parentId, String referralCode) {

        QCommonMember parentCommonMember = new QCommonMember("parentCommonMember");

        BooleanExpression condition = scheduleTemp.commonMember.parentId.eq(parentId);

        if (referralCode != null && !referralCode.isEmpty()) {
            condition = condition.or(
                    JPAExpressions
                            .select(parentCommonMember.myReferralCode)
                            .from(parentCommonMember)
                            .where(parentCommonMember.id.eq(scheduleTemp.commonMember.parentId))
                            .eq(referralCode)
            );
        }

        List<ScheduleTemp> schedules = queryFactory
                .select(scheduleTemp)
                .from(scheduleTemp)
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<ScheduleTemp> countQuery = queryFactory
                .select(scheduleTemp)
                .from(scheduleTemp)
                .where(condition);

        return PageableExecutionUtils.getPage(schedules.stream().map(
                ScheduleTempDto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    @Override
    public Page<ScheduleTempDto> searchByChildId(Pageable pageable, Long childId) {

        List<ScheduleTemp> schedules = queryFactory
                .select(scheduleTemp)
                .from(scheduleTemp)
                .where(
                        scheduleTemp.commonMember.id.eq(childId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<ScheduleTemp> countQuery = queryFactory
                .select(scheduleTemp)
                .from(scheduleTemp)
                .where(
                        scheduleTemp.commonMember.id.eq(childId)
                );

        return PageableExecutionUtils.getPage(schedules.stream().map(
                ScheduleTempDto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    @Override
    public Long countByParentIdOrReferralCode(Long parentId, String referralCode) {

        QCommonMember parentCommonMember = new QCommonMember("parentCommonMember");

        BooleanExpression condition = scheduleTemp.commonMember.parentId.eq(parentId);

        if (referralCode != null && !referralCode.isEmpty()) {
            condition = condition.or(
                    JPAExpressions
                            .select(parentCommonMember.myReferralCode)
                            .from(parentCommonMember)
                            .where(parentCommonMember.id.eq(scheduleTemp.commonMember.parentId))
                            .eq(referralCode)
            );
        }

        JPAQuery<Long> countQuery = queryFactory
                .select(scheduleTemp.count())
                .from(scheduleTemp)
                .where(condition.and(scheduleTemp.confirmStatus.eq(ConfirmStatus.WAIT)));

        return countQuery.fetchOne();
    }

    @Override
    public Long countByChildId(Long childId) {

        JPAQuery<Long> countQuery = queryFactory
                .select(scheduleTemp.count())
                .from(scheduleTemp)
                .where(
                        scheduleTemp.commonMember.id.eq(childId)
                                .and(scheduleTemp.confirmStatus.eq(ConfirmStatus.WAIT))
                );

        return countQuery.fetchOne();
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Schedule.class, "scheduleTemp");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }
}
