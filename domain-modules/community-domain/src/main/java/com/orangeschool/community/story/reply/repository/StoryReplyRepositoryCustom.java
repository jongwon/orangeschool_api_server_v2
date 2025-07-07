package com.orangeschool.community.story.reply.repository;


import com.orangeschool.community.story.reply.dto.StoryReplyDto;
import com.orangeschool.community.story.reply.dto.StoryReplySearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface StoryReplyRepositoryCustom {
    Page<StoryReplyDto> search(Pageable pageable,Long storyCommentId, StoryReplySearchDto storyReplySearchDto);
}
