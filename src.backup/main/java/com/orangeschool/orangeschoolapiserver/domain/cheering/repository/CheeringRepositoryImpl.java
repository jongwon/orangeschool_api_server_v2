package com.orangeschool.orangeschoolapiserver.domain.cheering.repository;

import com.orangeschool.orangeschoolapiserver.domain.cheering.dto.CheeringDto;
import com.orangeschool.orangeschoolapiserver.domain.cheering.entity.Cheering;
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

import static com.orangeschool.orangeschoolapiserver.domain.cheering.entity.QCheering.cheering;

@Repository
@RequiredArgsConstructor
public class CheeringRepositoryImpl implements CheeringRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CheeringDto> search(Long cheeredMemberId, Pageable pageable) {

        List<Cheering> cheerings = queryFactory
                .select(cheering)
                .from(cheering)
                .where(
                        cheering.cheeredMember.id.eq(cheeredMemberId)

                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        long totalSize = queryFactory
                .select(cheering)
                .from(cheering)
                .where(
                        cheering.cheeredMember.id.eq(cheeredMemberId)
                )
                .fetch()
                .size();

        return new PageImpl<CheeringDto>(cheerings.stream().map(
                CheeringDto::create).collect(Collectors.toList()), pageable, totalSize);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Cheering.class, "cheering");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }
}
