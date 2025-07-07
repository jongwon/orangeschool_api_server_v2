package com.orangeschool.orangeschoolapiserver.domain.story.like;


import com.orangeschool.orangeschoolapiserver.common.response.CustomException;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.repository.CommonMemberRepository;
import com.orangeschool.orangeschoolapiserver.domain.story.like.dto.StoryLikeDto;
import com.orangeschool.orangeschoolapiserver.domain.story.like.dto.StoryLikeSearchDto;
import com.orangeschool.orangeschoolapiserver.domain.story.like.entity.StoryLike;
import com.orangeschool.orangeschoolapiserver.domain.story.like.repository.StoryLikeRepository;
import com.orangeschool.orangeschoolapiserver.domain.story.story.entity.Story;
import com.orangeschool.orangeschoolapiserver.domain.story.story.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StoryLikeService {


    private final StoryLikeRepository storyLikeRepository;
    private final StoryRepository storyRepository;
    private final CommonMemberRepository commonMemberRepository;

    @Transactional
    public void create(Long commonMemberId, Long storyId) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (!commonMemberOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<Story> storyOptional = storyRepository.findById(storyId);

        if (!storyOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<StoryLike> storyLikeOptional = storyLikeRepository
                .findByCommonMemberIdAndStoryId(commonMemberId, storyId);

        if (storyLikeOptional.isPresent()) {
            storyLikeRepository.deleteById(storyLikeOptional.get().getId());
        } else {
            StoryLike storyLike = StoryLike.builder()
                    .commonMember(commonMemberOptional.get())
                    .story(storyOptional.get())
                    .build();

            storyLikeRepository.save(storyLike);
        }
    }

    @Transactional(readOnly = true)
    public Page<StoryLikeDto> get(Pageable pageable, Long storyId, StoryLikeSearchDto storyLikeSearchDto) throws Exception {
        return storyLikeRepository.search(pageable, storyId, storyLikeSearchDto);
    }
}
