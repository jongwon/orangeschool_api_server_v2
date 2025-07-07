package com.orangeschool.community.pick.like.repository;

import com.orangeschool.community.pick.like.entity.PickLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PickLikeRepository extends JpaRepository<PickLike, Long>, PickLikeRepositoryCustom {
    Optional<PickLike> findByCommonMemberIdAndPickId(Long commonMemberId, Long pickId);
}
