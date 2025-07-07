package com.orangeschool.member.commonMember.service;

import com.orangeschool.member.api.dto.MemberInfo;
import com.orangeschool.member.api.service.MemberInfoProvider;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.member.commonMember.repository.CommonMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * MemberInfoProvider 구현체
 * 다른 도메인에 회원 정보를 제공
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberInfoProviderImpl implements MemberInfoProvider {
    
    private final CommonMemberRepository commonMemberRepository;
    
    @Override
    public Optional<MemberInfo> getMemberInfo(Long memberId) {
        return commonMemberRepository.findById(memberId)
                .map(this::convertToMemberInfo);
    }
    
    @Override
    public boolean existsMember(Long memberId) {
        return commonMemberRepository.existsById(memberId);
    }
    
    @Override
    public List<MemberInfo> getMembersByIds(List<Long> memberIds) {
        return commonMemberRepository.findAllById(memberIds).stream()
                .map(this::convertToMemberInfo)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<MemberInfo> getMemberByEmail(String email) {
        return commonMemberRepository.findByEmail(email)
                .filter(member -> member.getIsActive())
                .map(this::convertToMemberInfo);
    }
    
    @Override
    public boolean isActiveMember(Long memberId) {
        return commonMemberRepository.findById(memberId)
                .map(member -> member.getIsActive())
                .orElse(false);
    }
    
    private MemberInfo convertToMemberInfo(CommonMember member) {
        return MemberInfo.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .nickname(member.getNickName())
                .memberType(member.getMemberType())
                .profileImage(member.getFileUrl())
                .isActive(member.getIsActive())
                .createdAt(member.getCreatedAt())
                .schoolName(member.getSchoolName())
                .grade(member.getGrade())
                .parentId(member.getParentId())
                .phoneNumber(member.getPhoneNumber())
                .build();
    }
}