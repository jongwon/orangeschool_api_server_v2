package com.orangeschool.orangeschoolapiserver.domain.banner.repository;

import com.orangeschool.orangeschoolapiserver.common.dto.request.KeywordSearchDto;
import com.orangeschool.orangeschoolapiserver.domain.banner.dto.BannerV2Dto;
import com.orangeschool.orangeschoolapiserver.domain.banner.entity.BannerV2;
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

import static com.orangeschool.orangeschoolapiserver.domain.banner.entity.QBannerV2.bannerV2;

@Repository
@RequiredArgsConstructor
public class BannerV2RepositoryImpl implements BannerV2RepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BannerV2Dto> search(Pageable pageable, KeywordSearchDto keywordSearchDto) {

        List<BannerV2> bannerV2s = queryFactory
                .select(bannerV2)
                .from(bannerV2)
                .where(
                        keywordContains(keywordSearchDto.getKeyword())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<BannerV2> countQuery = queryFactory
                .select(bannerV2)
                .from(bannerV2)
                .where(
                        keywordContains(keywordSearchDto.getKeyword())
                );

        return PageableExecutionUtils.getPage(bannerV2s.stream().map(
                BannerV2Dto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    @Override
    public List<BannerV2Dto> searchToUser(Long locationCode) {

        String locationCodeStr = "#" + locationCode;

        List<BannerV2> bannerV2s = queryFactory
                .select(bannerV2)
                .from(bannerV2)
                .where(
                        bannerV2.isActive.isTrue(),
                        bannerV2.regionCodeTag.contains(locationCodeStr)
                )
                .orderBy(bannerV2.id.desc())
                .fetch();

        return bannerV2s.stream().map(
                BannerV2Dto::create).collect(Collectors.toList());
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(BannerV2.class, "bannerV2");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }

    private BooleanExpression keywordContains(String keyword) {

        if (keyword == null) {
            keyword = "";
        }

        return bannerV2.title.contains(keyword);
    }
}
