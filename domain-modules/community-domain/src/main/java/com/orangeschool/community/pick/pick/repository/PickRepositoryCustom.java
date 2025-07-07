package com.orangeschool.community.pick.pick.repository;

import com.orangeschool.community.pick.pick.dto.PickDto;
import com.orangeschool.community.pick.pick.dto.PickFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PickRepositoryCustom {

    Page<PickDto> search(Pageable pageable, PickFilterDto pickFilterDto);
    Page<PickDto> searchToUser(Pageable pageable, PickFilterDto pickFilterDto, Long commonMemberId);
}
