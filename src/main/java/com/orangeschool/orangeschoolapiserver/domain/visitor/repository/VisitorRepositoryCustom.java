package com.orangeschool.orangeschoolapiserver.domain.visitor.repository;


import com.orangeschool.orangeschoolapiserver.common.enums.MemberType;
import com.orangeschool.orangeschoolapiserver.domain.visitor.dto.VisitorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VisitorRepositoryCustom {

    Page<VisitorDto> search(Pageable pageable, MemberType memberType);
}
