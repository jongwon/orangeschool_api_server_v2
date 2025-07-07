package com.orangeschool.orangeschoolapiserver.domain.leaveMember.repository;

import com.orangeschool.orangeschoolapiserver.common.enums.MemberType;
import com.orangeschool.orangeschoolapiserver.domain.leaveMember.entity.LeaveMember;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LeaveMemberRepository extends PagingAndSortingRepository<LeaveMember, Long>, LeaveMemberRepositoryCustom {

    Long countByMemberType(MemberType memberType);
}
