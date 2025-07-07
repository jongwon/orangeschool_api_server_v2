package com.orangeschool.support.popup.repository;

import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.support.popup.dto.PopupDto;
import com.orangeschool.support.popup.entity.Popup;
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

import static com.orangeschool.support.popup.entity.QPopup.popup;

@Repository
@RequiredArgsConstructor
public class PopupRepositoryImpl implements PopupRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PopupDto> search(Pageable pageable, KeywordSearchDto keywordSearchDto) {

        List<Popup> popups = queryFactory
                .select(popup)
                .from(popup)
                .where(
                        keywordContains(keywordSearchDto.getKeyword())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<Popup> countQuery = queryFactory
                .select(popup)
                .from(popup)
                .where(
                        keywordContains(keywordSearchDto.getKeyword())
                );

        return PageableExecutionUtils.getPage(popups.stream().map(
                PopupDto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    @Override
    public List<PopupDto> searchToUser() {

        List<Popup> popups = queryFactory
                .select(popup)
                .from(popup)
                .where(
                        popup.isActive.isTrue()
                )
                .orderBy(popup.id.desc())
                .fetch();

        return popups.stream().map(
                PopupDto::create).collect(Collectors.toList());
    }


    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Popup.class, "popup");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }
    private BooleanExpression keywordContains(String keyword) {

        if (keyword == null) {
            keyword = "";
        }

        return popup.title.contains(keyword);
    }

}
