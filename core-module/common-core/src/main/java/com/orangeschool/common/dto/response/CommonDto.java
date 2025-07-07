package com.orangeschool.common.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommonDto {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long id;
}
