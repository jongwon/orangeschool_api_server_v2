package com.orangeschool.orangeschoolapiserver.domain.story.reply;


import com.orangeschool.orangeschoolapiserver.common.response.CustomException;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.repository.CommonMemberRepository;
import com.orangeschool.orangeschoolapiserver.domain.story.comment.entity.StoryComment;
import com.orangeschool.orangeschoolapiserver.domain.story.comment.repository.StoryCommentRepository;
import com.orangeschool.orangeschoolapiserver.domain.story.reply.dto.CreateStoryReplyDto;
import com.orangeschool.orangeschoolapiserver.domain.story.reply.dto.StoryReplyDto;
import com.orangeschool.orangeschoolapiserver.domain.story.reply.dto.StoryReplySearchDto;
import com.orangeschool.orangeschoolapiserver.domain.story.reply.dto.UpdateStoryReplyDto;
import com.orangeschool.orangeschoolapiserver.domain.story.reply.entity.StoryReply;
import com.orangeschool.orangeschoolapiserver.domain.story.reply.repository.StoryReplyRepository;
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
public class StoryReplyService {


    private final StoryReplyRepository storyReplyRepository;
    private final StoryCommentRepository storyCommentRepository;
    private final CommonMemberRepository commonMemberRepository;
    private final StoryRepository storyRepository;

    @Transactional
    public void create(Long senderId, Long storyCommentId, CreateStoryReplyDto createStoryReplyDto) throws Exception {

        Optional<CommonMember> senderOptional = commonMemberRepository.findById(senderId);

        if (!senderOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<CommonMember> receiverOptional = commonMemberRepository.findById(createStoryReplyDto.getReceiverId());

        if (!receiverOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<StoryComment> storyCommentOptional = storyCommentRepository.findById(storyCommentId);

        if (!storyCommentOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        StoryReply storyReply = StoryReply.builder()
                .sender(senderOptional.get())
                .receiver(receiverOptional.get())
                .storyComment(storyCommentOptional.get())
                .content(createStoryReplyDto.getContent())
                .build();

        storyReplyRepository.save(storyReply);

        Story story = storyCommentOptional.get().getStory();
        story.updateTotalCommentCount(story.getTotalCommentCount() + 1);
        storyRepository.save(story);
    }

    @Transactional(readOnly = true)
    public Page<StoryReplyDto> get(Pageable pageable, Long storyCommentId, StoryReplySearchDto storyReplySearchDto) throws Exception {
        return storyReplyRepository.search(pageable, storyCommentId, storyReplySearchDto);
    }

    @Transactional(readOnly = true)
    public StoryReplyDto getById(Long replyId) throws Exception {

        Optional<StoryReply> replyOptional = storyReplyRepository.findById(replyId);

        if (replyOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        return StoryReplyDto.create(replyOptional.get());
    }

    @Transactional
    public void put(Long replyId, UpdateStoryReplyDto updateStoryReplyDto) throws Exception {

        Optional<StoryReply> replyOptional = storyReplyRepository.findById(replyId);

        if (replyOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        StoryReply storyReply = replyOptional.get();

        storyReply.update(
                updateStoryReplyDto.getContent()
        );

        storyReplyRepository.save(storyReply);
    }

    @Transactional
    public void delete(Long replyId) throws Exception {
        Optional<StoryReply> replyOptional = storyReplyRepository.findById(replyId);

        if (replyOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Story story = replyOptional.get().getStoryComment().getStory();
        story.updateTotalCommentCount(story.getTotalCommentCount() - 1);
        storyRepository.save(story);

        storyReplyRepository.deleteById(replyId);
    }
}
