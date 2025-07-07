package com.orangeschool.orangeschoolapiserver.domain.pick.comment.repository;


import com.orangeschool.orangeschoolapiserver.domain.pick.comment.dto.PickCommentDto;
import com.orangeschool.orangeschoolapiserver.domain.pick.comment.dto.PickCommentSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PickCommentRepositoryCustom {
    Page<PickCommentDto> search(Pageable pageable, Long pickId, PickCommentSearchDto pickCommentSearchDto);
}
