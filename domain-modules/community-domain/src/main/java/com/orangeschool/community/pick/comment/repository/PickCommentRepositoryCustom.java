package com.orangeschool.community.pick.comment.repository;


import com.orangeschool.community.pick.comment.dto.PickCommentDto;
import com.orangeschool.community.pick.comment.dto.PickCommentSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PickCommentRepositoryCustom {
    Page<PickCommentDto> search(Pageable pageable, Long pickId, PickCommentSearchDto pickCommentSearchDto);
}
