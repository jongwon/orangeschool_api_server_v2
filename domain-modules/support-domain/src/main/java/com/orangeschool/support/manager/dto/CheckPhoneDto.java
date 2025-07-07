package com.orangeschool.support.manager.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class CheckPhoneDto {

    @NotBlank
    private String account;

    @NotBlank
    private String authNumber;
}
