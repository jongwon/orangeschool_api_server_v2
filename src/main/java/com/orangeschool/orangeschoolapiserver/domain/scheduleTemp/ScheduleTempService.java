package com.orangeschool.orangeschoolapiserver.domain.scheduleTemp;

import com.orangeschool.orangeschoolapiserver.common.enums.*;
import com.orangeschool.orangeschoolapiserver.common.response.CustomException;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.domain.academy.dto.AcademyDto;
import com.orangeschool.orangeschoolapiserver.domain.academy.entity.Academy;
import com.orangeschool.orangeschoolapiserver.domain.academy.repository.AcademyRepository;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.repository.CommonMemberRepository;
import com.orangeschool.orangeschoolapiserver.domain.schedule.ScheduleService;
import com.orangeschool.orangeschoolapiserver.domain.schedule.dto.*;
import com.orangeschool.orangeschoolapiserver.domain.schedule.entity.Schedule;
import com.orangeschool.orangeschoolapiserver.domain.schedule.repository.ScheduleRepository;
import com.orangeschool.orangeschoolapiserver.domain.scheduleTemp.dto.ScheduleTempDto;
import com.orangeschool.orangeschoolapiserver.domain.scheduleTemp.dto.UpdateScheduleConfirmDto;
import com.orangeschool.orangeschoolapiserver.domain.scheduleTemp.entity.ScheduleTemp;
import com.orangeschool.orangeschoolapiserver.domain.scheduleTemp.repository.ScheduleTempRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ScheduleTempService {

    private final ScheduleTempRepository scheduleTempRepository;
    private final CommonMemberRepository commonMemberRepository;
    private final ScheduleRepository scheduleRepository;
    private final AcademyRepository academyRepository;

    private final ScheduleService scheduleService;

    private static final Logger logger = LoggerFactory.getLogger(ScheduleTempService.class);

    @Transactional
    public Long create(CreateScheduleDto createScheduleDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(createScheduleDto.getCommonMemberId());

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND_MEMBER);
        }

        ScheduleTemp scheduleTemp = ScheduleTemp.builder()
                .commonMember(commonMemberOptional.get())
                .title(createScheduleDto.getTitle())
                .startDate(createScheduleDto.getStartDate())
                .startTime(createScheduleDto.getStartTime())
                .endDate(createScheduleDto.getEndDate())
                .endTime(createScheduleDto.getEndTime())
                .isAllDay(createScheduleDto.getIsAllDay())
                .scheduleType(createScheduleDto.getScheduleType())
                .academyId(createScheduleDto.getAcademyId() == null ? 0L : createScheduleDto.getAcademyId())
                .academyName(createScheduleDto.getAcademyName())
                .color(createScheduleDto.getColor())
                .memo(createScheduleDto.getMemo())
                .cycleType(createScheduleDto.getCycleType())
                .cycleDays(createScheduleDto.getCycleDays())
                .calendarCycle(createScheduleDto.getCalendarCycle())
                .cycleEndDate(createScheduleDto.getCycleEndDate())
                .usePay(createScheduleDto.getUsePay())
                .payDate(createScheduleDto.getPayDate())
                .payCycle(createScheduleDto.getPayCycle())
                .payCycleEndDate(createScheduleDto.getPayCycleEndDate())
                .amount(createScheduleDto.getAmount())
                .isSingle(createScheduleDto.getStartDate().equals(createScheduleDto.getEndDate()))
                .scheduleAlarmType(createScheduleDto.getScheduleAlarmType())
                .scheduleAlarmTypeForParent(createScheduleDto.getScheduleAlarmTypeForParent())
                .usePaymentAlarm(createScheduleDto.getUsePaymentAlarm())
                .payAlarmType(createScheduleDto.getPayAlarmType())
                .isImportant(createScheduleDto.getIsImportant())

                .confirmStatus(ConfirmStatus.WAIT)
                .scheduleUpdateType(ScheduleUpdateType.NONE)
                .scheduleRequestType(ScheduleRequestType.CREATE)
                .isPay(false)
                .build();

        Long scheduleTempId = scheduleTempRepository.save(scheduleTemp).getId();

        return scheduleTempId;
    }

    @Transactional
    public Long createPayment(CreateSchedulePaymentDto createSchedulePaymentDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(createSchedulePaymentDto.getCommonMemberId());

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND_MEMBER);
        }

        ScheduleTemp scheduleTemp = ScheduleTemp.builder()
                .commonMember(commonMemberOptional.get())
                .title(createSchedulePaymentDto.getTitle())
                .startDate(createSchedulePaymentDto.getPayDate())
                .endDate(createSchedulePaymentDto.getPayDate())
                .isAllDay(true)
                .scheduleType(ScheduleType.PAY)
                .color(createSchedulePaymentDto.getColor())
                .memo(createSchedulePaymentDto.getMemo())
                .payDate(createSchedulePaymentDto.getPayDate())
                .payCycle(createSchedulePaymentDto.getPayCycle())
                .payCycleEndDate(createSchedulePaymentDto.getPayCycleEndDate())
                .amount(createSchedulePaymentDto.getAmount())
                .isSingle(createSchedulePaymentDto.getStartDate().equals(createSchedulePaymentDto.getEndDate()))
                .scheduleAlarmType(ScheduleAlarmType.NONE)
                .scheduleAlarmTypeForParent(ScheduleAlarmType.NONE)
                .usePaymentAlarm(createSchedulePaymentDto.getUsePaymentAlarm())
                .payAlarmType(createSchedulePaymentDto.getPayAlarmType())

                .startTime(null)
                .endTime(null)
                .academyId(0L)
                .cycleType(null)
                .cycleDays(null)
                .calendarCycle(null)
                .cycleEndDate(null)
                .usePay(true)
                .isImportant(false)

                .confirmStatus(ConfirmStatus.WAIT)
                .scheduleUpdateType(ScheduleUpdateType.NONE)
                .scheduleRequestType(ScheduleRequestType.CREATE)
                .isPay(true)
                .build();

        Long scheduleTempId = scheduleTempRepository.save(scheduleTemp).getId();

        return scheduleTempId;
    }

    @Transactional
    public Long put(Long childId, Long scheduleId, UpdateScheduleDto updateScheduleDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(childId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND_MEMBER);
        }

        Optional<Schedule> scheduleOptional = scheduleRepository.findById(scheduleId);

        if (scheduleOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Schedule savedSchedule = scheduleOptional.get();

        if (savedSchedule.getCommonMember().getId() != childId) {
            throw new CustomException(ResponseCode.BAD_REQUEST);
        }

        ScheduleTemp scheduleTemp = ScheduleTemp.builder()
                .commonMember(commonMemberOptional.get())
                .title(updateScheduleDto.getTitle())
                .startDate(updateScheduleDto.getStartDate())
                .startTime(updateScheduleDto.getStartTime())
                .endDate(updateScheduleDto.getEndDate())
                .endTime(updateScheduleDto.getEndTime())
                .isAllDay(updateScheduleDto.getIsAllDay())
                .scheduleType(updateScheduleDto.getScheduleType())
                .academyId(updateScheduleDto.getAcademyId() == null ? 0L : updateScheduleDto.getAcademyId())
                .academyName(updateScheduleDto.getAcademyName())
                .color(updateScheduleDto.getColor())
                .memo(updateScheduleDto.getMemo())
                .cycleType(updateScheduleDto.getCycleType())
                .cycleDays(updateScheduleDto.getCycleDays())
                .calendarCycle(updateScheduleDto.getCalendarCycle())
                .cycleEndDate(updateScheduleDto.getCycleEndDate())
                .usePay(updateScheduleDto.getUsePay())
                .payDate(updateScheduleDto.getPayDate())
                .payCycle(updateScheduleDto.getPayCycle())
                .payCycleEndDate(updateScheduleDto.getPayCycleEndDate())
                .amount(updateScheduleDto.getAmount())
                .isSingle(updateScheduleDto.getStartDate().equals(updateScheduleDto.getEndDate()))
                .scheduleAlarmType(updateScheduleDto.getScheduleAlarmType())
                .scheduleAlarmTypeForParent(updateScheduleDto.getScheduleAlarmTypeForParent())
                .usePaymentAlarm(updateScheduleDto.getUsePaymentAlarm())
                .payAlarmType(updateScheduleDto.getPayAlarmType())
                .isImportant(updateScheduleDto.getIsImportant())

                .confirmStatus(ConfirmStatus.WAIT)
                .scheduleId(scheduleId)
                .scheduleUpdateType(updateScheduleDto.getScheduleUpdateType())
                .updateStandardDate(updateScheduleDto.getUpdateStandardDate())
                .scheduleRequestType(ScheduleRequestType.UPDATE)
                .isPay(false)
                .build();

        Long scheduleTempId = scheduleTempRepository.save(scheduleTemp).getId();

        return scheduleTempId;
    }

    @Transactional
    public Long putPayment(Long childId, Long scheduleId, UpdateSchedulePaymentDto updateSchedulePaymentDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(childId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND_MEMBER);
        }

        Optional<Schedule> scheduleOptional = scheduleRepository.findById(scheduleId);

        if (scheduleOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Schedule savedSchedule = scheduleOptional.get();

        if (savedSchedule.getCommonMember().getId() != childId) {
            throw new CustomException(ResponseCode.BAD_REQUEST);
        }

        ScheduleTemp scheduleTemp = ScheduleTemp.builder()
                .commonMember(commonMemberOptional.get())
                .title(updateSchedulePaymentDto.getTitle())
                .startDate(updateSchedulePaymentDto.getStartDate())
                .endDate(updateSchedulePaymentDto.getEndDate())
                .isAllDay(true)
                .scheduleType(ScheduleType.PAY)
                .color(updateSchedulePaymentDto.getColor())
                .memo(updateSchedulePaymentDto.getMemo())
                .payDate(updateSchedulePaymentDto.getPayDate())
                .payCycle(updateSchedulePaymentDto.getPayCycle())
                .payCycleEndDate(updateSchedulePaymentDto.getPayCycleEndDate())
                .amount(updateSchedulePaymentDto.getAmount())
                .isSingle(updateSchedulePaymentDto.getStartDate().equals(updateSchedulePaymentDto.getEndDate()))
                .scheduleAlarmType(ScheduleAlarmType.NONE)
                .scheduleAlarmTypeForParent(ScheduleAlarmType.NONE)
                .usePaymentAlarm(updateSchedulePaymentDto.getUsePaymentAlarm())
                .payAlarmType(updateSchedulePaymentDto.getPayAlarmType())

                .startTime(null)
                .endTime(null)
                .academyId(0L)
                .cycleType(null)
                .cycleDays(null)
                .calendarCycle(null)
                .cycleEndDate(null)
                .usePay(true)
                .isImportant(false)

                .confirmStatus(ConfirmStatus.WAIT)
                .scheduleId(scheduleId)
                .scheduleUpdateType(ScheduleUpdateType.NONE)
                .updateStandardDate(updateSchedulePaymentDto.getUpdateStandardDate())
                .scheduleRequestType(ScheduleRequestType.UPDATE)
                .isPay(true)
                .build();

        Long scheduleTempId = scheduleTempRepository.save(scheduleTemp).getId();

        return scheduleTempId;
    }

    @Transactional(readOnly = true)
    public Page<ScheduleTempDto> get(Pageable pageable, Long commonMemberId) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND_MEMBER);
        }

        if (commonMemberOptional.get().getMemberType() == MemberType.PARENT) {
            return scheduleTempRepository.searchByParentIdOrReferralCode(pageable, commonMemberId, commonMemberOptional.get().getReferralCode());
        } else {
            return scheduleTempRepository.searchByChildId(pageable, commonMemberId);
        }
    }

    @Transactional(readOnly = true)
    public ScheduleTempDto getById(Long scheduleTempId) throws Exception {

        Optional<ScheduleTemp> scheduleTempOptional = scheduleTempRepository.findById(scheduleTempId);

        if (scheduleTempOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        ScheduleTemp scheduleTemp = scheduleTempOptional.get();

        ScheduleTempDto scheduleTempDto = ScheduleTempDto.create(scheduleTemp);
        if (scheduleTemp.getAcademyId() != 0L) {
            Optional<Academy> academyOptional = academyRepository.findById(scheduleTemp.getAcademyId());
            academyOptional.ifPresent(academy -> scheduleTempDto.setAcademy(AcademyDto.create(academy)));
        }

        return scheduleTempDto;
    }

    @Transactional(readOnly = true)
    public Long getCount(Long commonMemberId) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND_MEMBER);
        }

        if (commonMemberOptional.get().getMemberType() == MemberType.PARENT) {
            return scheduleTempRepository.countByParentIdOrReferralCode(commonMemberId, commonMemberOptional.get().getReferralCode());
        } else {
            return scheduleTempRepository.countByChildId(commonMemberId);
        }
    }

    @Transactional
    public void updateScheduleConfirm(Long scheduleTempId, UpdateScheduleConfirmDto updateScheduleConfirmDto) throws Exception {

        Optional<ScheduleTemp> scheduleTempOptional = scheduleTempRepository.findById(scheduleTempId);

        if (scheduleTempOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        ScheduleTemp scheduleTemp = scheduleTempOptional.get();

        setConfirmStatus(scheduleTemp, updateScheduleConfirmDto);

        // 반려일 때는 상태만 변경
        if (updateScheduleConfirmDto.getConfirmStatus() == ConfirmStatus.REJECT) {
            return;
        }

        // 승인일 경우 데이터 반영
        if (scheduleTemp.getScheduleRequestType() == ScheduleRequestType.CREATE) {
            if (scheduleTemp.getIsPay()) {
                CreateSchedulePaymentDto createSchedulePaymentDto = CreateSchedulePaymentDto.builder()
                        .commonMemberId(scheduleTemp.getCommonMember().getId())
                        .title(updateScheduleConfirmDto.getTitle())
                        .startDate(updateScheduleConfirmDto.getStartDate())
                        .endDate(updateScheduleConfirmDto.getEndDate())
                        .color(updateScheduleConfirmDto.getColor())
                        .memo(updateScheduleConfirmDto.getMemo())
                        .payDate(updateScheduleConfirmDto.getPayDate())
                        .payCycle(updateScheduleConfirmDto.getPayCycle())
                        .payCycleEndDate(updateScheduleConfirmDto.getPayCycleEndDate())
                        .amount(updateScheduleConfirmDto.getAmount())
                        .usePaymentAlarm(updateScheduleConfirmDto.getUsePaymentAlarm())
                        .payAlarmType(updateScheduleConfirmDto.getPayAlarmType())
                        .build();

                scheduleService.createPayment(createSchedulePaymentDto);
            }

            if (!scheduleTemp.getIsPay()) {
                CreateScheduleDto createScheduleDto = CreateScheduleDto.builder()
                        .commonMemberId(scheduleTemp.getCommonMember().getId())
                        .title(updateScheduleConfirmDto.getTitle())
                        .startDate(updateScheduleConfirmDto.getStartDate())
                        .startTime(updateScheduleConfirmDto.getStartTime())
                        .endDate(updateScheduleConfirmDto.getEndDate())
                        .endTime(updateScheduleConfirmDto.getEndTime())
                        .isAllDay(updateScheduleConfirmDto.getIsAllDay())
                        .scheduleType(updateScheduleConfirmDto.getScheduleType())
                        .academyId(updateScheduleConfirmDto.getAcademyId())
                        .academyName(updateScheduleConfirmDto.getAcademyName())
                        .color(updateScheduleConfirmDto.getColor())
                        .memo(updateScheduleConfirmDto.getMemo())
                        .cycleType(updateScheduleConfirmDto.getCycleType())
                        .cycleDays(updateScheduleConfirmDto.getCycleDays())
                        .calendarCycle(updateScheduleConfirmDto.getCalendarCycle())
                        .cycleEndDate(updateScheduleConfirmDto.getCycleEndDate())
                        .scheduleAlarmType(updateScheduleConfirmDto.getScheduleAlarmType())
                        .scheduleAlarmTypeForParent(updateScheduleConfirmDto.getScheduleAlarmTypeForParent())
                        .usePay(updateScheduleConfirmDto.getUsePay())
                        .payDate(updateScheduleConfirmDto.getPayDate())
                        .payCycle(updateScheduleConfirmDto.getPayCycle())
                        .payCycleEndDate(updateScheduleConfirmDto.getPayCycleEndDate())
                        .amount(updateScheduleConfirmDto.getAmount())
                        .usePaymentAlarm(updateScheduleConfirmDto.getUsePaymentAlarm())
                        .payAlarmType(updateScheduleConfirmDto.getPayAlarmType())
                        .isImportant(updateScheduleConfirmDto.getIsImportant())
                        .build();

                scheduleService.create(createScheduleDto, null);
            }
        }

        if (scheduleTemp.getScheduleRequestType() == ScheduleRequestType.UPDATE) {
            if (scheduleTemp.getIsPay()) {
                UpdateSchedulePaymentDto updateSchedulePaymentDto = UpdateSchedulePaymentDto.builder()
                        .title(updateScheduleConfirmDto.getTitle())
                        .startDate(updateScheduleConfirmDto.getStartDate())
                        .endDate(updateScheduleConfirmDto.getEndDate())
                        .isAllDay(updateScheduleConfirmDto.getIsAllDay())
                        .scheduleType(updateScheduleConfirmDto.getScheduleType())
                        .color(updateScheduleConfirmDto.getColor())
                        .memo(updateScheduleConfirmDto.getMemo())
                        .payDate(updateScheduleConfirmDto.getPayDate())
                        .payCycle(updateScheduleConfirmDto.getPayCycle())
                        .payCycleEndDate(updateScheduleConfirmDto.getPayCycleEndDate())
                        .amount(updateScheduleConfirmDto.getAmount())
                        .usePaymentAlarm(updateScheduleConfirmDto.getUsePaymentAlarm())
                        .payAlarmType(updateScheduleConfirmDto.getPayAlarmType())

                        .scheduleUpdateType(scheduleTemp.getScheduleUpdateType())
                        .updateStandardDate(scheduleTemp.getUpdateStandardDate())
                        .build();

                scheduleService.putPayment(scheduleTemp.getScheduleId(), updateSchedulePaymentDto);
            }

            if (!scheduleTemp.getIsPay()) {
                UpdateScheduleDto updateScheduleDto = UpdateScheduleDto.builder()
                        .title(updateScheduleConfirmDto.getTitle())
                        .startDate(updateScheduleConfirmDto.getStartDate())
                        .startTime(updateScheduleConfirmDto.getStartTime())
                        .endDate(updateScheduleConfirmDto.getEndDate())
                        .endTime(updateScheduleConfirmDto.getEndTime())
                        .isAllDay(updateScheduleConfirmDto.getIsAllDay())
                        .scheduleType(updateScheduleConfirmDto.getScheduleType())
                        .academyId(updateScheduleConfirmDto.getAcademyId())
                        .academyName(updateScheduleConfirmDto.getAcademyName())
                        .color(updateScheduleConfirmDto.getColor())
                        .memo(updateScheduleConfirmDto.getMemo())
                        .cycleType(updateScheduleConfirmDto.getCycleType())
                        .cycleDays(updateScheduleConfirmDto.getCycleDays())
                        .calendarCycle(updateScheduleConfirmDto.getCalendarCycle())
                        .cycleEndDate(updateScheduleConfirmDto.getCycleEndDate())
                        .scheduleAlarmType(updateScheduleConfirmDto.getScheduleAlarmType())
                        .scheduleAlarmTypeForParent(updateScheduleConfirmDto.getScheduleAlarmTypeForParent())
                        .usePay(updateScheduleConfirmDto.getUsePay())
                        .payDate(updateScheduleConfirmDto.getPayDate())
                        .payCycle(updateScheduleConfirmDto.getPayCycle())
                        .payCycleEndDate(updateScheduleConfirmDto.getPayCycleEndDate())
                        .amount(updateScheduleConfirmDto.getAmount())
                        .usePaymentAlarm(updateScheduleConfirmDto.getUsePaymentAlarm())
                        .payAlarmType(updateScheduleConfirmDto.getPayAlarmType())
                        .scheduleUpdateType(scheduleTemp.getScheduleUpdateType())
                        .updateStandardDate(scheduleTemp.getUpdateStandardDate())
                        .isImportant(updateScheduleConfirmDto.getIsImportant())
                        .build();

                scheduleService.put(scheduleTemp.getScheduleId(), updateScheduleDto, null);
            }
        }
    }

    private void setConfirmStatus(ScheduleTemp scheduleTemp, UpdateScheduleConfirmDto updateScheduleConfirmDto) {
        scheduleTemp.updateConfirmStatus(updateScheduleConfirmDto.getConfirmStatus());
        scheduleTempRepository.save(scheduleTemp);
    }
}