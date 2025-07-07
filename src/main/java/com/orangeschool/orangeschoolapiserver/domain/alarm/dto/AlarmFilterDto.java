package com.orangeschool.orangeschoolapiserver.domain.alarm.dto;

import com.orangeschool.orangeschoolapiserver.common.dto.request.KeywordSearchDto;
import com.orangeschool.orangeschoolapiserver.common.enums.AlarmMemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AlarmFilterDto extends KeywordSearchDto {

    @Schema(description = "알림 회원 종류", example = "ALL", required = true)
    private AlarmMemberType alarmMemberType = AlarmMemberType.NONE;
}
