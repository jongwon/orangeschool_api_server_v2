package com.orangeschool.orangeschoolapiserver.domain.schedule.dto;


import com.orangeschool.orangeschoolapiserver.common.dto.response.CommonDto;
import com.orangeschool.orangeschoolapiserver.domain.schedule.entity.DayMessage;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DayMessageDto extends CommonDto {
    
    private LocalDate dayDate;
    private String parentMessage;
    private String childMessage;

    public static DayMessageDto create(DayMessage dayMessage) {

        DayMessageDto dayMessageDto = DayMessageDto.builder()
                .dayDate(dayMessage.getDayDate())
                .parentMessage(dayMessage.getParentMessage())
                .childMessage(dayMessage.getChildMessage())
                .build();

        dayMessageDto.setCreatedAt(dayMessage.getCreatedAt());
        dayMessageDto.setUpdatedAt(dayMessage.getUpdatedAt());
        dayMessageDto.setId(dayMessage.getId());

        return dayMessageDto;
    }
}
