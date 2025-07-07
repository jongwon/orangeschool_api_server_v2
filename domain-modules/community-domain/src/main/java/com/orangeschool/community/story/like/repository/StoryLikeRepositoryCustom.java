package com.orangeschool.community.story.like.repository;


import com.orangeschool.community.story.like.dto.StoryLikeDto;
import com.orangeschool.community.story.like.dto.StoryLikeSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoryLikeRepositoryCustom {
    Page<StoryLikeDto> search(Pageable pageable,Long storyId, StoryLikeSearchDto storyCommentSearchDto);
}
