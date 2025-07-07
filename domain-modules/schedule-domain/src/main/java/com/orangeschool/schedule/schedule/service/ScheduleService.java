package com.orangeschool.schedule.schedule;

import com.google.firebase.messaging.Notification;
import com.orangeschool.common.enums.MemberType;
import com.orangeschool.common.enums.ScheduleAlarmType;
import com.orangeschool.common.enums.ScheduleType;
import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.education.academy.dto.AcademyDto;
import com.orangeschool.education.academy.entity.Academy;
import com.orangeschool.education.academy.repository.AcademyRepository;
import com.orangeschool.schedule.calendar.CalendarService;
import com.orangeschool.schedule.calendar.dto.CalendarSearchDto;
import com.orangeschool.schedule.calendar.entity.Calendar;
import com.orangeschool.schedule.calendar.repository.CalendarRepository;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.member.commonMember.repository.CommonMemberRepository;
import com.orangeschool.education.memberAcademy.entity.MemberAcademy;
import com.orangeschool.education.memberAcademy.repository.MemberAcademyRepository;
import com.orangeschool.support.api.MemberAlarmProvider;
import com.orangeschool.support.api.dto.AlarmNotificationRequest;
import com.orangeschool.schedule.schedule.dto.*;
import com.orangeschool.schedule.schedule.entity.DayMessage;
import com.orangeschool.schedule.schedule.entity.Schedule;
import com.orangeschool.schedule.schedule.entity.SleepInfo;
import com.orangeschool.schedule.schedule.entity.TodoList;
import com.orangeschool.schedule.schedule.repository.DayMessageRepository;
import com.orangeschool.schedule.schedule.repository.ScheduleRepository;
import com.orangeschool.schedule.schedule.repository.SleepInfoRepository;
import com.orangeschool.schedule.schedule.repository.TodoListRepository;
import com.orangeschool.schedule.scheduleAlarm.repository.ScheduleAlarmRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CommonMemberRepository commonMemberRepository;
    private final MemberAcademyRepository memberAcademyRepository;
    private final AcademyRepository academyRepository;
    private final CalendarService calendarService;
    private final CalendarRepository calendarRepository;
    private final DayMessageRepository dayMessageRepository;
    private final TodoListRepository todoListRepository;
    private final SleepInfoRepository sleepInfoRepository;
    private final ScheduleAlarmRepository scheduleAlarmRepository;
    private final MemberAlarmProvider memberAlarmProvider;

    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    @Transactional
    public Long create(CreateScheduleDto createScheduleDto, Long creatorId) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(createScheduleDto.getCommonMemberId());

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND_MEMBER);
        }

        // 학원 일정인 경우
        if (createScheduleDto.getAcademyId() != null && createScheduleDto.getAcademyId() != 0L) {
            Optional<Academy> academyOptional = academyRepository.findById(createScheduleDto.getAcademyId());

            if (academyOptional.isEmpty()) {
                throw new CustomException(ResponseCode.NOT_FOUND_ACADEMY);
            }

            MemberAcademy memberAcademy = MemberAcademy.builder()
                    .commonMember(commonMemberOptional.get())
                    .academy(academyOptional.get())
                    .build();

            memberAcademyRepository.save(memberAcademy);
        }

        Schedule schedule = Schedule.builder()
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
                .build();

        Long scheduleId = scheduleRepository.save(schedule).getId();

        // 달력 일정만들기
        calendarService.create(schedule, creatorId);

        return scheduleId;
    }

    @Transactional
    public Long createPayment(CreateSchedulePaymentDto createSchedulePaymentDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(createSchedulePaymentDto.getCommonMemberId());

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND_MEMBER);
        }

        Schedule schedule = Schedule.builder()
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
                .build();

        Long scheduleId = scheduleRepository.save(schedule).getId();

        // 달력 일정만들기
        calendarService.createPayment(schedule);

        return scheduleId;
    }

    @Transactional
    public void createDayMessage(Long commonMemberId, CreateDayMessageDto createDayMessageDto) throws Exception {

        Optional<CommonMember> childOptional = commonMemberRepository.findById(commonMemberId);

        if (childOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember child = childOptional.get();

        Optional<CommonMember> parentOptional = commonMemberRepository.findById(child.getParentId());

        if (parentOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember parent = parentOptional.get();

        LocalDate dayDate = createDayMessageDto.getDayDate();
        Optional<DayMessage> dayMessageOptional = dayMessageRepository.findByDayDateAndCommonMemberId(dayDate, commonMemberId);

        DayMessage dayMessage = dayMessageOptional.orElseGet(() -> DayMessage.builder()
                .commonMember(child)
                .dayDate(dayDate)
                .build());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일");
        String formattedDate = dayDate.format(formatter);
        String message = formattedDate + " 메모가 등록되었어요!";

        Notification notification = Notification.builder()
                .setTitle("메모알림")
                .setBody(message)
                .build();

        if (createDayMessageDto.getMemberType() == MemberType.PARENT) {
            dayMessage.updateParentMessage(createDayMessageDto.getParentMessage());
            memberAlarmProvider.createSystemAlarm(AlarmNotificationRequest.builder()
                    .memberId(child.getId())
                    .title("메모알림")
                    .content(message)
                    .notification(notification)
                    .build());
        } else {
            dayMessage.updateChildMessage(createDayMessageDto.getChildMessage());
            memberAlarmProvider.createSystemAlarm(AlarmNotificationRequest.builder()
                    .memberId(parent.getId())
                    .title("메모알림")
                    .content(message)
                    .notification(notification)
                    .build());
        }

        dayMessageRepository.save(dayMessage);
    }

    @Transactional
    public void createTodoList(Long commonMemberId, CreateTodoListDto createTodoListDto) throws Exception {

        Optional<CommonMember> childOptional = commonMemberRepository.findById(commonMemberId);

        if (childOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember child = childOptional.get();

        Optional<CommonMember> parentOptional = commonMemberRepository.findById(child.getParentId());

        if (parentOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember parent = parentOptional.get();

        LocalDate dayDate = createTodoListDto.getDayDate();
        Optional<TodoList> todoListOptional = todoListRepository.findByDayDateAndCommonMemberId(dayDate, commonMemberId);

        TodoList todoList = todoListOptional.orElseGet(() -> TodoList.builder()
                .commonMember(child)
                .dayDate(dayDate)
                .todoList(createTodoListDto.getTodoList())
                .build());

        todoList.updateTodoList(createTodoListDto.getTodoList());

        todoListRepository.save(todoList);
    }

    @Transactional
    public void createSleep(Long commonMemberId, CreateSleepDto createSleepDto) throws Exception {

        Optional<CommonMember> childOptional = commonMemberRepository.findById(commonMemberId);

        if (childOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember child = childOptional.get();

        Optional<SleepInfo> sleepInfoOptional = sleepInfoRepository.findByCommonMemberId(commonMemberId);

        SleepInfo sleepInfo = sleepInfoOptional.orElseGet(() -> SleepInfo.builder()
                .commonMember(child)
                .build());

        sleepInfo.update(createSleepDto.getWakeTime(), createSleepDto.getSleepTime());

        sleepInfoRepository.save(sleepInfo);
    }

    @Transactional(readOnly = true)
    public List<ScheduleDto> getWeekSchedules(CalendarSearchDto calendarSearchDto) throws Exception {

        return scheduleRepository.searchWeekSchedules(calendarSearchDto.getCommonMemberId(), calendarSearchDto.getWeekStartDate());
    }

    @Transactional(readOnly = true)
    public Page<ScheduleDto> getByCommonMemberId(Pageable pageable, Long commonMemberId) throws Exception {
        Page<ScheduleDto> scheduleDtoPage = scheduleRepository.searchByCommonMemberIdAndAcademy(pageable, commonMemberId);
        return scheduleDtoPage.map(scheduleDto -> {
            if (scheduleDto.getAcademyId() != 0L) {
                Optional<Academy> academyOptional = academyRepository.findById(scheduleDto.getAcademyId());
                academyOptional.ifPresent(academy -> scheduleDto.setAcademy(AcademyDto.create(academy)));
            }
            return scheduleDto;
        });
    }

    @Transactional(readOnly = true)
    public ScheduleDto getById(Long scheduleId) throws Exception {

        Optional<Schedule> scheduleOptional = scheduleRepository.findById(scheduleId);

        if (scheduleOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Schedule schedule = scheduleOptional.get();

        ScheduleDto scheduleDto = ScheduleDto.create(schedule);
        if (schedule.getAcademyId() != 0L) {
            Optional<Academy> academyOptional = academyRepository.findById(schedule.getAcademyId());
            academyOptional.ifPresent(academy -> scheduleDto.setAcademy(AcademyDto.create(academy)));
        }

        return scheduleDto;
    }

    @Transactional(readOnly = true)
    public DayMessageDto getDayMessage(Long commonMemberId, DayMessageSearchDto dayMessageSearchDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<DayMessage> dayMessageOptional = dayMessageRepository.findByDayDateAndCommonMemberId(dayMessageSearchDto.getDayDate(), commonMemberId);

        return dayMessageOptional.map(DayMessageDto::create).orElseGet(() -> DayMessageDto.builder().build());
    }

    @Transactional(readOnly = true)
    public TodoListDto getTodoList(Long commonMemberId, DayMessageSearchDto dayMessageSearchDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<TodoList> todoListOptional = todoListRepository.findByDayDateAndCommonMemberId(dayMessageSearchDto.getDayDate(), commonMemberId);

        return todoListOptional.map(TodoListDto::create).orElseGet(() -> TodoListDto.builder().build());
    }

    @Transactional(readOnly = true)
    public SleepInfoDto getSleep(Long commonMemberId) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Optional<SleepInfo> sleepInfoOptional = sleepInfoRepository.findByCommonMemberId(commonMemberId);

        return sleepInfoOptional.map(SleepInfoDto::create).orElseGet(() -> SleepInfoDto.builder().build());
    }

    @Transactional
    public void put(Long scheduleId, UpdateScheduleDto updateScheduleDto, Long creatorId) throws Exception {

        Optional<Schedule> scheduleOptional = scheduleRepository.findById(scheduleId);

        if (scheduleOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Schedule savedSchedule = scheduleOptional.get();
        CommonMember scheduleCommonMember = savedSchedule.getCommonMember();

        switch (updateScheduleDto.getScheduleUpdateType()) {
            case ALL:
                // 달력 모두 삭제
                calendarRepository.deleteByScheduleId(scheduleId);
//                scheduleAlarmRepository.deleteByScheduleId(scheduleId);
                memberAcademyRepository.deleteByAcademyIdAndCommonMemberId(savedSchedule.getAcademyId(), scheduleCommonMember.getId());

                // 학원 일정인 경우
                if (updateScheduleDto.getAcademyId() != null && updateScheduleDto.getAcademyId() != 0L) {
                    Optional<Academy> academyOptional = academyRepository.findById(updateScheduleDto.getAcademyId());

                    if (academyOptional.isEmpty()) {
                        throw new CustomException(ResponseCode.NOT_FOUND_ACADEMY);
                    }

                    MemberAcademy memberAcademy = MemberAcademy.builder()
                            .commonMember(scheduleCommonMember)
                            .academy(academyOptional.get())
                            .build();

                    memberAcademyRepository.save(memberAcademy);
                }

                savedSchedule.update(
                        updateScheduleDto.getTitle(),
                        updateScheduleDto.getStartDate(),
                        updateScheduleDto.getStartTime(),
                        updateScheduleDto.getEndDate(),
                        updateScheduleDto.getEndTime(),
                        updateScheduleDto.getIsAllDay(),
                        updateScheduleDto.getScheduleType(),
                        updateScheduleDto.getAcademyId() == null ? 0L : updateScheduleDto.getAcademyId(),
                        updateScheduleDto.getAcademyName(),
                        updateScheduleDto.getColor(),
                        updateScheduleDto.getMemo(),
                        updateScheduleDto.getCycleType(),
                        updateScheduleDto.getCycleDays(),
                        updateScheduleDto.getCalendarCycle(),
                        updateScheduleDto.getCycleEndDate(),
                        updateScheduleDto.getUsePay(),
                        updateScheduleDto.getPayDate(),
                        updateScheduleDto.getPayCycle(),
                        updateScheduleDto.getPayCycleEndDate(),
                        updateScheduleDto.getAmount(),
                        updateScheduleDto.getStartDate().equals(updateScheduleDto.getEndDate()),
                        updateScheduleDto.getScheduleAlarmType(),
                        updateScheduleDto.getScheduleAlarmTypeForParent(),
                        updateScheduleDto.getUsePaymentAlarm(),
                        updateScheduleDto.getPayAlarmType(),
                        updateScheduleDto.getIsImportant()
                );

                scheduleRepository.save(savedSchedule);

                // 달력 일정만들기
                calendarService.create(savedSchedule, creatorId);
                break;
            case CURRENT_ALL:
                // 수정 기준일자 이후 달력 삭제
                calendarRepository.deleteByScheduleIdAndStartDateGreaterThanEqual(savedSchedule.getId(), updateScheduleDto.getUpdateStandardDate());

                // 학원 일정인 경우
                if (updateScheduleDto.getAcademyId() != null && updateScheduleDto.getAcademyId() != 0L) {
                    Optional<Academy> academyOptional = academyRepository.findById(updateScheduleDto.getAcademyId());

                    if (academyOptional.isEmpty()) {
                        throw new CustomException(ResponseCode.NOT_FOUND_ACADEMY);
                    }

                    MemberAcademy memberAcademy = MemberAcademy.builder()
                            .commonMember(scheduleCommonMember)
                            .academy(academyOptional.get())
                            .build();

                    memberAcademyRepository.save(memberAcademy);
                }

                Schedule schedule = Schedule.builder()
                        .commonMember(scheduleCommonMember)
                        .title(updateScheduleDto.getTitle())
                        .startDate(updateScheduleDto.getUpdateStandardDate())
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
                        .build();

                scheduleRepository.save(schedule);

                // 달력 일정만들기
                calendarService.create(schedule, creatorId);
                break;
        }
    }

    @Transactional
    public void putPayment(Long scheduleId, UpdateSchedulePaymentDto updateSchedulePaymentDto) throws Exception {

        Optional<Schedule> scheduleOptional = scheduleRepository.findById(scheduleId);

        if (scheduleOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Schedule savedSchedule = scheduleOptional.get();
        CommonMember scheduleCommonMember = savedSchedule.getCommonMember();

        switch (updateSchedulePaymentDto.getScheduleUpdateType()) {
            case ALL:
                // 이전 달력 삭제
                calendarRepository.deleteByScheduleId(scheduleId);
//                scheduleAlarmRepository.deleteByScheduleId(scheduleId);

                savedSchedule.updatePayment(
                        updateSchedulePaymentDto.getTitle(),
                        updateSchedulePaymentDto.getStartDate(),
                        updateSchedulePaymentDto.getEndDate(),
                        updateSchedulePaymentDto.getIsAllDay(),
                        updateSchedulePaymentDto.getScheduleType(),
                        updateSchedulePaymentDto.getColor(),
                        updateSchedulePaymentDto.getMemo(),
                        updateSchedulePaymentDto.getPayDate(),
                        updateSchedulePaymentDto.getPayCycle(),
                        updateSchedulePaymentDto.getPayCycleEndDate(),
                        updateSchedulePaymentDto.getAmount(),
                        updateSchedulePaymentDto.getStartDate().equals(updateSchedulePaymentDto.getEndDate()),
                        updateSchedulePaymentDto.getUsePaymentAlarm(),
                        updateSchedulePaymentDto.getPayAlarmType()
                );

                scheduleRepository.save(savedSchedule);

                // 달력 일정만들기
                calendarService.createPayment(savedSchedule);
                break;
            case CURRENT_ALL:
                // 수정 기준일자 이후 달력 삭제
                calendarRepository.deleteByScheduleIdAndStartDateGreaterThanEqual(savedSchedule.getId(), updateSchedulePaymentDto.getUpdateStandardDate());

                Schedule schedule = Schedule.builder()
                        .commonMember(scheduleCommonMember)
                        .title(updateSchedulePaymentDto.getTitle())
                        .startDate(updateSchedulePaymentDto.getPayDate())
                        .endDate(updateSchedulePaymentDto.getPayDate())
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

                        .startTime(null)
                        .endTime(null)
                        .academyId(0L)
                        .cycleType(null)
                        .cycleDays(null)
                        .calendarCycle(null)
                        .cycleEndDate(null)
                        .usePay(true)
                        .isImportant(false)
                        .build();

                scheduleRepository.save(schedule);

                // 달력 일정만들기
                calendarService.createPayment(schedule);
                break;
        }
    }

    @Transactional
    public void deleteCalendar(Long calendarId) throws Exception {

        Optional<Calendar> calendarOptional = calendarRepository.findById(calendarId);

        if (calendarOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Calendar calendar = calendarOptional.get();

        int count = calendarRepository.countByScheduleId(calendar.getSchedule().getId());

        if (count == 1) {
            Schedule schedule = calendar.getSchedule();
            memberAcademyRepository.deleteByAcademyIdAndCommonMemberId(schedule.getAcademyId(), schedule.getCommonMember().getId());
            calendarRepository.deleteByScheduleId(schedule.getId());
//            scheduleAlarmRepository.deleteByScheduleId(schedule.getId());

            scheduleRepository.deleteById(schedule.getId());
        } else {
            scheduleAlarmRepository.deleteByCalendarId(calendarId);
            calendarRepository.deleteById(calendarId);
        }
    }

    @Transactional
    public void deleteAfterStandardDate(Long scheduleId, DeleteScheduleDto deleteScheduleDto) throws Exception {

        if(deleteScheduleDto.getDeleteStandardDate() != null) {
            Optional<Schedule> scheduleOptional = scheduleRepository.findById(scheduleId);

            if (scheduleOptional.isEmpty()) {
                throw new CustomException(ResponseCode.NOT_FOUND);
            }

            calendarRepository.deleteByScheduleIdAndStartDateGreaterThanEqual(scheduleId, deleteScheduleDto.getDeleteStandardDate());
        }
    }

    @Transactional
    public void deleteSchedule(Long scheduleId) throws Exception {

        Optional<Schedule> scheduleOptional = scheduleRepository.findById(scheduleId);

        if (scheduleOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Schedule schedule = scheduleOptional.get();

        // 이전 일정 삭제
        memberAcademyRepository.deleteByAcademyIdAndCommonMemberId(schedule.getAcademyId(), schedule.getCommonMember().getId());
        calendarRepository.deleteByScheduleId(scheduleId);
//        scheduleAlarmRepository.deleteByScheduleId(scheduleId);

        scheduleRepository.deleteById(scheduleId);
    }
}