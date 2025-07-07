package com.orangeschool.education.challenge.repository;

import com.orangeschool.education.challenge.dto.ChallengeDto;
import com.orangeschool.education.challenge.dto.ChallengeFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChallengeRepositoryCustom {

    Page<ChallengeDto> search(Long childId, Pageable pageable, ChallengeFilterDto challengeFilterDto);
}
