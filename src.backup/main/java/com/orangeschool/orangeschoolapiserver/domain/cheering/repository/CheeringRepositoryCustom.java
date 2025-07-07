package com.orangeschool.orangeschoolapiserver.domain.cheering.repository;

import com.orangeschool.orangeschoolapiserver.domain.cheering.dto.CheeringDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CheeringRepositoryCustom {

    Page<CheeringDto> search(Long cheeredId, Pageable pageable);
}
