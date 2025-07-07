package com.orangeschool.education.memberAcademy.repository;


import com.orangeschool.education.memberAcademy.entity.MemberAcademy;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MemberAcademyRepository extends PagingAndSortingRepository<MemberAcademy, Long>, MemberAcademyRepositoryCustom {

    void deleteByAcademyIdAndCommonMemberId(Long academyId, Long commonMemberId);
}
