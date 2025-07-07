package com.orangeschool.orangeschoolapiserver.domain.commonMember.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class UpdateSettingAlarmDto {

    @Schema(description = "서비스 알림 수신 동의", example = "true")
    private Boolean agreeToService= false;
    @Schema(description = "광고성 알림 수신 동의", example = "true")
    private Boolean agreeToAd= false;
    @Schema(description = "일정 알림 수신 동의", example = "true")
    private Boolean agreeToSchedule= false;
}