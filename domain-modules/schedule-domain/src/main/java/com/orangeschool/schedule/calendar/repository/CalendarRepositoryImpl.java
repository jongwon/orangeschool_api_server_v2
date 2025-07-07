package com.orangeschool.schedule.calendar.repository;

import com.orangeschool.common.enums.ScheduleType;
import com.orangeschool.schedule.calendar.dto.CalendarDto;
import com.orangeschool.schedule.calendar.dto.CalendarSearchDto;
import com.orangeschool.schedule.calendar.dto.GraphDto;
import com.orangeschool.schedule.calendar.entity.Calendar;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.orangeschool.schedule.calendar.entity.QCalendar.calendar;
import static com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.QCommonMember.commonMember;
import static com.orangeschool.orangeschoolapiserver.domain.schedule.entity.QSchedule.schedule;


@Repository
@RequiredArgsConstructor
public class CalendarRepositoryImpl implements CalendarRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CalendarDto> searchMonth(Long commonMemberId, CalendarSearchDto calendarSearchDto) {

        StringExpression startDateFormatQ = Expressions.stringTemplate("DATE_FORMAT({0}, {1})", calendar.startDate, ConstantImpl.create("%y-%m"));
        StringExpression endDateFormatQ = Expressions.stringTemplate("DATE_FORMAT({0}, {1})", calendar.endDate, ConstantImpl.create("%y-%m"));
        StringExpression monthStartDateFormat = Expressions.stringTemplate("DATE_FORMAT({0}, {1})", calendarSearchDto.getMonthStartDate(), ConstantImpl.create("%y-%m"));

        List<Calendar> calendars = queryFactory
                .select(calendar)
                .from(calendar)
                .where(
                        calendar.schedule.commonMember.id.in(calendarSearchDto.getCommonMemberIdList()),
                        startDateFormatQ.eq(monthStartDateFormat).or(endDateFormatQ.eq(monthStartDateFormat)),
                        calendarSearchDto.getPayOnly()
                                ? calendar.scheduleType.eq(ScheduleType.PAY)
                                : calendar.scheduleType.eq(ScheduleType.SCHEDULE).or(calendar.scheduleType.eq(ScheduleType.PAY)).or(calendar.scheduleType.eq(ScheduleType.HOSPITAL))
                )
                .orderBy(calendar.startDate.asc(), calendar.startTime.asc())
                .fetch();

