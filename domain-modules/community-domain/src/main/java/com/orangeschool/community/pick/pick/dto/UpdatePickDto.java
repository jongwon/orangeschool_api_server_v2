package com.orangeschool.community.pick.pick.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@EqualsAndHashCode(callSuper = false)@Data
public class UpdatePickDto extends CreatePickDto{

    @Schema(description = "삭제할 이미지 아이디 목록")
    private List<Long> deleteFileIds;
}
