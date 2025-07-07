package com.orangeschool.orangeschoolapiserver.domain.commonMember.repository;

import com.orangeschool.orangeschoolapiserver.common.enums.MemberFilter;
import com.orangeschool.orangeschoolapiserver.common.enums.MemberType;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.CommonMemberDto;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.CommonMemberFilterDto;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.CommonMemberProfileDto;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.TownFriendFilterDto;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.QCommonMember;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
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

import static com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.QCommonMember.commonMember;

@Repository
@RequiredArgsConstructor
public class CommonMemberRepositoryImpl implements CommonMemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CommonMemberDto> search(Pageable pageable, CommonMemberFilterDto commonMemberFilter) {

        List<CommonMember> commonMembers = queryFactory
                .select(commonMember)
                .from(commonMember)
                .where(
                        keywordContains(commonMemberFilter),
                        memberTypeFilter(commonMemberFilter.getMemberType()),
                        parentIdFilter(commonMemberFilter.getParentId())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<CommonMember> countQuery = queryFactory
                .select(commonMember)
                .from(commonMember)
                .where(
                        keywordContains(commonMemberFilter),
                        memberTypeFilter(commonMemberFilter.getMemberType()),
                        parentIdFilter(commonMemberFilter.getParentId())
                );

        return PageableExecutionUtils.getPage(commonMembers.stream().map(
                CommonMemberDto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    @Override
    public Page<CommonMemberProfileDto> search(Long followingMemberId, Pageable pageable, TownFriendFilterDto townFriendFilterDto) {

        List<CommonMember> commonMembers = queryFactory
                .select(commonMember)
                .from(commonMember)
                .where(
                        keywordContains(townFriendFilterDto.getKeyword()),
                        commonMember.parentId.ne(0L),
                        commonMember.id.ne(followingMemberId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<CommonMember> countQuery = queryFactory
                .select(commonMember)
                .from(commonMember)
                .where(
                        keywordContains(townFriendFilterDto.getKeyword()),
                        commonMember.parentId.ne(0L),
                        commonMember.id.ne(followingMemberId)
                );

        return PageableExecutionUtils.getPage(commonMembers.stream().map(
                commonMember -> CommonMemberProfileDto.create(followingMemberId, commonMember)).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    @Override
    public Page<CommonMemberDto> searchByParentId(Long commonMemberId, Pageable pageable) {

        List<CommonMember> commonMembers = queryFactory
                .select(commonMember)
                .from(commonMember)
                .where(
                        commonMember.parentId.eq(commonMemberId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .fetch();

        JPAQuery<CommonMember> countQuery = queryFactory
                .select(commonMember)
                .from(commonMember)
                .where(
                        commonMember.parentId.eq(commonMemberId)
                );

        return PageableExecutionUtils.getPage(commonMembers.stream().map(
                CommonMemberDto::create).collect(Collectors.toList()), pageable, countQuery::fetchCount);
    }

    @Override
    public List<String> findServicePushTokenByMemberType(MemberType memberType) {

        List<String> pushTokenList = queryFactory
                .select(commonMember.pushToken)
                .from(commonMember)
                .where(
                        commonMember.pushToken.isNotEmpty(),
                        commonMember.pushToken.isNotNull(),
                        commonMember.agreeToService.isTrue(),
                        //commonMember.memberType.eq(memberType)
                        memberTypeFilter(memberType)
                )
                .fetch();

        return pushTokenList;
    }

    @Override
    public List<String> findAdPushTokenByMemberType(MemberType memberType) {

        List<String> pushTokenList = queryFactory
                .select(commonMember.pushToken)
                .from(commonMember)
                .where(
                        commonMember.pushToken.isNotEmpty(),
                        commonMember.pushToken.isNotNull(),
                        commonMember.agreeToAd.isTrue(),
                        //commonMember.memberType.eq(memberType)
                        memberTypeFilter(memberType)
                )
                .fetch();

        return pushTokenList;
    }

    @Override
    public List<CommonMember> findByParentIdOrReferralCode(Long commonMemberId, String referralCode) {

        QCommonMember parentCommonMember = new QCommonMember("parentCommonMember");

        List<CommonMember> commonMembers = queryFactory
                .selectFrom(commonMember)
                .where(
                        commonMember.parentId.eq(commonMemberId)
                                .or(
                                        JPAExpressions
                                                .select(parentCommonMember.myReferralCode)
                                                .from(parentCommonMember)
                                                .where(parentCommonMember.id.eq(commonMember.parentId))
                                                .eq(referralCode)
                                )
                )
                .fetch();

        return commonMembers;
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(CommonMember.class, "commonMember");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        return orders;
    }

    private BooleanExpression keywordContains(CommonMemberFilterDto commonMemberFilter) {

        String keyword = commonMemberFilter.getKeyword();
        MemberFilter memberFilter = commonMemberFilter.getMemberFilter();

        if (keyword == null || keyword.isEmpty()) {
            keyword = "";
        }

        switch (memberFilter) {
            case NAME:
                return commonMember.name.contains(keyword);
            case EMAIL:
                return commonMember.email.contains(keyword);
            case GENDER:
                return commonMember.genderTitle.contains(keyword);
            case BIRTH:
                return commonMember.birth.contains(keyword);
            case PHONE_NUMBER:
                return commonMember.phoneNumber.contains(keyword);
            case ADDRESS:
                return commonMember.address.contains(keyword).or(commonMember.addressDetail.contains(keyword));
            case PARENT:
                return commonMember.parentName.contains(keyword);
        }

        // default
        return commonMember.name.contains(keyword)
                .or(commonMember.email.contains(keyword))
                .or(commonMember.genderTitle.contains(keyword))
                .or(commonMember.birth.contains(keyword))
                .or(commonMember.phoneNumber.contains(keyword))
                .or(commonMember.address.contains(keyword))
                .or(commonMember.addressDetail.contains(keyword));
    }

    private BooleanExpression keywordContains(String keyword) {

        if (keyword == null || keyword.isEmpty()) {
            keyword = "";
        }

        return commonMember.name.contains(keyword)
                .or(Expressions.stringTemplate("replace({0}, ' ', '')", commonMember.nickName).contains(keyword.replaceAll(" ", "")))
                .or(commonMember.email.contains(keyword));
    }

    private BooleanExpression memberTypeFilter(MemberType memberType) {

        if (memberType == null || memberType == MemberType.NONE) {
            return null;
        }

        return commonMember.memberType.eq(memberType);
    }

    private BooleanExpression parentIdFilter(Long parentId) {

        if (parentId == null || parentId == 0L) {
            return null;
        }

        return commonMember.parentId.eq(parentId);
    }

}
