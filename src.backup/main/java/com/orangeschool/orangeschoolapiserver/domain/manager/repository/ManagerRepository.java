package com.orangeschool.orangeschoolapiserver.domain.manager.repository;

import com.orangeschool.orangeschoolapiserver.domain.manager.entity.Manager;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ManagerRepository extends PagingAndSortingRepository<Manager, Long>, ManagerRepositoryCustom {

    Optional<Manager> findByEmail(String email);

    Optional<Manager> findById(long l);
    
}
