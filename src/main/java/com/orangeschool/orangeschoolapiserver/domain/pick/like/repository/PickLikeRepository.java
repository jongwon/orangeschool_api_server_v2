package com.orangeschool.orangeschoolapiserver.domain.pick.like.repository;

import com.orangeschool.orangeschoolapiserver.domain.pick.like.entity.PickLike;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface PickLikeRepository extends PagingAndSortingRepository<PickLike, Long>, PickLikeRepositoryCustom {
    Optional<PickLike> findByCommonMemberIdAndPickId(Long commonMemberId, Long pickId);
}
