package com.orangeschool.schedule.scheduleAlarm.repository;

import com.orangeschool.schedule.scheduleAlarm.entity.ScheduleAlarm;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.orangeschool.schedule.scheduleAlarm.entity.QScheduleAlarm.scheduleAlarm;


@Repository
@RequiredArgsConstructor
public class ScheduleAlarmRepositoryImpl implements ScheduleAlarmRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ScheduleAlarm> searchByNow() {

        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES); // 시간을 분까지만 자름

        List<ScheduleAlarm> scheduleAlarms = queryFactory
                .select(scheduleAlarm)
                .from(scheduleAlarm)
                .where(
                        scheduleAlarm.alarmDateTime.eq(now)
                )
                .orderBy(scheduleAlarm.alarmDateTime.asc())
                .fetch();

        return scheduleAlarms;
    }

    @Override
    public void deleteToDay() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfToday = now.toLocalDate().atStartOfDay();

        queryFactory
                .delete(scheduleAlarm)
                .where(
                        scheduleAlarm.alarmDateTime.lt(startOfToday)
                )
                .execute();
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(ScheduleAlarm.class, "scheduleAlarm");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }
}
