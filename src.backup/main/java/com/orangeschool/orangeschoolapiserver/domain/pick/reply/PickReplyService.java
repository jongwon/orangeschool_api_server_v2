package com.orangeschool.orangeschoolapiserver.domain.pick.reply;


import com.orangeschool.orangeschoolapiserver.common.response.CustomException;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.repository.CommonMemberRepository;
import com.orangeschool.orangeschoolapiserver.domain.pick.comment.entity.PickComment;
import com.orangeschool.orangeschoolapiserver.domain.pick.comment.repository.PickCommentRepository;
import com.orangeschool.orangeschoolapiserver.domain.pick.pick.entity.Pick;
import com.orangeschool.orangeschoolapiserver.domain.pick.pick.repository.PickRepository;
import com.orangeschool.orangeschoolapiserver.domain.pick.reply.dto.CreatePickReplyDto;
import com.orangeschool.orangeschoolapiserver.domain.pick.reply.dto.PickReplyDto;
import com.orangeschool.orangeschoolapiserver.domain.pick.reply.dto.PickReplySearchDto;
import com.orangeschool.orangeschoolapiserver.domain.pick.reply.dto.UpdatePickReplyDto;
import com.orangeschool.orangeschoolapiserver.domain.pick.reply.entity.PickReply;
import com.orangeschool.orangeschoolapiserver.domain.pick.reply.repository.PickReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PickReplyService {


    private final PickReplyRepository pickReplyRepository;
    private final PickCommentRepository pickCommentRepository;
    private final CommonMemberRepository commonMemberRepository;
    private final PickRepository pickRepository;

    @Transactional
    public void create(Long senderId, Long pickCommentId, CreatePickReplyDto createPickReplyDto) throws Exception {

        Optional<CommonMember> senderOptional = commonMemberRepository.findById(senderId);

        if (!senderOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<CommonMember> receiverOptional = commonMemberRepository.findById(createPickReplyDto.getReceiverId());

        if (!receiverOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<PickComment> pickCommentOptional = pickCommentRepository.findById(pickCommentId);

        if (!pickCommentOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        PickReply pickReply = PickReply.builder()
                .sender(senderOptional.get())
                .receiver(receiverOptional.get())
                .pickComment(pickCommentOptional.get())
                .content(createPickReplyDto.getContent())
                .build();

        pickReplyRepository.save(pickReply);

        Pick pick = pickCommentOptional.get().getPick();
        pick.updateTotalCommentCount(pick.getTotalCommentCount() + 1);
        pickRepository.save(pick);
    }

    @Transactional(readOnly = true)
    public Page<PickReplyDto> get(Pageable pageable, Long pickCommentId, PickReplySearchDto pickReplySearchDto) throws Exception {
        return pickReplyRepository.search(pageable, pickCommentId, pickReplySearchDto);
    }

    @Transactional(readOnly = true)
    public PickReplyDto getById(Long replyId) throws Exception {

        Optional<PickReply> replyOptional = pickReplyRepository.findById(replyId);

        if (replyOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        return PickReplyDto.create(replyOptional.get());
    }

    @Transactional
    public void put(Long replyId, UpdatePickReplyDto updatePickReplyDto) throws Exception {

        Optional<PickReply> replyOptional = pickReplyRepository.findById(replyId);

        if (replyOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        PickReply pickReply = replyOptional.get();

        pickReply.update(
                updatePickReplyDto.getContent()
        );

        pickReplyRepository.save(pickReply);
    }

    @Transactional
    public void delete(Long replyId) throws Exception {
        Optional<PickReply> replyOptional = pickReplyRepository.findById(replyId);

        if (replyOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Pick pick = replyOptional.get().getPickComment().getPick();
        pick.updateTotalCommentCount(pick.getTotalCommentCount() - 1);
        pickRepository.save(pick);

        pickReplyRepository.deleteById(replyId);
    }
}
