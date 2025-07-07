package com.orangeschool.support.popup;

import com.orangeschool.common.dto.request.IdListDto;
import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.common.dto.request.UpdateActivationDto;
import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.utils.FileManagement;
import com.orangeschool.support.popup.dto.CreatePopupDto;
import com.orangeschool.support.popup.dto.PopupDto;
import com.orangeschool.support.popup.dto.UpdatePopupDto;
import com.orangeschool.support.popup.entity.Popup;
import com.orangeschool.support.popup.repository.PopupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PopupService {

    private final PopupRepository popupRepository;
    private final FileManagement fileManagement;

    @Transactional
    public void create(CreatePopupDto createPopupDto, MultipartFile file) throws Exception {

        Popup popup = Popup.builder()
                .title(createPopupDto.getTitle())
                .link(createPopupDto.getLink())
                .isActive(true)
                .viewCount(0L)
                .build();

        if (file != null) {
            String serverFileName = fileManagement.createServerFileName(file);
            String fileUrl = fileManagement.save(file, serverFileName);
            popup.setFile(file.getOriginalFilename(), serverFileName, fileUrl);
        }

        popupRepository.save(popup);
    }

    @Transactional(readOnly = true)
    public Page<PopupDto> get(Pageable pageable, KeywordSearchDto keywordSearchDto) throws Exception {
        return popupRepository.search(pageable, keywordSearchDto);
    }

    @Transactional(readOnly = true)
    public List<PopupDto> getToUser() throws Exception {
        return popupRepository.searchToUser();
    }

    @Transactional
    public PopupDto getById(Long popupId, boolean isApp) throws Exception {

        Optional<Popup> popupOptional = popupRepository.findById(popupId);

        if (popupOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Popup popup = popupOptional.get();

        if(isApp) {
            popup.updateViewCount();
            popupRepository.save(popup);
        }

        PopupDto popupDto = PopupDto.create(popup);
        return popupDto;
    }

    @Transactional
    public void put(Long popupId, UpdatePopupDto updatePopupDto, MultipartFile file) throws Exception {

        Optional<Popup> popupOptional = popupRepository.findById(popupId);

        if (popupOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Popup popup = popupOptional.get();

        popup.update(
                updatePopupDto.getTitle(),
                updatePopupDto.getLink());

        if (file != null) {
            fileManagement.delete(popup.getServerFileName());
            String serverFileName = fileManagement.createServerFileName(file);
            String fileUrl = fileManagement.save(file, serverFileName);
            popup.setFile(file.getOriginalFilename(), serverFileName, fileUrl);
        }

        popupRepository.save(popup);
    }

    @Transactional
    public void putActivation(Long popupId, UpdateActivationDto updateActivationDto) throws Exception {

        Optional<Popup> popupOptional = popupRepository.findById(popupId);

        if (popupOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Popup popup = popupOptional.get();
        popup.updateActivation(updateActivationDto.getActivation());

        popupRepository.save(popup);
    }

    @Transactional
    public void delete(Long popupId) throws Exception {
        
        Optional<Popup> popupOptional = popupRepository.findById(popupId);

        if (popupOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Popup popup = popupOptional.get();

        if (popup.getServerFileName() != null || !popup.getServerFileName().isEmpty()) {
            fileManagement.delete(popup.getServerFileName());
        }
        
        popupRepository.deleteById(popupId);
    }

    @Transactional
    public void deleteAll(IdListDto idListDto) throws Exception {

        Iterable<Popup> popups = popupRepository.findAllById(idListDto.getIdList());
        for (Popup popup : popups) {
            if (popup.getServerFileName() != null || !popup.getServerFileName().isEmpty()) {
                fileManagement.delete(popup.getServerFileName());
            }
        }

        popupRepository.deleteAllById(idListDto.getIdList());
    }
}
