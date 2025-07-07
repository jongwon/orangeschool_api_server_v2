package com.orangeschool.community.follow.repository;

import com.orangeschool.member.commonMember.dto.CommonMemberProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FollowRepositoryCustom {

    Page<CommonMemberProfileDto> search(Long followingMemberId, Pageable pageable);
}
