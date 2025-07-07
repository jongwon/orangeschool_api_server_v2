package com.orangeschool.education.schoolSchedule;

import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.member.commonMember.repository.CommonMemberRepository;
import com.orangeschool.education.schoolSchedule.dto.CreateSchoolScheduleDto;
import com.orangeschool.education.schoolSchedule.dto.SchoolScheduleDto;
import com.orangeschool.education.schoolSchedule.entity.SchoolSchedule;
import com.orangeschool.education.schoolSchedule.repository.SchoolScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SchoolScheduleService {

    private final SchoolScheduleRepository schoolScheduleRepository;
    private final CommonMemberRepository commonMemberRepository;

    @Transactional
    public void create(CreateSchoolScheduleDto createSchoolScheduleDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(createSchoolScheduleDto.getCommonMemberId());

        if (!commonMemberOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember commonMember = commonMemberOptional.get();
        String keyStringValue = createSchoolScheduleDto.getKeyStringValue();

        Optional<SchoolSchedule> schoolScheduleOptional = schoolScheduleRepository.findByCommonMemberIdAndKeyStringValue(createSchoolScheduleDto.getCommonMemberId(), keyStringValue);

        if (schoolScheduleOptional.isPresent()) {
            SchoolSchedule schoolSchedule = schoolScheduleOptional.get();

            schoolSchedule.update(createSchoolScheduleDto.getIsImportant(), createSchoolScheduleDto.getColor());

            schoolScheduleRepository.save(schoolSchedule);
        } else {
            SchoolSchedule schoolSchedule = SchoolSchedule.builder()
                    .commonMember(commonMember)
                    .keyStringValue(createSchoolScheduleDto.getKeyStringValue())
                    .isImportant(createSchoolScheduleDto.getIsImportant())
                    .color(createSchoolScheduleDto.getColor())
                    .build();

            schoolScheduleRepository.save(schoolSchedule);
        }
    }

    @Transactional(readOnly = true)
    public List<SchoolScheduleDto> get(Long commonMemberId) throws Exception {
        return schoolScheduleRepository.findByCommonMemberId(commonMemberId).stream().map(SchoolScheduleDto::create).collect(Collectors.toList());
    }
}
