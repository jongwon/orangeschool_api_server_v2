package com.orangeschool.member.commonMember.repository;

import com.orangeschool.common.enums.MemberType;
import com.orangeschool.member.commonMember.dto.CommonMemberDto;
import com.orangeschool.member.commonMember.dto.CommonMemberFilterDto;
import com.orangeschool.member.commonMember.dto.CommonMemberProfileDto;
import com.orangeschool.member.commonMember.dto.TownFriendFilterDto;
import com.orangeschool.member.commonMember.entity.CommonMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommonMemberRepositoryCustom {
    Page<CommonMemberDto> search(Pageable pageable, CommonMemberFilterDto commonMemberFilter);
    Page<CommonMemberProfileDto> search(Long followingMemberId,Pageable pageable, TownFriendFilterDto townFriendFilterDto);

    Page<CommonMemberDto> searchByParentId(Long commonMemberId, Pageable pageable);

    List<String> findServicePushTokenByMemberType(MemberType memberType);
    List<String> findAdPushTokenByMemberType(MemberType memberType);

    List<CommonMember> findByParentIdOrReferralCode(Long commonMemberId, String referralCode);
}
