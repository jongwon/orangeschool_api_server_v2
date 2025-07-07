package com.orangeschool.community.cheering.service;

import com.orangeschool.community.api.dto.CheeringInfo;
import com.orangeschool.community.api.service.CheeringInfoProvider;
import com.orangeschool.community.cheering.dto.CheeringDto;
import com.orangeschool.community.cheering.repository.CheeringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CheeringInfoProviderImpl implements CheeringInfoProvider {

    private final CheeringRepository cheeringRepository;

    @Override
    public Page<CheeringInfo> getCheeringsForMember(Long cheeredMemberId, Pageable pageable) {
        Page<CheeringDto> cheeringDtos = cheeringRepository.search(cheeredMemberId, pageable);
        
        return cheeringDtos.map(dto -> CheeringInfo.builder()
                .id(dto.getId())
                .cheeringMemberId(dto.getCheeringMemberId())
                .cheeringMemberNickname(dto.getCheeringMemberNickname())
                .cheeringMemberProfileImage(dto.getCheeringMemberProfileImage())
                .cheeredMemberId(cheeredMemberId)
                .cheeringMessage(dto.getCheeringMessage())
                .createdAt(dto.getCreatedAt())
                .build());
    }

    @Override
    public Long getCheeringCount(Long cheeredMemberId) {
        return cheeringRepository.countByCheeredMemberId(cheeredMemberId);
    }

    @Override
    public boolean isCheeringExists(Long cheeringMemberId, Long cheeredMemberId) {
        return cheeringRepository.existsByCheeringMemberIdAndCheeredMemberId(cheeringMemberId, cheeredMemberId);
    }
}