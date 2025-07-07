package com.orangeschool.orangeschoolapiserver.domain.schedule.repository;

import com.orangeschool.orangeschoolapiserver.common.enums.ScheduleType;
import com.orangeschool.orangeschoolapiserver.domain.schedule.dto.ScheduleDto;
import com.orangeschool.orangeschoolapiserver.domain.schedule.entity.Schedule;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.orangeschool.orangeschoolapiserver.domain.schedule.entity.QSchedule.schedule;


@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ScheduleDto> searchWeekSchedules(Long commonMemberId, LocalDate weekStartDate) {

        List<Schedule> schedules = queryFactory
                .select(schedule)
                .from(schedule)
                .where(
                        schedule.commonMember.id.eq(commonMemberId),
                        schedule.startDate.between(weekStartDate, weekStartDate.plusDays(6)).or(schedule.endDate.between(weekStartDate, weekStartDate.plusDays(6)))
                )
                .orderBy(schedule.startTime.asc())
                .fetch();

        return schedules.stream().map(ScheduleDto::create).collect(Collectors.toList());
    }

    @Override
    public Page<ScheduleDto> searchByCommonMemberIdAndAcademy(Pageable pageable, Long commonMemberId) {

        List<Schedule> schedules = queryFactory
                .select(schedule)
                .from(schedule)
                .where(
                        schedule.commonMember.id.eq(commonMemberId),
                        schedule.scheduleType.eq(ScheduleType.ACADEMY)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<Schedule> countQuery = queryFactory
                .select(schedule)
                .from(schedule)
                .where(
                        schedule.commonMember.id.eq(commonMemberId),
                        schedule.scheduleType.eq(ScheduleType.ACADEMY)
                );

        return PageableExecutionUtils.getPage(schedules.stream().map(
                ScheduleDto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Schedule.class, "schedule");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }
}
