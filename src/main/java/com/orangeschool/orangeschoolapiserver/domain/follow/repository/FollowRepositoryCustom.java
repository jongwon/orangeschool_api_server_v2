package com.orangeschool.orangeschoolapiserver.domain.follow.repository;

import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.CommonMemberProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FollowRepositoryCustom {

    Page<CommonMemberProfileDto> search(Long followingMemberId, Pageable pageable);
}
