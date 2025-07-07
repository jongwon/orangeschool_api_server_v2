package com.orangeschool.education.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 학원 정보 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademyInfo {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phoneNumber;
    private String code;
    private boolean isActive;
    private LocalDateTime createdAt;
}