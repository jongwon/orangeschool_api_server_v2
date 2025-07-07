package com.orangeschool.orangeschoolapiserver.domain.cheering.dto;

import com.orangeschool.orangeschoolapiserver.common.dto.response.CommonDto;
import com.orangeschool.orangeschoolapiserver.common.enums.MemberType;
import com.orangeschool.orangeschoolapiserver.domain.cheering.entity.Cheering;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.CommonMemberDto;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheeringDto extends CommonDto {

    private String name;
    private String nickName;

    private Long commonMemberId;
    private MemberType memberType;
    private String memberTypeTitle;
    private String fileUrl;

    public static CheeringDto create(Cheering cheering) {

        CheeringDto cheeringDto = CheeringDto.builder()
                .name(cheering.getCheeringMember().getName())
                .nickName(cheering.getCheeringMember().getNickName())
                .commonMemberId(cheering.getCheeringMember().getId())
                .memberType(cheering.getCheeringMember().getMemberType())
                .memberTypeTitle(cheering.getCheeringMember().getMemberType().getTitle())
                .fileUrl(cheering.getCheeringMember().getFileUrl())
                .build();

        cheeringDto.setCreatedAt(cheering.getCreatedAt());
        cheeringDto.setUpdatedAt(cheering.getUpdatedAt());
        cheeringDto.setId(cheering.getId());

        return cheeringDto;
    }
}
