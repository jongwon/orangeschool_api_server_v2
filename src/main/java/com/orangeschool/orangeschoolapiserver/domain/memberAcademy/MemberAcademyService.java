package com.orangeschool.orangeschoolapiserver.domain.memberAcademy;

import com.orangeschool.orangeschoolapiserver.domain.academy.dto.AcademyDto;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.CommonMemberDto;
import com.orangeschool.orangeschoolapiserver.domain.memberAcademy.repository.MemberAcademyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberAcademyService {

    private final MemberAcademyRepository memberAcademyRepository;

//    @Transactional
//    public void create(CreateMemberAcademyDto createMemberAcademyDto) throws Exception {
//
//        Optional<MemberAcademy> memberAcademyOptional = memberAcademyRepository.findByNumber(createMemberAcademyDto.getNumber());
//
//        if(memberAcademyOptional.isPresent()) {
//            throw new CustomException(ResponseCode.BAD_REQUEST);
//        }
//
//        MemberAcademy memberAcademy = MemberAcademy.builder()
//                .memberAcademyType(createMemberAcademyDto.getMemberAcademyType())
//                .title(createMemberAcademyDto.getTitle())
//                .content(createMemberAcademyDto.getContent())
//                .link(createMemberAcademyDto.getLink())
//                .build();
//
//        memberAcademyRepository.save(memberAcademy);
//    }

    @Transactional(readOnly = true)
    public Page<AcademyDto> getByCommonMember(Pageable pageable, Long commonMemberId) throws Exception {
        return memberAcademyRepository.searchByCommonMember(pageable, commonMemberId);
    }

    @Transactional(readOnly = true)
    public Page<CommonMemberDto> getByAcademy(Pageable pageable, Long academyId) throws Exception {
        return memberAcademyRepository.searchByAcademy(pageable, academyId);
    }

//    @Transactional
//    public void put(Long memberAcademyId, UpdateMemberAcademyDto updateMemberAcademyDto) throws Exception {
//
//        Optional<MemberAcademy> memberAcademyOptional = memberAcademyRepository.findById(memberAcademyId);
//
//        if (memberAcademyOptional.isEmpty()) {
//            throw new CustomException(ResponseCode.NOT_FOUND);
//        }
//
//        Optional<MemberAcademy> memberAcademyOptional2 = memberAcademyRepository.findByNumber(updateMemberAcademyDto.getNumber());
//
//        if(memberAcademyOptional2.isPresent()) {
//            throw new CustomException(ResponseCode.BAD_REQUEST);
//        }
//
//        MemberAcademy memberAcademy = memberAcademyOptional.get();
//        memberAcademy.update(
//                updateMemberAcademyDto.getNumber(),
//                updateMemberAcademyDto.getMemberAcademyType(),
//                updateMemberAcademyDto.getTitle(),
//                updateMemberAcademyDto.getContent(),
//                updateMemberAcademyDto.getLink()
//        );
//
//        memberAcademyRepository.save(memberAcademy);
//    }

//    @Transactional
//    public void delete(Long memberAcademyId) throws Exception {
//        memberAcademyRepository.deleteById(memberAcademyId);
//    }
//
//    @Transactional
//    public void deleteAll(IdListDto idListDto) throws Exception {
//        memberAcademyRepository.deleteAllById(idListDto.getIdList());
//    }
}
