package com.orangeschool.support.alarm.dto;

import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.common.enums.AlarmMemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AlarmFilterDto extends KeywordSearchDto {

    @Schema(description = "알림 회원 종류", example = "ALL", required = true)
    private AlarmMemberType alarmMemberType = AlarmMemberType.NONE;
}
