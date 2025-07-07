package com.orangeschool.orangeschoolapiserver.domain.popup.repository;

import com.orangeschool.orangeschoolapiserver.domain.popup.entity.Popup;
import com.orangeschool.orangeschoolapiserver.domain.popup.repository.PopupRepositoryCustom;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PopupRepository extends PagingAndSortingRepository<Popup, Long>, PopupRepositoryCustom {

}
