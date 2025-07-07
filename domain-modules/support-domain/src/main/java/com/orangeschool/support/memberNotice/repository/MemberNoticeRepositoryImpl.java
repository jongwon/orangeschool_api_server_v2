package com.orangeschool.support.memberNotice.repository;

import com.orangeschool.support.memberNotice.dto.MemberNoticeDto;
import com.orangeschool.support.memberNotice.entity.MemberNotice;
import com.orangeschool.notice.entity.Notice;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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

import static com.orangeschool.support.memberNotice.entity.QMemberNotice.memberNotice;


@Repository
@RequiredArgsConstructor
public class MemberNoticeRepositoryImpl implements MemberNoticeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MemberNoticeDto> search(Pageable pageable, Long commonMemberId) {

        List<MemberNotice> notices = queryFactory
                .select(memberNotice)
                .from(memberNotice)
                .where(
                        memberNotice.commonMember.id.eq(commonMemberId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<MemberNotice> countQuery = queryFactory
                .select(memberNotice)
                .from(memberNotice)
                .where(
                        memberNotice.commonMember.id.eq(commonMemberId)
                );

        return PageableExecutionUtils.getPage(notices.stream().map(
                MemberNoticeDto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Notice.class, "memberNotice");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }
}
