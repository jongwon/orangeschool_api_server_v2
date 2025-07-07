package com.orangeschool.orangeschoolapiserver.domain.suggest.repository;

import com.orangeschool.orangeschoolapiserver.domain.suggest.entity.Suggest;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SuggestRepository extends PagingAndSortingRepository<Suggest, Long> , SuggestRepositoryCustom {
}
