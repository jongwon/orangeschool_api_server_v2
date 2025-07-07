package com.orangeschool.community.pick.reply.repository;

import com.orangeschool.community.pick.reply.entity.PickReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PickReplyRepository extends JpaRepository<PickReply, Long>, PickReplyRepositoryCustom {

}
