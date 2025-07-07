package com.orangeschool.orangeschoolapiserver.domain.pick.pick.repository;


import com.orangeschool.orangeschoolapiserver.domain.pick.pick.entity.Pick;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface PickRepository extends PagingAndSortingRepository<Pick, Long>, PickRepositoryCustom {

    Optional<Pick> findByNumber(Long number);
}
