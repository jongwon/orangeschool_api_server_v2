package com.orangeschool.support.alarm;

import com.orangeschool.common.dto.request.IdListDto;
import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.support.alarm.dto.AlarmDto;
import com.orangeschool.support.alarm.dto.AlarmFilterDto;
import com.orangeschool.support.alarm.dto.CreateAlarmDto;
import com.orangeschool.support.alarm.entity.Alarm;
import com.orangeschool.support.alarm.repository.AlarmRepository;
import com.orangeschool.support.memberAlarm.MemberAlarmService;
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
