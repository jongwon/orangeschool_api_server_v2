package com.orangeschool.community.pick.like.service;

import com.orangeschool.member.api.service.MemberInfoProvider;
import com.orangeschool.member.api.dto.MemberInfo;

import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.community.pick.like.dto.PickLikeDto;
import com.orangeschool.community.pick.like.dto.PickLikeSearchDto;
import com.orangeschool.community.pick.like.entity.PickLike;
import com.orangeschool.community.pick.like.repository.PickLikeRepository;
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
public class PickLikeService {


    private final PickLikeRepository pickLikeRepository;
    private final PickRepository pickRepository;
    private final MemberInfoProvider memberInfoProvider;

    @Transactional
    public void create(Long commonMemberId, Long pickId) throws Exception {

        Optional<CommonMember> commonMemberOptional = memberInfoProvider.getMemberInfo(commonMemberId);

        if (!commonMemberOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<Pick> pickOptional = pickRepository.findById(pickId);

        if (!pickOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<PickLike> pickLikeOptional = pickLikeRepository
                .findByCommonMemberIdAndPickId(commonMemberId, pickId);

        if (pickLikeOptional.isPresent()) {
            pickLikeRepository.deleteById(pickLikeOptional.get().getId());
        } else {
            PickLike pickLike = PickLike.builder()
                    .commonMember(commonMemberOptional.get())
                    .pick(pickOptional.get())
                    .build();

            pickLikeRepository.save(pickLike);
        }
    }

    @Transactional(readOnly = true)
    public Page<PickLikeDto> get(Pageable pageable, Long pickId, PickLikeSearchDto pickLikeSearchDto) throws Exception {
        return pickLikeRepository.search(pageable, pickId, pickLikeSearchDto);
    }
}
