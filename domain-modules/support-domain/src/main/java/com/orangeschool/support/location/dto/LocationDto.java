package com.orangeschool.support.location.dto;

import com.orangeschool.common.dto.response.CommonDto;
import com.orangeschool.support.location.entity.Location;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationDto extends CommonDto {

    private long code;
    private String title;

    public static LocationDto create(Location location) {

        LocationDto locationDto = LocationDto.builder()
                .code(location.getCode())
                .title(location.getTitle())
                .build();

        locationDto.setCreatedAt(location.getCreatedAt());
        locationDto.setUpdatedAt(location.getUpdatedAt());
        locationDto.setId(location.getId());

        return locationDto;
    }
}
