package com.orangeschool.education.schoolSchedule.repository;

import com.orangeschool.education.schoolSchedule.dto.SchoolScheduleDto;
import com.orangeschool.education.schoolSchedule.entity.SchoolSchedule;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.orangeschool.orangeschoolapiserver.domain.schoolSchedule.entity.QSchoolSchedule.schoolSchedule;

@Repository
@RequiredArgsConstructor
public class SchoolScheduleRepositoryImpl implements SchoolScheduleRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<SchoolScheduleDto> search(Pageable pageable) {

        List<SchoolSchedule> schoolScheduleList = queryFactory
                .select(schoolSchedule)
                .from(schoolSchedule)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        long totalSize = queryFactory
                .select(schoolSchedule)
                .from(schoolSchedule)
                .fetch()
                .size();
        ;

        return new PageImpl<SchoolScheduleDto>(schoolScheduleList.stream().map(
                SchoolScheduleDto::create).collect(Collectors.toList()), pageable, totalSize);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(SchoolSchedule.class, "schoolSchedule");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }
}
