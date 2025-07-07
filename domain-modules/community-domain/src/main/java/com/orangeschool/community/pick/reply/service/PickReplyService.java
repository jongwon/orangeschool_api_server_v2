package com.orangeschool.community.pick.reply;


import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.member.api.MemberInfoProvider;
import com.orangeschool.community.pick.comment.entity.PickComment;
import com.orangeschool.community.pick.comment.repository.PickCommentRepository;
import com.orangeschool.community.pick.pick.entity.Pick;
import com.orangeschool.community.pick.pick.repository.PickRepository;
import com.orangeschool.community.pick.reply.dto.CreatePickReplyDto;
import com.orangeschool.community.pick.reply.dto.PickReplyDto;
import com.orangeschool.community.pick.reply.dto.PickReplySearchDto;
import com.orangeschool.community.pick.reply.dto.UpdatePickReplyDto;
import com.orangeschool.community.pick.reply.entity.PickReply;
import com.orangeschool.community.pick.reply.repository.PickReplyRepository;
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
    private final MemberInfoProvider memberInfoProvider;
    private final PickRepository pickRepository;

    @Transactional
    public void create(Long senderId, Long pickCommentId, CreatePickReplyDto createPickReplyDto) throws Exception {

        Optional<CommonMember> senderOptional = memberInfoProvider.getMemberInfo(senderId);

        if (!senderOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<CommonMember> receiverOptional = memberInfoProvider.getMemberInfo(createPickReplyDto.getReceiverId());

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
