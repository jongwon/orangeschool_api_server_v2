package com.orangeschool.support.location.repository;

import com.orangeschool.support.location.entity.Location;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends PagingAndSortingRepository<Location, Long>, LocationRepositoryCustom {
    Optional<Location> findByCode(long code);

    List<Location> findAll();

    void saveAll(List<Location> locationList);

}
