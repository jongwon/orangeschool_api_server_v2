package com.orangeschool.orangeschoolapiserver.domain.pick.reply.repository;

import com.orangeschool.orangeschoolapiserver.domain.pick.reply.entity.PickReply;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PickReplyRepository extends PagingAndSortingRepository<PickReply, Long>, PickReplyRepositoryCustom {

}
