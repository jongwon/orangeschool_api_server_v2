package com.orangeschool.community.pick.pick;

import com.orangeschool.common.dto.request.IdListDto;
import com.orangeschool.common.dto.request.UpdateActivationDto;
import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.utils.FileManagement;
import com.orangeschool.community.manager.entity.Manager;
import com.orangeschool.community.manager.repository.ManagerRepository;
import com.orangeschool.community.pick.pick.dto.CreatePickDto;
import com.orangeschool.community.pick.pick.dto.PickDto;
import com.orangeschool.community.pick.pick.dto.PickFilterDto;
import com.orangeschool.community.pick.pick.dto.UpdatePickDto;
import com.orangeschool.community.pick.pick.entity.Pick;
import com.orangeschool.community.pick.pick.entity.PickImage;
import com.orangeschool.community.pick.pick.repository.PickImageRepository;
import com.orangeschool.community.pick.pick.repository.PickRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PickService {

    private final PickRepository pickRepository;
    private final PickImageRepository pickImageRepository;
    private final FileManagement fileManagement;
    private final ManagerRepository managerRepository;

    @Transactional
    public void create(Long managerId, CreatePickDto createPickDto, List<MultipartFile> files) throws Exception {

        Optional<Manager> managerOptional = managerRepository.findById(managerId);

        if (managerOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<Pick> pickOptional = pickRepository.findByNumber(createPickDto.getNumber());

        if (pickOptional.isPresent()) {
            throw new CustomException(ResponseCode.BAD_REQUEST_EXIST_NUMBER);
        }

        Pick pick = Pick.builder()
                .number(createPickDto.getNumber())
                .pickType(createPickDto.getPickType())
                .title(createPickDto.getTitle())
                .content(createPickDto.getContent())
                .link(createPickDto.getLink())
                .writerEmail(managerOptional.get().getEmail())
                .previewContent(createPickDto.getPreviewContent())
                .linkBtnName(createPickDto.getLinkBtnName())
                .isActive(true)
                .viewCount(0L)
                .build();

        if (files != null) {
            List<PickImage> pickImages = new ArrayList<>();

            for (MultipartFile file : files) {
                String serverFileName = fileManagement.createServerFileName(file);
                String fileUrl = fileManagement.save(file, serverFileName);

                pickImages.add(
                        PickImage.builder()
                                .pick(pick)
                                .imageUrl(fileUrl)
                                .originFileName(file.getOriginalFilename())
                                .serverFileName(serverFileName)
                                .build()
                );
            }
            pickImageRepository.saveAll(pickImages);
        }

        pickRepository.save(pick);
    }

    @Transactional(readOnly = true)
    public Page<PickDto> getToUser(Pageable pageable, PickFilterDto pickFilterDto, Long commonMemberId) throws Exception {
        return pickRepository.searchToUser(pageable, pickFilterDto, commonMemberId);
    }

    @Transactional(readOnly = true)
    public Page<PickDto> get(Pageable pageable, PickFilterDto pickFilterDto) throws Exception {
        return pickRepository.search(pageable, pickFilterDto);
    }

    @Transactional
    public PickDto getByIdToPublic(Long pickId) throws Exception {

        Optional<Pick> pickOptional = pickRepository.findById(pickId);

        if (pickOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        return PickDto.create(pickOptional.get());
    }

    @Transactional
    public PickDto getByIdToUser(Long pickId, Long commonMemberId, PickFilterDto pickFilterDto) throws Exception {

        Optional<Pick> pickOptional = pickRepository.findById(pickId);

        if (pickOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Pick pick = pickOptional.get();

        if (pickFilterDto.getIsFirst()) {
            pick.updateViewCount();
        }

        pickRepository.save(pick);

        return PickDto.create(pick, commonMemberId);
    }

    @Transactional(readOnly = true)
    public PickDto getById(Long pickId) throws Exception {

        Optional<Pick> pickOptional = pickRepository.findById(pickId);

        if (pickOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        return PickDto.create(pickOptional.get());
    }

    @Transactional
    public void put(Long pickId, UpdatePickDto updatePickDto, List<MultipartFile> files) throws Exception {

        Optional<Pick> pickOptional = pickRepository.findById(pickId);

        if (pickOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Pick pick = pickOptional.get();

        if (pick.getNumber() != updatePickDto.getNumber()) {
            Optional<Pick> pickOptionalToNumber = pickRepository.findByNumber(updatePickDto.getNumber());

            if (pickOptionalToNumber.isPresent()) {
                throw new CustomException(ResponseCode.BAD_REQUEST);
            }
        }

        pick.update(
                updatePickDto.getNumber(),
                updatePickDto.getPickType(),
                updatePickDto.getTitle(),
                updatePickDto.getContent(),
                updatePickDto.getLink(),
                updatePickDto.getPreviewContent(),
                updatePickDto.getLinkBtnName()
        );

        List<Long> deleteFileIds = updatePickDto.getDeleteFileIds();

        if (deleteFileIds != null && deleteFileIds.size() > 0) {
            List<Long> deleteIdList = new ArrayList<>();
            for (Long fileId : deleteFileIds) {
                fileManagement.delete(pickImageRepository.findById(fileId).get().getServerFileName());
                deleteIdList.add(fileId);
            }
            pickImageRepository.deleteByIdInQuery(deleteIdList);
        }


        if (files != null) {
            List<PickImage> pickImages = new ArrayList<>();

            for (MultipartFile file : files) {
                String serverFileName = fileManagement.createServerFileName(file);
                String fileUrl = fileManagement.save(file, serverFileName);

                pickImages.add(
                        PickImage.builder()
                                .pick(pick)
                                .imageUrl(fileUrl)
                                .originFileName(file.getOriginalFilename())
                                .serverFileName(serverFileName)
                                .build()
                );
            }
            pickImageRepository.saveAll(pickImages);
        }

        pickRepository.save(pick);
    }

    @Transactional
    public void putActivation(Long pickId, UpdateActivationDto updateActivationDto) throws Exception {

        Optional<Pick> pickOptional = pickRepository.findById(pickId);

        if (pickOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Pick pick = pickOptional.get();
        pick.updateActivation(updateActivationDto.getActivation());

        pickRepository.save(pick);
    }

    @Transactional
    public void delete(Long pickId) throws Exception {
        pickRepository.deleteById(pickId);
    }

    @Transactional
    public void deleteAll(IdListDto idListDto) throws Exception {
        pickRepository.deleteAllById(idListDto.getIdList());
    }
}
