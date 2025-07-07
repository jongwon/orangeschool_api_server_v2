package com.orangeschool.community.pick.reply.repository;


import com.orangeschool.community.pick.reply.dto.PickReplyDto;
import com.orangeschool.community.pick.reply.dto.PickReplySearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface PickReplyRepositoryCustom {
    Page<PickReplyDto> search(Pageable pageable, Long storyCommentId, PickReplySearchDto pickReplySearchDto);
}
