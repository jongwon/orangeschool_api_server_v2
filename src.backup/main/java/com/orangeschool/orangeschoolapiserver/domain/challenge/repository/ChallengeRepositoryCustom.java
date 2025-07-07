package com.orangeschool.orangeschoolapiserver.domain.challenge.repository;

import com.orangeschool.orangeschoolapiserver.domain.challenge.dto.ChallengeDto;
import com.orangeschool.orangeschoolapiserver.domain.challenge.dto.ChallengeFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChallengeRepositoryCustom {

    Page<ChallengeDto> search(Long childId, Pageable pageable, ChallengeFilterDto challengeFilterDto);
}
