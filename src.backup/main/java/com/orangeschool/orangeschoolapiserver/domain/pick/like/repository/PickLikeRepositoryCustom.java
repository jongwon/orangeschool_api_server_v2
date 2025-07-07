package com.orangeschool.orangeschoolapiserver.domain.pick.like.repository;


import com.orangeschool.orangeschoolapiserver.domain.pick.like.dto.PickLikeDto;
import com.orangeschool.orangeschoolapiserver.domain.pick.like.dto.PickLikeSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PickLikeRepositoryCustom {
    Page<PickLikeDto> search(Pageable pageable, Long storyId, PickLikeSearchDto storyCommentSearchDto);
}
