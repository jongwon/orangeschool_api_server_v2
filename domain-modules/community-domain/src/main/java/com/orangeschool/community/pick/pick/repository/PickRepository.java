package com.orangeschool.community.pick.pick.repository;


import com.orangeschool.community.pick.pick.entity.Pick;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PickRepository extends JpaRepository<Pick, Long>, PickRepositoryCustom {

    Optional<Pick> findByNumber(Long number);
}
