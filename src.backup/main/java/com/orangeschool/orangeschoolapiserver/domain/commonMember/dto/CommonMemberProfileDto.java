package com.orangeschool.orangeschoolapiserver.domain.commonMember.dto;

import com.orangeschool.orangeschoolapiserver.common.dto.response.CommonDto;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CommonMemberProfileDto extends CommonDto {

    private String name;
    private String fileUrl;
    private String nickName;
    private int totalOrange;
    private int totalOrangeAll;
    private String intro;
    private int age;
    private Boolean isFollow;
    private Boolean isTop10;

    private int follower;
    private int following;
    private String mission;
    private int cheering1Count;
    private int cheering2Count;
    private int cheering3Count;
    private int cheering4Count;
    private int cheering5Count;
    private int cheering6Count;
    private int totalCheeringCount;
    private Boolean isCheering1;
    private Boolean isCheering2;
    private Boolean isCheering3;
    private Boolean isCheering4;
    private Boolean isCheering5;
    private Boolean isCheering6;

    public static CommonMemberProfileDto create(CommonMember commonMember) {

        CommonMemberProfileDto commonMemberDto = CommonMemberProfileDto.builder()
                .name(commonMember.getName())
                .fileUrl(commonMember.getFileUrl())
                .nickName(commonMember.getNickName())
                .totalOrange(commonMember.getTotalOrange())
                .totalOrangeAll(commonMember.getTotalOrangeAll())
                .intro(commonMember.getIntro())
                .age(getAge(commonMember.getBirth(), commonMember.getGender()))
                .isFollow(true)
                .build();

        commonMemberDto.setCreatedAt(commonMember.getCreatedAt());
        commonMemberDto.setUpdatedAt(commonMember.getUpdatedAt());
        commonMemberDto.setId(commonMember.getId());

        return commonMemberDto;
    }

    public static CommonMemberProfileDto create(Long commonMemberId, CommonMember commonMember) {

        CommonMemberProfileDto commonMemberDto = CommonMemberProfileDto.builder()
                .name(commonMember.getName())
                .fileUrl(commonMember.getFileUrl())
                .nickName(commonMember.getNickName())
                .totalOrange(commonMember.getTotalOrange())
                .totalOrangeAll(commonMember.getTotalOrangeAll())
                .intro(commonMember.getIntro())
                .age(getAge(commonMember.getBirth(), commonMember.getGender()))
                .isFollow(commonMember.getFollowers().stream().anyMatch(follower -> follower.getFollowingMember().getId() == commonMemberId))
                .build();

        commonMemberDto.setCreatedAt(commonMember.getCreatedAt());
        commonMemberDto.setUpdatedAt(commonMember.getUpdatedAt());
        commonMemberDto.setId(commonMember.getId());

        return commonMemberDto;
    }

    private static int getAge(String birth, int gender) {
        String[] parts = birth.split("/");
        if (parts.length == 3) {
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            // 나이 계산을 위해 연도 조정
//            if (gender == 1 || gender == 2) {
//                year += 1900; // 1900년대 출생
//            } else if (gender == 3 || gender == 4) {
//                year += 2000; // 2000년대 출생
//            }

            LocalDate dateOfBirth = LocalDate.of(year, month, day);

            // 현재 날짜를 가져옴
            LocalDate currentDate = LocalDate.now();

            // 나이 계산
            return currentDate.getYear() - dateOfBirth.getYear();
        }

        return 0;
    }
}
