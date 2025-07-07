package com.orangeschool.community.follow.repository;

import com.orangeschool.member.api.dto.MemberInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FollowRepositoryCustom {
    Page<MemberInfo> searchFollowers(Long memberId, Pageable pageable);
    Page<MemberInfo> searchFollowing(Long memberId, Pageable pageable);
}
