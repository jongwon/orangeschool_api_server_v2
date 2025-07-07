package com.orangeschool.orangeschoolapiserver.domain.cheering;

import com.orangeschool.orangeschoolapiserver.common.response.CustomException;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.domain.cheering.dto.CheeringDto;
import com.orangeschool.orangeschoolapiserver.domain.cheering.dto.CheeringRequestDto;
import com.orangeschool.orangeschoolapiserver.domain.cheering.entity.Cheering;
import com.orangeschool.orangeschoolapiserver.domain.cheering.repository.CheeringRepository;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.repository.CommonMemberRepository;
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
    private final CommonMemberRepository commonMemberRepository;

    @Transactional
    public void cheering(Long cheeringMemberId, Long cheeredMemberId, CheeringRequestDto cheeringRequestDto) throws Exception {

        if (cheeringMemberId == cheeredMemberId) {
            throw new CustomException(ResponseCode.BAD_REQUEST);
        }

        Optional<CommonMember> cheeringMemberOptional = commonMemberRepository.findById(cheeringMemberId);

        if (cheeringMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<CommonMember> cheeredMemberOptional = commonMemberRepository.findById(cheeredMemberId);

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
