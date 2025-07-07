package com.orangeschool.community.pick.comment.repository;

import com.orangeschool.community.pick.comment.entity.PickComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PickCommentRepository extends JpaRepository<PickComment, Long>, PickCommentRepositoryCustom {
}
