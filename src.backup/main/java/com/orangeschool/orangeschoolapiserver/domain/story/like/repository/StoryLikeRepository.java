package com.orangeschool.orangeschoolapiserver.domain.story.like.repository;

import com.orangeschool.orangeschoolapiserver.domain.story.like.entity.StoryLike;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface StoryLikeRepository extends PagingAndSortingRepository<StoryLike, Long>, StoryLikeRepositoryCustom {
    Optional<StoryLike> findByCommonMemberIdAndStoryId(Long commonMemberId, Long storyId);
}
