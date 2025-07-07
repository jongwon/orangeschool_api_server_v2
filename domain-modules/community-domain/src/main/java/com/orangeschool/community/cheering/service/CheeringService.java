package com.orangeschool.community.cheering.service;

import com.orangeschool.member.api.service.MemberInfoProvider;
import com.orangeschool.member.api.dto.MemberInfo;
import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.community.cheering.dto.CheeringDto;
import com.orangeschool.community.cheering.dto.CheeringRequestDto;
import com.orangeschool.community.cheering.entity.Cheering;
import com.orangeschool.community.cheering.repository.CheeringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CheeringService {

    private final CheeringRepository cheeringRepository;
    private final MemberInfoProvider memberInfoProvider;

    @Transactional
    public CheeringDto createCheering(CheeringRequestDto requestDto) {
        // 자기 자신을 응원할 수 없음
        if (requestDto.getCheeringMemberId().equals(requestDto.getCheeredMemberId())) {
            throw new CustomException(ResponseCode.BAD_REQUEST);
        }
        
        // 회원 존재 여부 확인
        Optional<MemberInfo> cheeringMemberOpt = memberInfoProvider.getMemberInfo(requestDto.getCheeringMemberId());
        Optional<MemberInfo> cheeredMemberOpt = memberInfoProvider.getMemberInfo(requestDto.getCheeredMemberId());

        if (cheeringMemberOpt.isEmpty() || cheeredMemberOpt.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND_USER);
        }
        
        MemberInfo cheeringMember = cheeringMemberOpt.get();
        MemberInfo cheeredMember = cheeredMemberOpt.get();

        // 오늘 이미 응원했는지 확인
        Optional<Cheering> todayCheering = cheeringRepository.findTodayCheeringBetweenMembers(
            requestDto.getCheeringMemberId(), 
            requestDto.getCheeredMemberId()
        );

        if (todayCheering.isPresent()) {
            throw new CustomException(ResponseCode.ALREADY_CHEERED_TODAY);
        }

        Cheering cheering = Cheering.builder()
            .cheeringMemberId(requestDto.getCheeringMemberId())
            .cheeredMemberId(requestDto.getCheeredMemberId())
            .message(requestDto.getMessage())
            .build();

        cheering = cheeringRepository.save(cheering);

        return convertToDto(cheering, cheeringMember, cheeredMember);
    }

    public Page<CheeringDto> getCheeringList(Long memberId, Pageable pageable) {
        Page<Cheering> cheeringPage = cheeringRepository.findByCheeredMemberId(memberId, pageable);

        return cheeringPage.map(cheering -> {
            Optional<MemberInfo> cheeringMemberOpt = memberInfoProvider.getMemberInfo(cheering.getCheeringMemberId());
            Optional<MemberInfo> cheeredMemberOpt = memberInfoProvider.getMemberInfo(cheering.getCheeredMemberId());
            
            // 회원 정보가 없는 경우 기본값 처리
            MemberInfo cheeringMember = cheeringMemberOpt.orElse(null);
            MemberInfo cheeredMember = cheeredMemberOpt.orElse(null);
            
            return convertToDto(cheering, cheeringMember, cheeredMember);
        });
    }

    private CheeringDto convertToDto(Cheering cheering, MemberInfo cheeringMember, MemberInfo cheeredMember) {
        return CheeringDto.builder()
            .id(cheering.getId())
            .cheeringMember(cheeringMember)
            .cheeredMember(cheeredMember)
            .message(cheering.getMessage())
            .createdAt(cheering.getCreatedAt())
            .build();
    }
}
