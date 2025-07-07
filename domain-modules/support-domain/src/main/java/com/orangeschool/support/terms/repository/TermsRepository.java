package com.orangeschool.support.terms.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.orangeschool.support.terms.entity.Terms;

import java.util.List;
import java.util.Optional;

public interface TermsRepository extends PagingAndSortingRepository<Terms, Long>, TermsRepositoryCustom {

    List<Terms> findAll();

    Optional<Terms> findById(long l);

    void saveAll(List<Terms> termsList);

}
