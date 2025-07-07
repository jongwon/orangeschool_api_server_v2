package com.orangeschool.community.pick.like.repository;


import com.orangeschool.community.pick.like.dto.PickLikeDto;
import com.orangeschool.community.pick.like.dto.PickLikeSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PickLikeRepositoryCustom {
    Page<PickLikeDto> search(Pageable pageable, Long storyId, PickLikeSearchDto storyCommentSearchDto);
}
