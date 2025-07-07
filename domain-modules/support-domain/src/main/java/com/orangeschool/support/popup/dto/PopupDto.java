package com.orangeschool.support.popup.dto;

import com.orangeschool.common.dto.response.CommonDto;
import com.orangeschool.support.popup.entity.Popup;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PopupDto extends CommonDto {

    private String title;
    private String originFileName;
    private String serverFileName;
    private String fileUrl;
    private String link;
    private Boolean isActive;
    private Long viewCount;

    public static PopupDto create(Popup popup) {

        PopupDto popupDto = PopupDto.builder()
                .title(popup.getTitle())
                .originFileName(popup.getOriginFileName())
                .serverFileName(popup.getServerFileName())
                .fileUrl(popup.getFileUrl())
                .link(popup.getLink())
                .isActive(popup.getIsActive())
                .viewCount(popup.getViewCount())
                .build();

        popupDto.setCreatedAt(popup.getCreatedAt());
        popupDto.setUpdatedAt(popup.getUpdatedAt());
        popupDto.setId(popup.getId());

        return popupDto;
    }
}
