package com.orangeschool.community.cheering.repository;

import com.orangeschool.common.enums.CheeringMessage;
import com.orangeschool.community.cheering.entity.Cheering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheeringRepository extends JpaRepository<Cheering, Long>, CheeringRepositoryCustom {

    Optional<Cheering> findByCheeringMemberIdAndCheeredMemberIdAndCheeringMessage(Long cheeringMemberId, Long cheeredMemberId, CheeringMessage cheeringMessage);

    int countByCheeredMemberIdAndCheeringMessage(Long cheeredMemberId, CheeringMessage cheeringMessage);
    
    Long countByCheeredMemberId(Long cheeredMemberId);
    
    boolean existsByCheeringMemberIdAndCheeredMemberId(Long cheeringMemberId, Long cheeredMemberId);
}
