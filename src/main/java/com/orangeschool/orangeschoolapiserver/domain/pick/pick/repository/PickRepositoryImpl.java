package com.orangeschool.orangeschoolapiserver.domain.pick.pick.repository;

import com.orangeschool.orangeschoolapiserver.common.enums.PickType;
import com.orangeschool.orangeschoolapiserver.domain.pick.pick.dto.PickDto;
import com.orangeschool.orangeschoolapiserver.domain.pick.pick.dto.PickFilterDto;
import com.orangeschool.orangeschoolapiserver.domain.pick.pick.entity.Pick;
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

import static com.orangeschool.orangeschoolapiserver.domain.pick.pick.entity.QPick.pick;


@Repository
@RequiredArgsConstructor
public class PickRepositoryImpl implements PickRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PickDto> search(Pageable pageable, PickFilterDto pickFilterDto) {

        List<Pick> picks = queryFactory
                .select(pick)
                .from(pick)
                .where(
                        keywordContains(pickFilterDto.getKeyword()),
                        pickTypeFilter(pickFilterDto.getPickType())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                //.orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .orderBy(pick.number.desc())
                .fetch();

        JPAQuery<Pick> countQuery = queryFactory
                .select(pick)
                .from(pick)
                .where(
                        keywordContains(pickFilterDto.getKeyword()),
                        pickTypeFilter(pickFilterDto.getPickType())
                );

        return PageableExecutionUtils.getPage(picks.stream().map(
                PickDto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    @Override
    public Page<PickDto> searchToUser(Pageable pageable, PickFilterDto pickFilterDto, Long commonMemberId) {

        List<Pick> picks = queryFactory
                .select(pick)
                .from(pick)
                .where(
                        keywordContains(pickFilterDto.getKeyword()),
                        pickTypeFilterToAll(pickFilterDto.getPickType()),
                        pick.isActive.isTrue()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                //.orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .orderBy(pick.number.desc())
                .fetch();

        JPAQuery<Pick> countQuery = queryFactory
                .select(pick)
                .from(pick)
                .where(
                        keywordContains(pickFilterDto.getKeyword()),
                        pickTypeFilterToAll(pickFilterDto.getPickType()),
                        pick.isActive.isTrue()
                );

        return PageableExecutionUtils.getPage(
                picks.stream()
                        .map(pick -> PickDto.create(pick, commonMemberId))
                        .collect(Collectors.toList()),
                pageable,
                countQuery::fetchCount);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Pick.class, "pick");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }

    private BooleanExpression keywordContains(String keyword) {

        if (keyword == null) {
            keyword = "";
        }

        return pick.title.contains(keyword);
    }

    private BooleanExpression pickTypeFilter(PickType pickType) {

        if (pickType == null || pickType == PickType.NONE) {
            return null;
        }

        return pick.pickType.eq(pickType);
    }

    private BooleanExpression pickTypeFilterToAll(PickType pickType) {

        if (pickType == null || pickType == PickType.NONE) {
            return null;
        }

        return pick.pickType.eq(pickType).or(pick.pickType.eq(PickType.ALL));
    }
}
