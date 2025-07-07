package com.orangeschool.education.challenge.repository;


import com.orangeschool.common.enums.ChallengeStatus;
import com.orangeschool.education.challenge.entity.Challenge;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ChallengeRepository extends PagingAndSortingRepository<Challenge, Long>, ChallengeRepositoryCustom {

    Optional<Challenge> findByCommonMemberIdAndChallengeStatus(Long commonMemberId, ChallengeStatus challengeStatus);
}
