package com.orangeschool.member.leaveMember.repository;

import com.orangeschool.common.enums.MemberType;
import com.orangeschool.member.leaveMember.entity.LeaveMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaveMemberRepository extends JpaRepository<LeaveMember, Long>, LeaveMemberRepositoryCustom {

    Long countByMemberType(MemberType memberType);

    // save 메소드는 JpaRepository에서 상속받음

    void deleteById(Long leaveMemberId);

    Optional<LeaveMember> findById(Long leaveMemberId);

}
