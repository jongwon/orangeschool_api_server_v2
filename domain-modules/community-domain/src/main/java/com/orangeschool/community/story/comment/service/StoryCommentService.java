package com.orangeschool.community.story.comment;


import com.google.firebase.messaging.Notification;
import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.member.api.MemberInfoProvider;
import com.orangeschool.community.memberAlarm.MemberAlarmService;
import com.orangeschool.community.story.comment.dto.CreateStoryCommentDto;
import com.orangeschool.community.story.comment.dto.StoryCommentDto;
import com.orangeschool.community.story.comment.dto.StoryCommentSearchDto;
import com.orangeschool.community.story.comment.dto.UpdateStoryCommentDto;
import com.orangeschool.community.story.comment.entity.StoryComment;
import com.orangeschool.community.story.comment.repository.StoryCommentRepository;
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
public class StoryCommentService {


    private final StoryCommentRepository storyCommentRepository;
    private final StoryRepository storyRepository;
    private final MemberInfoProvider memberInfoProvider;

    private final MemberAlarmService memberAlarmService;

    @Transactional
    public void create(Long commonMemberId, Long storyId, CreateStoryCommentDto createStoryCommentDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = memberInfoProvider.getMemberInfo(commonMemberId);

        if (!commonMemberOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<Story> storyOptional = storyRepository.findById(storyId);

        if (!storyOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        StoryComment storyComment = StoryComment.builder()
                .commonMember(commonMemberOptional.get())
                .story(storyOptional.get())
                .content(createStoryCommentDto.getContent())
                .build();

        storyCommentRepository.save(storyComment);

        Story story = storyOptional.get();
        story.updateTotalCommentCount(story.getTotalCommentCount() + 1);
        storyRepository.save(story);

        if (!commonMemberId.equals(storyOptional.get().getCommonMember().getId())) {
            Notification notification = Notification.builder()
                    .setTitle("O's life")
                    .setBody("내가 올린 게시글에 새로운 댓글이 달렸어요!")
                    .build();

            memberAlarmService.createToSystem("O's life", "내가 올린 게시글에 새로운 댓글이 달렸어요!", notification, storyOptional.get().getCommonMember());
        }
    }

    @Transactional(readOnly = true)
    public Page<StoryCommentDto> get(Pageable pageable, Long storyId, StoryCommentSearchDto storyCommentSearchDto) throws Exception {
        return storyCommentRepository.search(pageable, storyId, storyCommentSearchDto);
    }

    @Transactional(readOnly = true)
    public StoryCommentDto getById(Long commentId) throws Exception {

        Optional<StoryComment> commentOptional = storyCommentRepository.findById(commentId);

        if (commentOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        return StoryCommentDto.create(commentOptional.get());
    }

    @Transactional
    public void put(Long commentId, UpdateStoryCommentDto updateStoryCommentDto) throws Exception {

        Optional<StoryComment> commentOptional = storyCommentRepository.findById(commentId);

        if (commentOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        StoryComment storyComment = commentOptional.get();

        storyComment.update(
                updateStoryCommentDto.getContent()
        );

        storyCommentRepository.save(storyComment);
    }

    @Transactional
    public void delete(Long commentId) throws Exception {
        Optional<StoryComment> commentOptional = storyCommentRepository.findById(commentId);

        if (commentOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Story story = commentOptional.get().getStory();
        story.updateTotalCommentCount(story.getTotalCommentCount() - 1);
        storyRepository.save(story);

        storyCommentRepository.deleteById(commentId);
    }
}
