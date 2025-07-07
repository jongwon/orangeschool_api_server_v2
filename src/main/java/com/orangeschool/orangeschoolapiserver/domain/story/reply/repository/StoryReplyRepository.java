package com.orangeschool.orangeschoolapiserver.domain.story.reply.repository;

import com.orangeschool.orangeschoolapiserver.domain.story.reply.entity.StoryReply;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StoryReplyRepository extends PagingAndSortingRepository<StoryReply, Long>, StoryReplyRepositoryCustom {

}
