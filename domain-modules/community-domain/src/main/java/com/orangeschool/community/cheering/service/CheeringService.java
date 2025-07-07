package com.orangeschool.community.cheering;

import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.community.cheering.dto.CheeringDto;
import com.orangeschool.community.cheering.dto.CheeringRequestDto;
import com.orangeschool.community.cheering.entity.Cheering;
import com.orangeschool.community.cheering.repository.CheeringRepository;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.member.api.MemberInfoProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CheeringService {

    private final CheeringRepository cheeringRepository;
    private final MemberInfoProvider memberInfoProvider;

    @Transactional
    public void cheering(Long cheeringMemberId, Long cheeredMemberId, CheeringRequestDto cheeringRequestDto) throws Exception {

        if (cheeringMemberId == cheeredMemberId) {
            throw new CustomException(ResponseCode.BAD_REQUEST);
        }

        Optional<CommonMember> cheeringMemberOptional = memberInfoProvider.getMemberInfo(cheeringMemberId);

        if (cheeringMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<CommonMember> cheeredMemberOptional = memberInfoProvider.getMemberInfo(cheeredMemberId);

        if (cheeredMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<Cheering> cheeringOptional = cheeringRepository
                .findByCheeringMemberIdAndCheeredMemberIdAndCheeringMessage(cheeringMemberId, cheeredMemberId, cheeringRequestDto.getCheeringMessage());

        if (cheeringOptional.isPresent()) {
            cheeringRepository.deleteById(cheeringOptional.get().getId());
        } else {
            Cheering cheering = Cheering.builder()
                    .cheeringMember(cheeringMemberOptional.get())
                    .cheeredMember(cheeredMemberOptional.get())
                    .cheeringMessage(cheeringRequestDto.getCheeringMessage())
                    .build();

            cheeringRepository.save(cheering);
        }
    }

    @Transactional(readOnly = true)
    public Page<CheeringDto> get(Long cheeredId, Pageable pageable) throws Exception {
        return cheeringRepository.search(cheeredId, pageable);
    }
}
