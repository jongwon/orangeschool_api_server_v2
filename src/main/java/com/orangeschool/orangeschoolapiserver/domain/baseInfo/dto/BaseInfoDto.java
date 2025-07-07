package com.orangeschool.orangeschoolapiserver.domain.baseInfo.dto;

import com.orangeschool.orangeschoolapiserver.common.dto.response.CommonDto;
import com.orangeschool.orangeschoolapiserver.domain.baseInfo.entity.BaseInfo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseInfoDto extends CommonDto {

    private String businessName;
    private String representative;
    private String businessNumber;
    private String address;
    private String phoneNumber;
    private String email;
    private String iosVersion;
    private String androidVersion;


    public static BaseInfoDto create(BaseInfo baseInfo) {

        BaseInfoDto baseInfoDto = BaseInfoDto.builder()
                .businessName(baseInfo.getBusinessName())
                .representative(baseInfo.getRepresentative())
                .businessNumber(baseInfo.getBusinessNumber())
                .address(baseInfo.getAddress())
                .phoneNumber(baseInfo.getPhoneNumber())
                .email(baseInfo.getEmail())
                .iosVersion(baseInfo.getIosVersion())
                .androidVersion(baseInfo.getAndroidVersion())
                .build();

        return baseInfoDto;
    }
}
