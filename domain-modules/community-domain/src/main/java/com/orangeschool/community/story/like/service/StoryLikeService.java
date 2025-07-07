package com.orangeschool.community.story.like;


import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.member.api.MemberInfoProvider;
import com.orangeschool.community.story.like.dto.StoryLikeDto;
import com.orangeschool.community.story.like.dto.StoryLikeSearchDto;
import com.orangeschool.community.story.like.entity.StoryLike;
import com.orangeschool.community.story.like.repository.StoryLikeRepository;
import com.orangeschool.community.story.story.entity.Story;
import com.orangeschool.community.story.story.repository.StoryRepository;
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
    private final MemberInfoProvider memberInfoProvider;

    @Transactional
    public void create(Long commonMemberId, Long storyId) throws Exception {

        Optional<CommonMember> commonMemberOptional = memberInfoProvider.getMemberInfo(commonMemberId);

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
