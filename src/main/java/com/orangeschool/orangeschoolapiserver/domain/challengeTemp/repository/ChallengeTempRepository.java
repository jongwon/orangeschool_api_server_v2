package com.orangeschool.orangeschoolapiserver.domain.challengeTemp.repository;


import com.orangeschool.orangeschoolapiserver.domain.challengeTemp.entity.ChallengeTemp;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ChallengeTempRepository extends PagingAndSortingRepository<ChallengeTemp, Long> {

    Optional<ChallengeTemp> findByChallengeId(Long challengeId);
    void deleteByChallengeId(Long challengeId);
}
