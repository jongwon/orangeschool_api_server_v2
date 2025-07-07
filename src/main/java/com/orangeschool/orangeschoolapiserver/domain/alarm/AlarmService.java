package com.orangeschool.orangeschoolapiserver.domain.alarm;

import com.orangeschool.orangeschoolapiserver.common.dto.request.IdListDto;
import com.orangeschool.orangeschoolapiserver.common.response.CustomException;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.domain.alarm.dto.AlarmDto;
import com.orangeschool.orangeschoolapiserver.domain.alarm.dto.AlarmFilterDto;
import com.orangeschool.orangeschoolapiserver.domain.alarm.dto.CreateAlarmDto;
import com.orangeschool.orangeschoolapiserver.domain.alarm.entity.Alarm;
import com.orangeschool.orangeschoolapiserver.domain.alarm.repository.AlarmRepository;
import com.orangeschool.orangeschoolapiserver.domain.memberAlarm.MemberAlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final MemberAlarmService memberAlarmService;

    @Transactional
    public void create(CreateAlarmDto createAlarmDto) throws Exception {

        Alarm alarm = Alarm.builder()
                .title(createAlarmDto.getTitle())
                .content(createAlarmDto.getContent())
                .alarmType(createAlarmDto.getAlarmType())
                .alarmMemberType(createAlarmDto.getAlarmMemberType())
                .build();

        alarmRepository.save(alarm);
        memberAlarmService.create(alarm);
    }

    @Transactional(readOnly = true)
    public Page<AlarmDto> get(Pageable pageable, AlarmFilterDto alarmFilterDto) throws Exception {
        return alarmRepository.search(pageable, alarmFilterDto);
    }

    @Transactional(readOnly = true)
    public AlarmDto getById(Long alarmId) throws Exception {

        Optional<Alarm> alarmOptional = alarmRepository.findById(alarmId);

        if (!alarmOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        return AlarmDto.create(alarmOptional.get());
    }

    @Transactional
    public void delete(Long alarmId) throws Exception {

        Optional<Alarm> alarmOptional = alarmRepository.findById(alarmId);

        if (!alarmOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        alarmRepository.deleteById(alarmId);
    }

    @Transactional
    public void deleteAll(IdListDto idListDto) throws Exception {
        alarmRepository.deleteAllById(idListDto.getIdList());
    }
}
