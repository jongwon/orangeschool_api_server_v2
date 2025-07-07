package com.orangeschool.orangeschoolapiserver.domain.challenge.repository;


import com.orangeschool.orangeschoolapiserver.common.enums.ChallengeStatus;
import com.orangeschool.orangeschoolapiserver.domain.challenge.entity.Challenge;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ChallengeRepository extends PagingAndSortingRepository<Challenge, Long>, ChallengeRepositoryCustom {

    Optional<Challenge> findByCommonMemberIdAndChallengeStatus(Long commonMemberId, ChallengeStatus challengeStatus);
}
