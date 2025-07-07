package com.orangeschool.orangeschoolapiserver.domain.memberAcademy.repository;


import com.orangeschool.orangeschoolapiserver.domain.memberAcademy.entity.MemberAcademy;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MemberAcademyRepository extends PagingAndSortingRepository<MemberAcademy, Long>, MemberAcademyRepositoryCustom {

    void deleteByAcademyIdAndCommonMemberId(Long academyId, Long commonMemberId);
}
