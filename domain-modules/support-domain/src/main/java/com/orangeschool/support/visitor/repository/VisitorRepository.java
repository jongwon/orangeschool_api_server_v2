package com.orangeschool.support.visitor.repository;


import com.orangeschool.common.enums.MemberType;
import com.orangeschool.support.visitor.entity.Visitor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface VisitorRepository extends PagingAndSortingRepository<Visitor, Long>, VisitorRepositoryCustom {

    Optional<Visitor> findByTodayAndMemberType(LocalDate today, MemberType memberType);

}
