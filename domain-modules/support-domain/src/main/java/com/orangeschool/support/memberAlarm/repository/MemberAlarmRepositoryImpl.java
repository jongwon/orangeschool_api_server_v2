package com.orangeschool.support.memberAlarm.repository;

import com.orangeschool.support.alarm.entity.Alarm;
import com.orangeschool.support.memberAlarm.dto.MemberAlarmDto;
import com.orangeschool.support.memberAlarm.entity.MemberAlarm;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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

import static com.orangeschool.support.memberAlarm.entity.QMemberAlarm.memberAlarm;


@Repository
@RequiredArgsConstructor
public class MemberAlarmRepositoryImpl implements MemberAlarmRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MemberAlarmDto> search(Pageable pageable, Long commonMemberId) {

        List<MemberAlarm> alarms = queryFactory
                .select(memberAlarm)
                .from(memberAlarm)
                .where(
                        memberAlarm.commonMember.id.eq(commonMemberId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<MemberAlarm> countQuery = queryFactory
                .select(memberAlarm)
                .from(memberAlarm)
                .where(
                        memberAlarm.commonMember.id.eq(commonMemberId)
                );

        return PageableExecutionUtils.getPage(alarms.stream().map(
                MemberAlarmDto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Alarm.class, "memberAlarm");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }
}
