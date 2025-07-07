package com.orangeschool.support.banner;

import com.orangeschool.common.dto.request.IdListDto;
import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.common.dto.request.UpdateActivationDto;
import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.utils.FileManagement;
import com.orangeschool.support.banner.dto.BannerV2Dto;
import com.orangeschool.support.banner.dto.CreateBannerV2Dto;
import com.orangeschool.support.banner.dto.UpdateBannerV2Dto;
import com.orangeschool.support.banner.entity.BannerV2;
import com.orangeschool.support.banner.repository.BannerV2Repository;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.member.commonMember.repository.CommonMemberRepository;
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
public class BannerV2Service {

    private final BannerV2Repository bannerV2Repository;
    private final FileManagement fileManagement;
    private final CommonMemberRepository commonMemberRepository;

    @Transactional
    public void create(CreateBannerV2Dto createBannerV2Dto, MultipartFile file) throws Exception {

        BannerV2 bannerV2 = BannerV2.builder()
                .title(createBannerV2Dto.getTitle())
                .regionCodeTag(createBannerV2Dto.getRegionCodeTag())
                .regionNameTag(createBannerV2Dto.getRegionNameTag())
                .link(createBannerV2Dto.getLink())
                .isActive(true)
                .viewCount(0L)
                .build();

        if (file != null) {
            String serverFileName = fileManagement.createServerFileName(file);
            String fileUrl = fileManagement.save(file, serverFileName);
            bannerV2.setFile(file.getOriginalFilename(), serverFileName, fileUrl);
        }

        bannerV2Repository.save(bannerV2);
    }

    @Transactional(readOnly = true)
    public Page<BannerV2Dto> get(Pageable pageable, KeywordSearchDto keywordSearchDto) throws Exception {
        return bannerV2Repository.search(pageable, keywordSearchDto);
    }

    @Transactional(readOnly = true)
    public List<BannerV2Dto> getToUser(Long commonMemberId) throws Exception {
        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        return bannerV2Repository.searchToUser(commonMemberOptional.get().getLocationCode());
    }

    @Transactional
    public BannerV2Dto getById(Long bannerV2Id, boolean isApp) throws Exception {

        Optional<BannerV2> bannerV2Optional = bannerV2Repository.findById(bannerV2Id);

        if (bannerV2Optional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        BannerV2 bannerV2 = bannerV2Optional.get();

        if(isApp) {
            bannerV2.updateViewCount();
            bannerV2Repository.save(bannerV2);
        }

        BannerV2Dto bannerV2Dto = BannerV2Dto.create(bannerV2);

        return bannerV2Dto;
    }

    @Transactional
    public void put(Long bannerV2Id, UpdateBannerV2Dto updateBannerV2Dto, MultipartFile file) throws Exception {

        Optional<BannerV2> bannerV2Optional = bannerV2Repository.findById(bannerV2Id);

        if (bannerV2Optional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        BannerV2 bannerV2 = bannerV2Optional.get();

        bannerV2.update(
                updateBannerV2Dto.getTitle(),
                updateBannerV2Dto.getRegionCodeTag(),
                updateBannerV2Dto.getRegionNameTag(),
                updateBannerV2Dto.getLink(),
                updateBannerV2Dto.getActivation());

        if (file != null) {
            fileManagement.delete(bannerV2.getServerFileName());
            String serverFileName = fileManagement.createServerFileName(file);
            String fileUrl = fileManagement.save(file, serverFileName);
            bannerV2.setFile(file.getOriginalFilename(), serverFileName, fileUrl);
        }

        bannerV2Repository.save(bannerV2);
    }

    @Transactional
    public void putActivation(Long bannerV2Id, UpdateActivationDto updateActivationDto) throws Exception {

        Optional<BannerV2> bannerV2Optional = bannerV2Repository.findById(bannerV2Id);

        if (bannerV2Optional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        BannerV2 bannerV2 = bannerV2Optional.get();
        bannerV2.updateActivation(updateActivationDto.getActivation());

        bannerV2Repository.save(bannerV2);
    }

    @Transactional
    public void delete(Long bannerV2Id) throws Exception {

        Optional<BannerV2> bannerV2Optional = bannerV2Repository.findById(bannerV2Id);

        if (bannerV2Optional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        BannerV2 bannerV2 = bannerV2Optional.get();

        if (bannerV2.getServerFileName() != null || !bannerV2.getServerFileName().isEmpty()) {
            fileManagement.delete(bannerV2.getServerFileName());
        }

        bannerV2Repository.deleteById(bannerV2Id);
    }

    @Transactional
    public void deleteAll(IdListDto idListDto) throws Exception {

        Iterable<BannerV2> bannerV2s = bannerV2Repository.findAllById(idListDto.getIdList());
        for (BannerV2 bannerV2 : bannerV2s) {
            if (bannerV2.getServerFileName() != null || !bannerV2.getServerFileName().isEmpty()) {
                fileManagement.delete(bannerV2.getServerFileName());
            }
        }

        bannerV2Repository.deleteAllById(idListDto.getIdList());
    }
}
