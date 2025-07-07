package com.orangeschool.community.pick.comment.service;

import com.orangeschool.member.api.service.MemberInfoProvider;
import com.orangeschool.member.api.dto.MemberInfo;

import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.community.pick.comment.dto.CreatePickCommentDto;
import com.orangeschool.community.pick.comment.dto.PickCommentDto;
import com.orangeschool.community.pick.comment.dto.PickCommentSearchDto;
import com.orangeschool.community.pick.comment.dto.UpdatePickCommentDto;
import com.orangeschool.community.pick.comment.entity.PickComment;
import com.orangeschool.community.pick.comment.repository.PickCommentRepository;
import com.orangeschool.community.pick.pick.entity.Pick;
import com.orangeschool.community.pick.pick.repository.PickRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PickCommentService {


    private final PickCommentRepository pickCommentRepository;
    private final PickRepository pickRepository;
    private final MemberInfoProvider memberInfoProvider;

    @Transactional
    public void create(Long commonMemberId, Long pickId, CreatePickCommentDto createPickCommentDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = memberInfoProvider.getMemberInfo(commonMemberId);

        if (!commonMemberOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<Pick> pickOptional = pickRepository.findById(pickId);

        if (!pickOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        PickComment pickComment = PickComment.builder()
                .commonMember(commonMemberOptional.get())
                .pick(pickOptional.get())
                .content(createPickCommentDto.getContent())
                .build();

        pickCommentRepository.save(pickComment);

        Pick pick = pickOptional.get();
        pick.updateTotalCommentCount(pick.getTotalCommentCount() + 1);
        pickRepository.save(pick);
    }

    @Transactional(readOnly = true)
    public Page<PickCommentDto> get(Pageable pageable, Long pickId, PickCommentSearchDto pickCommentSearchDto) throws Exception {
        return pickCommentRepository.search(pageable, pickId, pickCommentSearchDto);
    }

    @Transactional(readOnly = true)
    public PickCommentDto getById(Long commentId) throws Exception {

        Optional<PickComment> commentOptional = pickCommentRepository.findById(commentId);

        if (commentOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        return PickCommentDto.create(commentOptional.get());
    }

    @Transactional
    public void put(Long commentId, UpdatePickCommentDto updatePickCommentDto) throws Exception {

        Optional<PickComment> commentOptional = pickCommentRepository.findById(commentId);

        if (commentOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        PickComment pickComment = commentOptional.get();

        pickComment.update(
                updatePickCommentDto.getContent()
        );

        pickCommentRepository.save(pickComment);
    }

    @Transactional
    public void delete(Long commentId) throws Exception {
        Optional<PickComment> commentOptional = pickCommentRepository.findById(commentId);

        if (commentOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Pick pick = commentOptional.get().getPick();
        pick.updateTotalCommentCount(pick.getTotalCommentCount() - 1);
        pickRepository.save(pick);

        pickCommentRepository.deleteById(commentId);
    }
}
