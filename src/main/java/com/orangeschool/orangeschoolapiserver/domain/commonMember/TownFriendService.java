package com.orangeschool.orangeschoolapiserver.domain.commonMember;

import com.orangeschool.orangeschoolapiserver.common.enums.ChallengeStatus;
import com.orangeschool.orangeschoolapiserver.common.enums.CheeringMessage;
import com.orangeschool.orangeschoolapiserver.common.response.CustomException;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.domain.challenge.entity.Challenge;
import com.orangeschool.orangeschoolapiserver.domain.challenge.repository.ChallengeRepository;
import com.orangeschool.orangeschoolapiserver.domain.cheering.repository.CheeringRepository;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.CommonMemberProfileDto;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.TownFriendFilterDto;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.repository.CommonMemberRepository;
import com.orangeschool.orangeschoolapiserver.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TownFriendService {

    private final FollowRepository followRepository;
    private final CheeringRepository cheeringRepository;
    private final CommonMemberRepository commonMemberRepository;
    private final ChallengeRepository challengeRepository;

    @Transactional(readOnly = true)
    public CommonMemberProfileDto getProfile(Long commonMemberId) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMemberProfileDto commonMemberProfileDto = CommonMemberProfileDto.create(commonMemberOptional.get());
        commonMemberProfileDto.setFollower(followRepository.countByFollowerMemberId(commonMemberId));
        commonMemberProfileDto.setFollowing(followRepository.countByFollowingMemberId(commonMemberId));

        return commonMemberProfileDto;
    }

    @Transactional(readOnly = true)
    public Page<CommonMemberProfileDto> get(Long followingMemberId, Pageable pageable) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(followingMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        long locationCode = commonMemberOptional.get().getLocationCode();
        List<CommonMember> top10List = commonMemberRepository.findTop10ByLocationCodeAndChallengeProgressOrderByTotalOrangeDesc(locationCode, true);

        Page<CommonMemberProfileDto> commonMemberProfileDtoPage = followRepository.search(followingMemberId, pageable);
        return commonMemberProfileDtoPage.map(commonMemberProfileDto -> {
            commonMemberProfileDto.setIsTop10(top10List.stream().anyMatch(top10Member -> top10Member.getId() == commonMemberProfileDto.getId()));
            return commonMemberProfileDto;
        });
    }

    @Transactional(readOnly = true)
    public List<CommonMemberProfileDto> getTop(Long commonMemberId) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        long locationCode = commonMemberOptional.get().getLocationCode();
        List<CommonMember> top10List = commonMemberRepository.findTop10ByLocationCodeAndChallengeProgressOrderByTotalOrangeDesc(locationCode, true);

        return top10List.stream().map(commonMember -> {
            CommonMemberProfileDto commonMemberProfileDto = CommonMemberProfileDto.create(commonMemberId, commonMember);
            commonMemberProfileDto.setIsTop10(top10List.stream().anyMatch(top10Member -> top10Member.getId() == commonMember.getId()));
            return commonMemberProfileDto;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<CommonMemberProfileDto> getBySearch(Long commonMemberId, Pageable pageable, TownFriendFilterDto townFriendFilterDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        List<CommonMember> top10List = commonMemberRepository.findTop10ByLocationCodeAndChallengeProgressOrderByTotalOrangeDesc(commonMemberOptional.get().getLocationCode(), true);

        Page<CommonMemberProfileDto> commonMemberProfileDtoPage = commonMemberRepository.search(commonMemberId, pageable, townFriendFilterDto);

        return commonMemberProfileDtoPage.map(commonMemberProfileDto -> {
            commonMemberProfileDto.setIsTop10(top10List.stream().anyMatch(top10Member -> top10Member.getId() == commonMemberProfileDto.getId()));
            return commonMemberProfileDto;
        });
    }

    @Transactional(readOnly = true)
    public CommonMemberProfileDto getById(Long myId, Long childId, Long commonMemberId) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMemberProfileDto commonMemberProfileDto = CommonMemberProfileDto.create(childId, commonMemberOptional.get());
        commonMemberProfileDto.setFollower(followRepository.countByFollowerMemberId(commonMemberId));
        commonMemberProfileDto.setFollowing(followRepository.countByFollowingMemberId(commonMemberId));
        Optional<Challenge> challengeOptional = challengeRepository.findByCommonMemberIdAndChallengeStatus(commonMemberId, ChallengeStatus.PROGRESS);
        commonMemberProfileDto.setMission(challengeOptional .isPresent() ? challengeOptional.get().getMission() : null);

        int cheering1Count = cheeringRepository.countByCheeredMemberIdAndCheeringMessage(commonMemberId, CheeringMessage.CHEERING1);
        int cheering2Count = cheeringRepository.countByCheeredMemberIdAndCheeringMessage(commonMemberId, CheeringMessage.CHEERING2);
        int cheering3Count = cheeringRepository.countByCheeredMemberIdAndCheeringMessage(commonMemberId, CheeringMessage.CHEERING3);
        int cheering4Count = cheeringRepository.countByCheeredMemberIdAndCheeringMessage(commonMemberId, CheeringMessage.CHEERING4);
        int cheering5Count = cheeringRepository.countByCheeredMemberIdAndCheeringMessage(commonMemberId, CheeringMessage.CHEERING5);
        int cheering6Count = cheeringRepository.countByCheeredMemberIdAndCheeringMessage(commonMemberId, CheeringMessage.CHEERING6);

        commonMemberProfileDto.setCheering1Count(cheering1Count);
        commonMemberProfileDto.setCheering2Count(cheering2Count);
        commonMemberProfileDto.setCheering3Count(cheering3Count);
        commonMemberProfileDto.setCheering4Count(cheering4Count);
        commonMemberProfileDto.setCheering5Count(cheering5Count);
        commonMemberProfileDto.setCheering6Count(cheering6Count);
        commonMemberProfileDto.setTotalCheeringCount(cheering1Count + cheering2Count + cheering3Count + cheering4Count + cheering5Count + cheering6Count);
        commonMemberProfileDto.setIsCheering1(cheeringRepository.findByCheeringMemberIdAndCheeredMemberIdAndCheeringMessage(myId, commonMemberId, CheeringMessage.CHEERING1).isPresent());
        commonMemberProfileDto.setIsCheering2(cheeringRepository.findByCheeringMemberIdAndCheeredMemberIdAndCheeringMessage(myId, commonMemberId, CheeringMessage.CHEERING2).isPresent());
        commonMemberProfileDto.setIsCheering3(cheeringRepository.findByCheeringMemberIdAndCheeredMemberIdAndCheeringMessage(myId, commonMemberId, CheeringMessage.CHEERING3).isPresent());
        commonMemberProfileDto.setIsCheering4(cheeringRepository.findByCheeringMemberIdAndCheeredMemberIdAndCheeringMessage(myId, commonMemberId, CheeringMessage.CHEERING4).isPresent());
        commonMemberProfileDto.setIsCheering5(cheeringRepository.findByCheeringMemberIdAndCheeredMemberIdAndCheeringMessage(myId, commonMemberId, CheeringMessage.CHEERING5).isPresent());
        commonMemberProfileDto.setIsCheering6(cheeringRepository.findByCheeringMemberIdAndCheeredMemberIdAndCheeringMessage(myId, commonMemberId, CheeringMessage.CHEERING6).isPresent());

        return commonMemberProfileDto;
    }
}
