package com.orangeschool.member.commonMember.service;

import com.orangeschool.common.enums.ChallengeStatus;
import com.orangeschool.common.enums.CheeringMessage;
import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.education.api.service.ChallengeInfoProvider;
import com.orangeschool.community.api.service.CheeringInfoProvider;
import com.orangeschool.community.api.service.FollowInfoProvider;
import com.orangeschool.member.commonMember.dto.CommonMemberProfileDto;
import com.orangeschool.member.commonMember.dto.TownFriendFilterDto;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.member.commonMember.repository.CommonMemberRepository;
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

    private final FollowInfoProvider followInfoProvider;
    private final CheeringInfoProvider cheeringInfoProvider;
    private final CommonMemberRepository commonMemberRepository;
    private final ChallengeInfoProvider challengeInfoProvider;

    @Transactional(readOnly = true)
    public CommonMemberProfileDto getProfile(Long commonMemberId) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMemberProfileDto commonMemberProfileDto = CommonMemberProfileDto.create(commonMemberOptional.get());
        commonMemberProfileDto.setFollower(followInfoProvider.countFollowers(commonMemberId).intValue());
        commonMemberProfileDto.setFollowing(followInfoProvider.countFollowing(commonMemberId).intValue());

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

        Page<CommonMemberProfileDto> commonMemberProfileDtoPage = followInfoProvider.searchFollowers(followingMemberId, pageable);
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
        commonMemberProfileDto.setFollower(followInfoProvider.countFollowers(commonMemberId).intValue());
        commonMemberProfileDto.setFollowing(followInfoProvider.countFollowing(commonMemberId).intValue());
        var challengeInfo = challengeInfoProvider.getProgressChallengeByMemberId(commonMemberId);
        // TODO: ChallengeInfo 타입 정의 후 처리 필요
        commonMemberProfileDto.setMission(null);

        int cheering1Count = cheeringInfoProvider.countCheeringByType(commonMemberId, CheeringMessage.CHEERING1);
        int cheering2Count = cheeringInfoProvider.countCheeringByType(commonMemberId, CheeringMessage.CHEERING2);
        int cheering3Count = cheeringInfoProvider.countCheeringByType(commonMemberId, CheeringMessage.CHEERING3);
        int cheering4Count = cheeringInfoProvider.countCheeringByType(commonMemberId, CheeringMessage.CHEERING4);
        int cheering5Count = cheeringInfoProvider.countCheeringByType(commonMemberId, CheeringMessage.CHEERING5);
        int cheering6Count = cheeringInfoProvider.countCheeringByType(commonMemberId, CheeringMessage.CHEERING6);

        commonMemberProfileDto.setCheering1Count(cheering1Count);
        commonMemberProfileDto.setCheering2Count(cheering2Count);
        commonMemberProfileDto.setCheering3Count(cheering3Count);
        commonMemberProfileDto.setCheering4Count(cheering4Count);
        commonMemberProfileDto.setCheering5Count(cheering5Count);
        commonMemberProfileDto.setCheering6Count(cheering6Count);
        commonMemberProfileDto.setTotalCheeringCount(cheering1Count + cheering2Count + cheering3Count + cheering4Count + cheering5Count + cheering6Count);
        commonMemberProfileDto.setIsCheering1(cheeringInfoProvider.hasCheered(myId, commonMemberId, CheeringMessage.CHEERING1));
        commonMemberProfileDto.setIsCheering2(cheeringInfoProvider.hasCheered(myId, commonMemberId, CheeringMessage.CHEERING2));
        commonMemberProfileDto.setIsCheering3(cheeringInfoProvider.hasCheered(myId, commonMemberId, CheeringMessage.CHEERING3));
        commonMemberProfileDto.setIsCheering4(cheeringInfoProvider.hasCheered(myId, commonMemberId, CheeringMessage.CHEERING4));
        commonMemberProfileDto.setIsCheering5(cheeringInfoProvider.hasCheered(myId, commonMemberId, CheeringMessage.CHEERING5));
        commonMemberProfileDto.setIsCheering6(cheeringInfoProvider.hasCheered(myId, commonMemberId, CheeringMessage.CHEERING6));

        return commonMemberProfileDto;
    }
}
