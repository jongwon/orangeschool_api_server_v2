package com.orangeschool.community.story.comment.repository;


import com.orangeschool.community.story.comment.dto.StoryCommentDto;
import com.orangeschool.community.story.comment.dto.StoryCommentSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoryCommentRepositoryCustom {
    Page<StoryCommentDto> search(Pageable pageable, Long storyId, StoryCommentSearchDto storyCommentSearchDto);
}
