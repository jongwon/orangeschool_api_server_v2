package com.orangeschool.orangeschoolapiserver.domain.pick.like;


import com.orangeschool.orangeschoolapiserver.common.response.CustomException;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.repository.CommonMemberRepository;
import com.orangeschool.orangeschoolapiserver.domain.pick.like.dto.PickLikeDto;
import com.orangeschool.orangeschoolapiserver.domain.pick.like.dto.PickLikeSearchDto;
import com.orangeschool.orangeschoolapiserver.domain.pick.like.entity.PickLike;
import com.orangeschool.orangeschoolapiserver.domain.pick.like.repository.PickLikeRepository;
import com.orangeschool.orangeschoolapiserver.domain.pick.pick.entity.Pick;
import com.orangeschool.orangeschoolapiserver.domain.pick.pick.repository.PickRepository;
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
    private final CommonMemberRepository commonMemberRepository;

    @Transactional
    public void create(Long commonMemberId, Long pickId) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

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
