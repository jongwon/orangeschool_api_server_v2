package com.orangeschool.orangeschoolapiserver.domain.manager.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CheckPhoneDto {

    @NotBlank
    private String account;

    @NotBlank
    private String authNumber;
}
