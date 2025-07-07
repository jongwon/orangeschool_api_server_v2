package com.orangeschool.community.cheering.repository;

import com.orangeschool.community.cheering.entity.Cheering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CheeringRepository extends JpaRepository<Cheering, Long>, CheeringRepositoryCustom {
    
    Page<Cheering> findByCheeredMemberId(Long cheeredMemberId, Pageable pageable);
    
    @Query("SELECT c FROM Cheering c WHERE c.cheeringMemberId = :cheeringMemberId " +
           "AND c.cheeredMemberId = :cheeredMemberId " +
           "AND DATE(c.createdAt) = CURRENT_DATE")
    Optional<Cheering> findTodayCheeringBetweenMembers(
        @Param("cheeringMemberId") Long cheeringMemberId, 
        @Param("cheeredMemberId") Long cheeredMemberId
    );
}