        return calendars.stream().map(CalendarDto::create).collect(Collectors.toList());
    }

    @Override
    public List<CalendarDto> searchWeek(Long commonMemberId, CalendarSearchDto calendarSearchDto) {
        LocalDate weekStartDate = calendarSearchDto.getWeekStartDate();
        LocalDate weekEndDate = calendarSearchDto.getWeekStartDate().plusDays(6);
        //LocalTime time6AM = LocalTime.of(6, 0, 0);
        List<Calendar> calendars = queryFactory
                .select(calendar)
                .from(calendar)
                .where(
                        calendar.schedule.commonMember.id.eq(calendarSearchDto.getCommonMemberId()),
                        calendar.startDate.between(weekStartDate, weekEndDate),
                        calendar.isSingle.isTrue()
                        //calendar.endTime.goe(time6AM)
                )
                .orderBy(calendar.startDate.asc(), calendar.startTime.asc())
                .fetch();

        return calendars.stream().map(CalendarDto::create).collect(Collectors.toList());
    }

    @Override
    public List<CalendarDto> searchWeekByIsAllDay(Long commonMemberId, CalendarSearchDto calendarSearchDto) {
        LocalDate weekStartDate = calendarSearchDto.getWeekStartDate();
        LocalDate weekEndDate = calendarSearchDto.getWeekStartDate().plusDays(6);

        List<Calendar> calendars = queryFactory
                .select(calendar)
                .from(calendar)
                .where(
                        calendar.schedule.commonMember.id.eq(calendarSearchDto.getCommonMemberId()),
                        calendar.startDate.between(weekStartDate, weekEndDate),
                        calendar.isAllDay.isTrue()
                )
                .orderBy(calendar.startDate.asc(), calendar.startTime.asc())
                .fetch();

        return calendars.stream().map(CalendarDto::create).collect(Collectors.toList());
    }

    @Override
    public List<CalendarDto> searchDay(Long commonMemberId, CalendarSearchDto calendarSearchDto) {

        StringExpression startDateFormatQ = Expressions.stringTemplate("DATE_FORMAT({0}, {1})", calendar.startDate, ConstantImpl.create("%y-%m-%d"));
        StringExpression endDateFormatQ = Expressions.stringTemplate("DATE_FORMAT({0}, {1})", calendar.endDate, ConstantImpl.create("%y-%m-%d"));
        StringExpression dayDateFormat = Expressions.stringTemplate("DATE_FORMAT({0}, {1})", calendarSearchDto.getDayDate(), ConstantImpl.create("%y-%m-%d"));

        List<Calendar> calendars = queryFactory
                .select(calendar)
                .from(calendar)
                .where(
                        calendar.schedule.commonMember.id.in(calendarSearchDto.getCommonMemberIdList()),
                        startDateFormatQ.eq(dayDateFormat).or(endDateFormatQ.eq(dayDateFormat)),
                        calendarSearchDto.getPayOnly()
                                ? calendar.scheduleType.eq(ScheduleType.PAY)
                                : null
                )
                .orderBy(calendar.startDate.asc(), calendar.startTime.asc())
                .fetch();

        return calendars.stream().map(CalendarDto::create).collect(Collectors.toList());
    }

    @Override
    public List<CalendarDto> searchTimeTable(CalendarSearchDto calendarSearchDto) {

        StringExpression startDateFormatQ = Expressions.stringTemplate("DATE_FORMAT({0}, {1})", calendar.startDate, ConstantImpl.create("%y-%m-%d"));
        StringExpression endDateFormatQ = Expressions.stringTemplate("DATE_FORMAT({0}, {1})", calendar.endDate, ConstantImpl.create("%y-%m-%d"));
        StringExpression dayDateFormat = Expressions.stringTemplate("DATE_FORMAT({0}, {1})", calendarSearchDto.getDayDate(), ConstantImpl.create("%y-%m-%d"));

        List<Calendar> calendars = queryFactory
                .select(calendar)
                .from(calendar)
                .where(
                        calendar.schedule.commonMember.id.eq(calendarSearchDto.getCommonMemberId()),
                        startDateFormatQ.eq(dayDateFormat).or(endDateFormatQ.eq(dayDateFormat)),
                        calendarSearchDto.getPayOnly()
                                ? calendar.scheduleType.eq(ScheduleType.PAY)
                                : null
                )
                .orderBy(calendar.startDate.asc(), calendar.startTime.asc())
                .fetch();

        return calendars.stream().map(CalendarDto::create).collect(Collectors.toList());
    }

    @Override
    public List<CalendarDto> searchPayment(List<Long> childList, CalendarSearchDto calendarSearchDto) {

        StringExpression startDateFormatQ = Expressions.stringTemplate("DATE_FORMAT({0}, {1})", calendar.startDate, ConstantImpl.create("%y-%m"));
        StringExpression endDateFormatQ = Expressions.stringTemplate("DATE_FORMAT({0}, {1})", calendar.endDate, ConstantImpl.create("%y-%m"));
        StringExpression monthStartDateFormat = Expressions.stringTemplate("DATE_FORMAT({0}, {1})", calendarSearchDto.getMonthStartDate(), ConstantImpl.create("%y-%m"));

        List<Calendar> calendars = queryFactory
                .select(calendar)
                .from(calendar)
                .where(
                        calendar.schedule.commonMember.id.in(childList),
                        startDateFormatQ.eq(monthStartDateFormat).or(endDateFormatQ.eq(monthStartDateFormat)),
                        calendar.scheduleType.eq(ScheduleType.PAY),
                        calendar.amount.ne(0L)
                )
                .orderBy(calendar.startDate.asc(), calendar.startTime.asc())
                .fetch();

        return calendars.stream().map(CalendarDto::create).collect(Collectors.toList());
    }

    @Override
    public List<GraphDto> searchGraphInfo(List<Long> childList, CalendarSearchDto calendarSearchDto) {
        LocalDate currentDate = calendarSearchDto.getDayDate();
        LocalDate fiveMonthsAgo = currentDate.minusMonths(5);
        LocalDate sixMonthsLater = currentDate.plusMonths(6);

        List<GraphDto> result = queryFactory
                .select(
                        Projections.bean(GraphDto.class,
                                calendar.startDate.year().as("year"),
                                calendar.startDate.month().as("month"),
                                commonMember.name.as("name"),
                                calendar.amount.sum().as("amount"))
                )
                .from(calendar)
                .leftJoin(calendar.schedule, schedule)
                .leftJoin(schedule.commonMember, commonMember)
                .where(
                        calendar.schedule.commonMember.id.in(childList),
                        calendar.startDate.between(fiveMonthsAgo, sixMonthsLater)
                )
                .groupBy(commonMember.name, calendar.startDate.month())
                .orderBy(calendar.startDate.month().asc())
                .fetch();

        return result;
    }

//    @Override
//    public void deleteByScheduleIdAndStandardDateAfter(Long scheduleId, LocalDate updateStandardDate) {
//
//        queryFactory.delete(calendar)
//                .where(calendar.schedule.id.eq(scheduleId).and(calendar.startDate.goe(updateStandardDate)))
//                .execute();
//    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Calendar.class, "calendar");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }
}
