package com.orangeschool.community.cheering.service;

import com.orangeschool.common.enums.CheeringMessage;
import com.orangeschool.community.api.service.CheeringInfoProvider;
import com.orangeschool.community.api.dto.CheeringInfo;
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
    public int countCheeringByType(Long memberId, CheeringMessage cheeringMessage) {
        return cheeringRepository.countByCheeredMemberIdAndCheeringMessage(memberId, cheeringMessage);
    }

    @Override
    public boolean hasCheered(Long cheeringMemberId, Long cheeredMemberId, CheeringMessage cheeringMessage) {
        return cheeringRepository.findByCheeringMemberIdAndCheeredMemberIdAndCheeringMessage(
                cheeringMemberId, cheeredMemberId, cheeringMessage).isPresent();
    }

    @Override
    public Page<CheeringInfo> getCheeringList(Long memberId, Pageable pageable) {
        // TODO: 추후 구현
        return Page.empty();
    }
}