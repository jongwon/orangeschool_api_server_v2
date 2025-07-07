package com.orangeschool.orangeschoolapiserver.domain.commonMember.dto;

import com.orangeschool.orangeschoolapiserver.common.dto.response.CommonDto;
import com.orangeschool.orangeschoolapiserver.common.enums.JoinType;
import com.orangeschool.orangeschoolapiserver.common.enums.MemberType;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonMemberDto extends CommonDto {

    private JoinType joinType;
    private String joinTypeTitle;
    private MemberType memberType;
    private String memberTypeTitle;
    private String email;
    private String name;
    private String birth;
    private Integer gender;
    private String genderTitle;
    private String phoneNumber;
    private String originFileName;
    private String serverFileName;
    private String fileUrl;
    private String address;
    private String addressDetail;
    private Boolean agreeToSms;
    private Boolean agreeToService;
    private Boolean agreeToAd;
    private Boolean agreeToSchedule;
    private Boolean isActive;
    private String nickName;
    private String parentNickName;
    private String intro;
    private String schoolCode;
    private String schoolName;
    private String grade;
    private String schoolClass;
    private String classNumber;
    private long locationCode;
    private CommonMemberDto parentInfo;

    private String regionNameTag;
    private String regionCodeTag;
    private String myReferralCode;
    private String referralCode;
    private String referralCodeTemp;

    public static CommonMemberDto create(CommonMember commonMember) {

        CommonMemberDto commonMemberDto = CommonMemberDto.builder()
                .joinType(commonMember.getJoinType())
                .joinTypeTitle(commonMember.getJoinType().getTitle())
                .memberType(commonMember.getMemberType())
                .memberTypeTitle(commonMember.getMemberType().getTitle())
                .email(commonMember.getEmail())
                .name(commonMember.getName())
                .birth(commonMember.getBirth())
                .gender(commonMember.getGender())
                .genderTitle(commonMember.getGenderTitle())
                .phoneNumber(commonMember.getPhoneNumber())
                .originFileName(commonMember.getOriginFileName())
                .serverFileName(commonMember.getServerFileName())
                .fileUrl(commonMember.getFileUrl())
                .address(commonMember.getAddress())
                .addressDetail(commonMember.getAddressDetail())
                .agreeToSms(commonMember.getAgreeToSms())
                .agreeToService(commonMember.getAgreeToService())
                .agreeToAd(commonMember.getAgreeToAd())
                .agreeToSchedule(commonMember.getAgreeToSchedule())
                .isActive(commonMember.getIsActive())
                .nickName(commonMember.getNickName())
                .parentNickName(commonMember.getParentNickName())
                .intro(commonMember.getIntro())
                .schoolCode(commonMember.getSchoolCode())
                .schoolName(commonMember.getSchoolName())
                .grade(commonMember.getGrade())
                .schoolClass(commonMember.getSchoolClass())
                .classNumber(commonMember.getClassNumber())
                .locationCode(commonMember.getLocationCode())

                .regionNameTag(commonMember.getRegionNameTag())
                .regionCodeTag(commonMember.getRegionCodeTag())
                .myReferralCode(commonMember.getMyReferralCode())
                .referralCode(commonMember.getReferralCode())
                .referralCodeTemp(commonMember.getReferralCodeTemp())
                .build();

        commonMemberDto.setCreatedAt(commonMember.getCreatedAt());
        commonMemberDto.setUpdatedAt(commonMember.getUpdatedAt());
        commonMemberDto.setId(commonMember.getId());

        return commonMemberDto;
    }

    public void setParentInfo(CommonMemberDto parentInfo) {
        this.parentInfo = parentInfo;
    }
}
