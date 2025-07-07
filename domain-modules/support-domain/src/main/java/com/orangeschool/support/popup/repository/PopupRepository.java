package com.orangeschool.support.popup.repository;

import com.orangeschool.support.popup.entity.Popup;
import com.orangeschool.support.popup.repository.PopupRepositoryCustom;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PopupRepository extends PagingAndSortingRepository<Popup, Long>, PopupRepositoryCustom {

}
