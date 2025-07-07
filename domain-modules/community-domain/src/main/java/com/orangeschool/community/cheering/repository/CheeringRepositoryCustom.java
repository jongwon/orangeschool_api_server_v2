package com.orangeschool.community.cheering.repository;

import com.orangeschool.community.cheering.dto.CheeringDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CheeringRepositoryCustom {

    Page<CheeringDto> search(Long cheeredId, Pageable pageable);
}
