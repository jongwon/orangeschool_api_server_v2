package com.orangeschool.support.banner;

import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.common.utils.FileManagement;
import com.orangeschool.support.banner.dto.BannerDto;
import com.orangeschool.support.banner.dto.UpdateBannerDto;
import com.orangeschool.support.banner.entity.Banner;
import com.orangeschool.support.banner.repository.BannerRepository;
import com.orangeschool.location.entity.Location;
import com.orangeschool.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BannerService {

    private final BannerRepository bannerRepository;
    private final LocationRepository locationRepository;
    private final FileManagement fileManagement;

    @Transactional
    public BannerDto create(Long locationId) throws Exception {

        Optional<Location> locationOptional = locationRepository.findById(locationId);

        if (locationOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Banner banner = Banner.builder()
                .location(locationOptional.get())
                .originFileName("")
                .serverFileName("")
                .fileUrl("")
                .link("")
                .build();

        bannerRepository.save(banner);
        locationRepository.save(locationOptional.get());

        return BannerDto.create(banner);
    }

    @Transactional(readOnly = true)
    public List<BannerDto> get(Long locationId) throws Exception {
        return bannerRepository.findByLocationId(locationId).stream().map(BannerDto::create)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BannerDto> getToUser(Long locationCode) throws Exception {
        return bannerRepository.searchByLocationCode(locationCode).stream().map(BannerDto::create)
                .collect(Collectors.toList());
    }

    @Transactional
    public void put(Long bannerId, UpdateBannerDto updateBannerDto, MultipartFile file) throws Exception {

        Optional<Banner> bannerOptional = bannerRepository.findById(bannerId);

        if (bannerOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Banner banner = bannerOptional.get();
        banner.update(
                updateBannerDto.getLink());

        if (file != null) {
            if (banner.getServerFileName() != null || !banner.getServerFileName().isEmpty()) {
                fileManagement.delete(banner.getServerFileName());
            }

            String serverFileName = fileManagement.createServerFileName(file);
            String fileUrl = fileManagement.save(file, serverFileName);
            banner.setFile(file.getOriginalFilename(), serverFileName, fileUrl);
        }

        bannerRepository.save(banner);
        locationRepository.save(locationRepository.findById(banner.getLocation().getId()).get());
    }

    @Transactional
    public void delete(Long bannerId) throws Exception {

        Optional<Banner> bannerOptional = bannerRepository.findById(bannerId);

        if (bannerOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Banner banner = bannerOptional.get();

        if (banner.getServerFileName() != null || !banner.getServerFileName().isEmpty()) {
            fileManagement.delete(banner.getServerFileName());
        }

        bannerRepository.deleteById(bannerId);
    }
}
