package com.orangeschool.community.follow.repository;

import com.orangeschool.member.api.dto.MemberInfo;
import com.orangeschool.member.api.service.MemberInfoProvider;
import com.orangeschool.community.follow.entity.Follow;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.orangeschool.community.follow.entity.QFollow.follow;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final MemberInfoProvider memberInfoProvider;

    @Override
    public Page<MemberInfo> searchFollowers(Long memberId, Pageable pageable) {
        List<Follow> follows = queryFactory
            .selectFrom(follow)
            .where(follow.followerMemberId.eq(memberId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory
            .select(follow.count())
            .from(follow)
            .where(follow.followerMemberId.eq(memberId))
            .fetchOne();

        List<MemberInfo> memberInfos = follows.stream()
            .map(f -> memberInfoProvider.getMemberInfo(f.getFollowingMemberId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());

        return new PageImpl<>(memberInfos, pageable, total != null ? total : 0L);
    }

    @Override
    public Page<MemberInfo> searchFollowing(Long memberId, Pageable pageable) {
        List<Follow> follows = queryFactory
            .selectFrom(follow)
            .where(follow.followingMemberId.eq(memberId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory
            .select(follow.count())
            .from(follow)
            .where(follow.followingMemberId.eq(memberId))
            .fetchOne();

        List<MemberInfo> memberInfos = follows.stream()
            .map(f -> memberInfoProvider.getMemberInfo(f.getFollowerMemberId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());

        return new PageImpl<>(memberInfos, pageable, total != null ? total : 0L);
    }
}
