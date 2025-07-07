package com.orangeschool.orangeschoolapiserver.domain.alarm.repository;

import com.orangeschool.orangeschoolapiserver.common.enums.AlarmMemberType;
import com.orangeschool.orangeschoolapiserver.domain.alarm.dto.AlarmDto;
import com.orangeschool.orangeschoolapiserver.domain.alarm.dto.AlarmFilterDto;
import com.orangeschool.orangeschoolapiserver.domain.alarm.entity.Alarm;
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

import static com.orangeschool.orangeschoolapiserver.domain.alarm.entity.QAlarm.alarm;


@Repository
@RequiredArgsConstructor
public class AlarmRepositoryImpl implements AlarmRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AlarmDto> search(Pageable pageable, AlarmFilterDto alarmFilterDto) {

        List<Alarm> alarms = queryFactory
                .select(alarm)
                .from(alarm)
                .where(
                        keywordContains(alarmFilterDto.getKeyword()),
                        alarmMemberTypeFilter(alarmFilterDto.getAlarmMemberType())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<Alarm> countQuery = queryFactory
                .select(alarm)
                .from(alarm)
                .where(
                        keywordContains(alarmFilterDto.getKeyword()),
                        alarmMemberTypeFilter(alarmFilterDto.getAlarmMemberType())
                );

        return PageableExecutionUtils.getPage(alarms.stream().map(
                AlarmDto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Alarm.class, "alarm");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }

    private BooleanExpression keywordContains(String keyword) {

        if (keyword == null) {
            keyword = "";
        }

        return alarm.title.contains(keyword);
    }

    private BooleanExpression alarmMemberTypeFilter(AlarmMemberType alarmMemberType) {

        if (alarmMemberType == null || alarmMemberType == alarmMemberType.NONE) {
            return null;
        }

        return alarm.alarmMemberType.eq(alarmMemberType);
    }
}
