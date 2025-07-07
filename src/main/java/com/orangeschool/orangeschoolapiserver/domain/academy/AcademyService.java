package com.orangeschool.orangeschoolapiserver.domain.academy;

import com.orangeschool.orangeschoolapiserver.common.dto.request.IdListDto;
import com.orangeschool.orangeschoolapiserver.common.response.CustomException;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.domain.academy.dto.AcademyDto;
import com.orangeschool.orangeschoolapiserver.domain.academy.dto.AcademySearchDto;
import com.orangeschool.orangeschoolapiserver.domain.academy.dto.CreateAcademyDto;
import com.orangeschool.orangeschoolapiserver.domain.academy.dto.UpdateAcademyDto;
import com.orangeschool.orangeschoolapiserver.domain.academy.entity.Academy;
import com.orangeschool.orangeschoolapiserver.domain.academy.repository.AcademyRepository;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.repository.CommonMemberRepository;
import com.orangeschool.orangeschoolapiserver.domain.manager.entity.Manager;
import com.orangeschool.orangeschoolapiserver.domain.manager.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AcademyService {

    private final AcademyRepository academyRepository;
    private final CommonMemberRepository commonMemberRepository;
    private final ManagerRepository managerRepository;

    @Transactional
    public Long createToUser(Long commonMemberId, CreateAcademyDto createAcademyDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND_MEMBER);
        }

        Academy academy = Academy.builder()
                .academyName(createAcademyDto.getAcademyName())
                .address(createAcademyDto.getAddress())
                .addressDetail(createAcademyDto.getAddressDetail())
                .isAdmin(false)
                .postUserEmail(commonMemberOptional.get().getEmail())
                .build();

        academyRepository.save(academy);
        return academy.getId();
    }

    @Transactional
    public void create(Long managerId, CreateAcademyDto createAcademyDto) throws Exception {

        Optional<Manager> managerOptional = managerRepository.findById(managerId);

        if (managerOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND_MEMBER);
        }

        Academy academy = Academy.builder()
                .academyName(createAcademyDto.getAcademyName())
                .address(createAcademyDto.getAddress())
                .addressDetail(createAcademyDto.getAddressDetail())
                .isAdmin(true)
                .postUserEmail(managerOptional.get().getEmail())
                .build();

        academyRepository.save(academy);
    }

    @Transactional(readOnly = true)
    public Page<AcademyDto> get(Pageable pageable, AcademySearchDto academySearchDto) throws Exception {
        return academyRepository.search(pageable, academySearchDto);
    }

    @Transactional(readOnly = true)
    public AcademyDto getById(Long academyId) throws Exception {

        Optional<Academy> academyOptional = academyRepository.findById(academyId);

        if (academyOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        return AcademyDto.create(academyOptional.get());
    }

    @Transactional
    public void put(Long academyId, UpdateAcademyDto updateAcademyDto) throws Exception {

        Optional<Academy> academyOptional = academyRepository.findById(academyId);

        if (academyOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Academy academy = academyOptional.get();
        academy.update(
                updateAcademyDto.getAcademyName(),
                updateAcademyDto.getAddress(),
                updateAcademyDto.getAddressDetail()
        );

        academyRepository.save(academy);
    }

    @Transactional
    public void delete(Long academyId) throws Exception {
        academyRepository.deleteById(academyId);
    }

    @Transactional
    public void deleteAll(IdListDto idListDto) throws Exception {
        academyRepository.deleteAllById(idListDto.getIdList());
    }
}
