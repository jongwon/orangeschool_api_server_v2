package com.orangeschool.community.story.reply.repository;

import com.orangeschool.community.story.reply.entity.StoryReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryReplyRepository extends JpaRepository<StoryReply, Long>, StoryReplyRepositoryCustom {

}
