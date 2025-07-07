package com.orangeschool.support.popup.repository;

import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.support.popup.dto.PopupDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PopupRepositoryCustom {
    Page<PopupDto> search(Pageable pageable, KeywordSearchDto keywordSearchDto);
    List<PopupDto> searchToUser();
}
