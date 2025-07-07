package com.orangeschool.orangeschoolapiserver.domain.baseInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orangeschool.orangeschoolapiserver.domain.baseInfo.dto.BaseInfoDto;
import com.orangeschool.orangeschoolapiserver.domain.baseInfo.dto.UpdateBaseInfoDto;
import com.orangeschool.orangeschoolapiserver.domain.baseInfo.entity.BaseInfo;
import com.orangeschool.orangeschoolapiserver.domain.baseInfo.repository.BaseInfoRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BaseInfoService {
    private final BaseInfoRepository baseInfoRepository;

    @Transactional(readOnly = true)
    public BaseInfoDto get() throws Exception {

        Optional<BaseInfo> baseInfoOptional = baseInfoRepository.findById(1L);

        if (baseInfoOptional.isPresent()) {
            return BaseInfoDto.create(baseInfoOptional.get());
        }

        return BaseInfoDto.builder()
                .businessName("")
                .representative("")
                .businessNumber("")
                .address("")
                .phoneNumber("")
                .email("")
                .build();
    }

    @Transactional
    public void put(UpdateBaseInfoDto updateBaseInfoDto) throws Exception {

        Optional<BaseInfo> baseInfoOptional = baseInfoRepository.findById(1L);

        BaseInfo baseInfo;

        if (baseInfoOptional.isPresent()) {
            baseInfo = baseInfoOptional.get();
            baseInfo.update(updateBaseInfoDto.getBusinessName(), updateBaseInfoDto.getRepresentative(),
                    updateBaseInfoDto.getBusinessNumber(), updateBaseInfoDto.getAddress(),
                    updateBaseInfoDto.getPhoneNumber(), updateBaseInfoDto.getEmail());
        } else {
            baseInfo = BaseInfo.builder()
                    .businessName(updateBaseInfoDto.getBusinessName())
                    .representative(updateBaseInfoDto.getRepresentative())
                    .businessNumber(updateBaseInfoDto.getBusinessNumber())
                    .address(updateBaseInfoDto.getAddress())
                    .phoneNumber(updateBaseInfoDto.getPhoneNumber())
                    .email(updateBaseInfoDto.getEmail())
                    .build();
        }

        baseInfoRepository.save(baseInfo);
    }
}
