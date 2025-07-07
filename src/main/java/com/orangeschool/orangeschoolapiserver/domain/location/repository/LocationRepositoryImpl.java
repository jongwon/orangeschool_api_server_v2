package com.orangeschool.orangeschoolapiserver.domain.location.repository;

import com.orangeschool.orangeschoolapiserver.domain.location.dto.LocationDto;
import com.orangeschool.orangeschoolapiserver.domain.location.entity.Location;
import com.orangeschool.orangeschoolapiserver.domain.manager.dto.ManagerDto;
import com.orangeschool.orangeschoolapiserver.common.dto.request.KeywordSearchDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import static com.orangeschool.orangeschoolapiserver.domain.location.entity.QLocation.location;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<LocationDto> search(Pageable pageable, KeywordSearchDto keywordSearchDto) {

        List<Location> locationList = queryFactory
                .select(location)
                .from(location)
                .where(
                        keywordContains(keywordSearchDto.getKeyword()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        long totalSize = queryFactory
                .select(location)
                .from(location)
                .where(
                        keywordContains(keywordSearchDto.getKeyword()))
                .fetch()
                .size();
        ;

        return new PageImpl<LocationDto>(locationList.stream().map(
                LocationDto::create).collect(Collectors.toList()), pageable, totalSize);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Location.class, "location");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }

    private BooleanExpression keywordContains(String keyword) {

        if (keyword == null) {
            keyword = "";
        }

        return location.title.contains(keyword);
    }
}
