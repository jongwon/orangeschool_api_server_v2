package com.orangeschool.orangeschoolapiserver.domain.pick.pick.repository;

import com.orangeschool.orangeschoolapiserver.domain.pick.pick.dto.PickDto;
import com.orangeschool.orangeschoolapiserver.domain.pick.pick.dto.PickFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PickRepositoryCustom {

    Page<PickDto> search(Pageable pageable, PickFilterDto pickFilterDto);
    Page<PickDto> searchToUser(Pageable pageable, PickFilterDto pickFilterDto, Long commonMemberId);
}
