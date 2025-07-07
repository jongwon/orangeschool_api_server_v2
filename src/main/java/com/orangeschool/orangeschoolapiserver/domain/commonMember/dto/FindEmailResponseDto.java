package com.orangeschool.orangeschoolapiserver.domain.commonMember.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class FindEmailResponseDto {

    private List<String> emailList;

    public static FindEmailResponseDto create(List<String> emailList){
        return FindEmailResponseDto.builder().emailList(emailList).build();
    }
}
