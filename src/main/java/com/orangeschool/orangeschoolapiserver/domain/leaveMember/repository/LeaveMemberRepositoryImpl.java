package com.orangeschool.orangeschoolapiserver.domain.leaveMember.repository;

import com.orangeschool.orangeschoolapiserver.common.dto.request.KeywordSearchDto;
import com.orangeschool.orangeschoolapiserver.domain.leaveMember.dto.LeaveMemberDto;
import com.orangeschool.orangeschoolapiserver.domain.leaveMember.entity.LeaveMember;
import com.orangeschool.orangeschoolapiserver.domain.leaveMember.repository.LeaveMemberRepositoryCustom;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.orangeschool.orangeschoolapiserver.domain.leaveMember.entity.QLeaveMember.leaveMember;

@Repository
@RequiredArgsConstructor
public class LeaveMemberRepositoryImpl implements LeaveMemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<LeaveMemberDto> search(Pageable pageable, KeywordSearchDto keywordSearchDto) {

        List<LeaveMember> leaveMembers = queryFactory
                .select(leaveMember)
                .from(leaveMember)
                .where(
                        keywordContains(keywordSearchDto.getKeyword()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        long totalSize = queryFactory
                .select(leaveMember)
                .from(leaveMember)
                .where(
                        keywordContains(keywordSearchDto.getKeyword()))
                .fetch()
                .size();

        return new PageImpl<LeaveMemberDto>(leaveMembers.stream().map(
                LeaveMemberDto::create).collect(Collectors.toList()), pageable, totalSize);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(LeaveMember.class, "leaveMember");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }

    private BooleanExpression keywordContains(String keyword) {

        if (keyword == null) {
            keyword = "";
        }

        return leaveMember.email.contains(keyword).or(leaveMember.name.contains(keyword));
    }
}
