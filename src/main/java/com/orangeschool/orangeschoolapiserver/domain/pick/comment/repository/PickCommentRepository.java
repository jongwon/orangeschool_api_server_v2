package com.orangeschool.orangeschoolapiserver.domain.pick.comment.repository;

import com.orangeschool.orangeschoolapiserver.domain.pick.comment.entity.PickComment;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PickCommentRepository extends PagingAndSortingRepository<PickComment, Long>, PickCommentRepositoryCustom {
}
