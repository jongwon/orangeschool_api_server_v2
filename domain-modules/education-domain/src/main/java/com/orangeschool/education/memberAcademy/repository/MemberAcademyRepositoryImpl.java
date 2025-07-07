package com.orangeschool.education.memberAcademy.repository;

import com.orangeschool.education.academy.dto.AcademyDto;
import com.orangeschool.member.commonMember.dto.CommonMemberDto;
import com.orangeschool.education.memberAcademy.entity.MemberAcademy;
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

import static com.orangeschool.orangeschoolapiserver.domain.memberAcademy.entity.QMemberAcademy.memberAcademy;


@Repository
@RequiredArgsConstructor
public class MemberAcademyRepositoryImpl implements MemberAcademyRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CommonMemberDto> searchByAcademy(Pageable pageable, Long academyId) {

        List<MemberAcademy> memberAcademys = queryFactory
                .select(memberAcademy)
                .from(memberAcademy)
                .where(
                        academyFilter(academyId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<MemberAcademy> countQuery = queryFactory
                .select(memberAcademy)
                .from(memberAcademy)
                .where(
                        academyFilter(academyId)
                );

        return PageableExecutionUtils.getPage(memberAcademys.stream().map(memberAcademy ->
                CommonMemberDto.create(memberAcademy.getCommonMember())).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    @Override
    public Page<AcademyDto> searchByCommonMember(Pageable pageable, Long commonMemberId) {

        List<MemberAcademy> memberAcademys = queryFactory
                .select(memberAcademy)
                .from(memberAcademy)
                .where(
                        commonMemberFilter(commonMemberId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<MemberAcademy> countQuery = queryFactory
                .select(memberAcademy)
                .from(memberAcademy)
                .where(
                        commonMemberFilter(commonMemberId)
                );

        return PageableExecutionUtils.getPage(memberAcademys.stream().map(memberAcademy ->
                AcademyDto.create(memberAcademy.getAcademy())).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(MemberAcademy.class, "memberAcademy");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }
    private BooleanExpression commonMemberFilter(Long commonMemberId) {

        if (commonMemberId == 0L) {
            return null;
        }

        return memberAcademy.commonMember.id.eq(commonMemberId);
    }

    private BooleanExpression academyFilter(Long academyId) {

        if (academyId == 0L) {
            return null;
        }

        return memberAcademy.academy.id.eq(academyId);
    }
}
