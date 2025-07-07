package com.orangeschool.orangeschoolapiserver.domain.pick.pick.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class UpdatePickDto extends CreatePickDto{

    @Schema(description = "삭제할 이미지 아이디 목록")
    private List<Long> deleteFileIds;
}
