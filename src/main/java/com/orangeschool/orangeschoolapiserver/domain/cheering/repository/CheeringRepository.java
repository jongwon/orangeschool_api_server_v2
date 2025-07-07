package com.orangeschool.orangeschoolapiserver.domain.cheering.repository;

import com.orangeschool.orangeschoolapiserver.common.enums.CheeringMessage;
import com.orangeschool.orangeschoolapiserver.domain.cheering.entity.Cheering;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CheeringRepository extends PagingAndSortingRepository<Cheering, Long>, CheeringRepositoryCustom {

    Optional<Cheering> findByCheeringMemberIdAndCheeredMemberIdAndCheeringMessage(Long cheeringMemberId, Long cheeredMemberId, CheeringMessage cheeringMessage);

    int countByCheeredMemberIdAndCheeringMessage(Long cheeredMemberId, CheeringMessage cheeringMessage);
}
