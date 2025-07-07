package com.orangeschool.support.visitor.repository;


import com.orangeschool.common.enums.MemberType;
import com.orangeschool.support.visitor.dto.VisitorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VisitorRepositoryCustom {

    Page<VisitorDto> search(Pageable pageable, MemberType memberType);
}
