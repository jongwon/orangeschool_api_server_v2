package com.orangeschool.orangeschoolapiserver.domain.terms.repository;

import com.orangeschool.orangeschoolapiserver.domain.manager.dto.ManagerDto;
import com.orangeschool.orangeschoolapiserver.domain.terms.dto.TermsDto;
import com.orangeschool.orangeschoolapiserver.domain.terms.entity.Terms;
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

import static com.orangeschool.orangeschoolapiserver.domain.terms.entity.QTerms.terms;

@Repository
@RequiredArgsConstructor
public class TermsRepositoryImpl implements TermsRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<TermsDto> search(Pageable pageable) {

        List<Terms> termsList = queryFactory
                .select(terms)
                .from(terms)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        long totalSize = queryFactory
                .select(terms)
                .from(terms)
                .fetch()
                .size();
        ;

        return new PageImpl<TermsDto>(termsList.stream().map(
                TermsDto::create).collect(Collectors.toList()), pageable, totalSize);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Terms.class, "terms");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }
}
