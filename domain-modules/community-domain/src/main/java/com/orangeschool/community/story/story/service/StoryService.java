package com.orangeschool.community.story.story;


import com.orangeschool.common.dto.request.IdListDto;
import com.orangeschool.common.dto.request.UpdateActivationDto;
import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.utils.FileManagement;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.member.api.MemberInfoProvider;
import com.orangeschool.community.story.story.dto.CreateStoryDto;
import com.orangeschool.community.story.story.dto.StoryDto;
import com.orangeschool.community.story.story.dto.StorySearchDto;
import com.orangeschool.community.story.story.dto.UpdateStoryDto;
import com.orangeschool.community.story.story.entity.Story;
import com.orangeschool.community.story.story.entity.StoryImage;
import com.orangeschool.community.story.story.repository.StoryImageRepository;
import com.orangeschool.community.story.story.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StoryService {


    private final StoryRepository storyRepository;
    private final StoryImageRepository storyImageRepository;
    private final FileManagement fileManagement;
    private final MemberInfoProvider memberInfoProvider;

    @Transactional
    public void create(Long commonMemberId, CreateStoryDto createStoryDto, List<MultipartFile> files) throws Exception {

        Optional<CommonMember> commonMemberOptional = memberInfoProvider.getMemberInfo(commonMemberId);

        if (!commonMemberOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Story story = Story.builder()
                .commonMember(commonMemberOptional.get())
                .title(createStoryDto.getTitle())
                .content(createStoryDto.getContent())
                .regionNameTag(createStoryDto.getRegionNameTag())
                .regionCodeTag(createStoryDto.getRegionCodeTag())
                .isActive(true)
                .viewCount(0L)
                .todayViewCount(0L)
                .build();

        storyRepository.save(story);

        if (files != null) {
            List<StoryImage> storyImages = new ArrayList<>();

            for (MultipartFile file : files) {
                String serverFileName = fileManagement.createServerFileName(file);
                String fileUrl = fileManagement.save(file, serverFileName);

                storyImages.add(
                        StoryImage.builder()
                                .story(story)
                                .imageUrl(fileUrl)
                                .originFileName(file.getOriginalFilename())
                                .serverFileName(serverFileName)
                                .build()
                );
            }
            storyImageRepository.saveAll(storyImages);
        }
    }

    @Transactional(readOnly = true)
    public Page<StoryDto> getToUser(Pageable pageable, StorySearchDto storySearchDto, Long commonMemberId) throws Exception {
        Page<StoryDto> storyDtoPage = storyRepository.searchToUser(pageable, storySearchDto, commonMemberId);

        if (!storySearchDto.getIsSearch() && pageable.getPageNumber() == 0) {
            Story story = storyRepository.findFirstByOrderByTodayViewCountDesc(storySearchDto, commonMemberId);

            StoryDto storyDto = StoryDto.create(story, commonMemberId);
            storyDto.setPopular(true);

            List<StoryDto> storyDtoList = new ArrayList<>();
            storyDtoList.add(storyDto);

            storyDtoList.addAll(storyDtoPage.getContent().stream().filter(storyDto1 -> !Objects.equals(storyDto1.getId(), storyDto.getId())).collect(Collectors.toList()));
            storyDtoPage = new PageImpl<>(storyDtoList, pageable, storyDtoPage.getTotalElements());
        }

        return storyDtoPage;
    }

    @Transactional(readOnly = true)
    public Page<StoryDto> get(Pageable pageable, StorySearchDto storySearchDto) throws Exception {
        return storyRepository.search(pageable, storySearchDto);
    }

    @Transactional
    public StoryDto getByIdToUser(Long storyId, Long commonMemberId, StorySearchDto storySearchDto) throws Exception {

        Optional<Story> storyOptional = storyRepository.findById(storyId);

        if (storyOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Story story = storyOptional.get();

        if (storySearchDto.getIsFirst()) {
            story.updateViewCount();
        }

        storyRepository.save(story);

        return StoryDto.create(story, commonMemberId);
    }

    @Transactional(readOnly = true)
    public StoryDto getById(Long storyId) throws Exception {

        Optional<Story> storyOptional = storyRepository.findById(storyId);

        if (storyOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        return StoryDto.create(storyOptional.get());
    }

    @Transactional
    public void put(Long storyId, UpdateStoryDto updateStoryDto, List<MultipartFile> files) throws Exception {

        Optional<Story> storyOptional = storyRepository.findById(storyId);

        if (storyOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Story story = storyOptional.get();

        story.update(
                updateStoryDto.getTitle(),
                updateStoryDto.getContent(),
                updateStoryDto.getRegionNameTag(),
                updateStoryDto.getRegionCodeTag()
        );

        List<Long> deleteFileIds = updateStoryDto.getDeleteFileIds();

        if (deleteFileIds != null && deleteFileIds.size() > 0) {
            List<Long> deleteIdList = new ArrayList<>();
            for (Long fileId : deleteFileIds) {
                fileManagement.delete(storyImageRepository.findById(fileId).get().getServerFileName());
                deleteIdList.add(fileId);
            }
            storyImageRepository.deleteByIdInQuery(deleteIdList);
        }


        if (files != null) {
            List<StoryImage> storyImages = new ArrayList<>();

            for (MultipartFile file : files) {
                String serverFileName = fileManagement.createServerFileName(file);
                String fileUrl = fileManagement.save(file, serverFileName);

                storyImages.add(
                        StoryImage.builder()
                                .story(story)
                                .imageUrl(fileUrl)
                                .originFileName(file.getOriginalFilename())
                                .serverFileName(serverFileName)
                                .build()
                );
            }
            storyImageRepository.saveAll(storyImages);
        }

        storyRepository.save(story);
    }

    @Transactional
    public void putActivation(Long storyId, UpdateActivationDto updateActivationDto) throws Exception {

        Optional<Story> storyOptional = storyRepository.findById(storyId);

        if (storyOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Story story = storyOptional.get();
        story.updateActivation(updateActivationDto.getActivation());

        storyRepository.save(story);
    }

    @Transactional
    public void delete(Long storyId) throws Exception {
        storyRepository.deleteById(storyId);
    }

    @Transactional
    public void deleteAll(IdListDto idListDto) throws Exception {
        storyRepository.deleteAllById(idListDto.getIdList());
    }
}
