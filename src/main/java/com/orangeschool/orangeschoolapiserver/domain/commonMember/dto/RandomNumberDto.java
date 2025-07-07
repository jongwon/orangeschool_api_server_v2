package com.orangeschool.orangeschoolapiserver.domain.commonMember.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RandomNumberDto {

    private String randomNumber;

    public static RandomNumberDto create(String randomNum){
        return RandomNumberDto.builder().randomNumber(randomNum).build();
    }
}
