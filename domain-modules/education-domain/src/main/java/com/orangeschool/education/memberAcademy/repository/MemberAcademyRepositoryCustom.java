package com.orangeschool.education.memberAcademy.repository;

import com.orangeschool.education.academy.dto.AcademyDto;
import com.orangeschool.member.commonMember.dto.CommonMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberAcademyRepositoryCustom {

    Page<CommonMemberDto> searchByAcademy(Pageable pageable, Long academyId);
    Page<AcademyDto> searchByCommonMember(Pageable pageable, Long commonMemberId);
}
