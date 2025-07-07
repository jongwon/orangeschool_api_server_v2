package com.orangeschool.support.suggest.repository;

import com.orangeschool.support.suggest.entity.Suggest;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SuggestRepository extends PagingAndSortingRepository<Suggest, Long> , SuggestRepositoryCustom {
}
