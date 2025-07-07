package com.orangeschool.orangeschoolapiserver.domain.academy.repository;


import com.orangeschool.orangeschoolapiserver.domain.academy.entity.Academy;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AcademyRepository extends PagingAndSortingRepository<Academy, Long>, AcademyRepositoryCustom {

    List<Academy> findAll();
    List<Academy> findByIdIn(List<Long> ids);
}
