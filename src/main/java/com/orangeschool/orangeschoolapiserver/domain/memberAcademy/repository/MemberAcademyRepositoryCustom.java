package com.orangeschool.orangeschoolapiserver.domain.memberAcademy.repository;

import com.orangeschool.orangeschoolapiserver.domain.academy.dto.AcademyDto;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.CommonMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberAcademyRepositoryCustom {

    Page<CommonMemberDto> searchByAcademy(Pageable pageable, Long academyId);
    Page<AcademyDto> searchByCommonMember(Pageable pageable, Long commonMemberId);
}
