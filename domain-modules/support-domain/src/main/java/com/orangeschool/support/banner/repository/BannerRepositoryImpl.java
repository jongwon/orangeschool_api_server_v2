package com.orangeschool.support.banner.repository;

import com.orangeschool.support.banner.entity.Banner;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.orangeschool.support.banner.entity.QBanner.banner;

@Repository
@RequiredArgsConstructor
public class BannerRepositoryImpl implements BannerRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Banner> searchByLocationCode(Long locationCode) {

        List<Banner> banners = queryFactory
                .select(banner)
                .from(banner)
                .where(
                        banner.location.code.eq(locationCode)
                )
                .orderBy(banner.id.desc())
                .fetch();

        return banners;
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Banner.class, "banner");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }
}
