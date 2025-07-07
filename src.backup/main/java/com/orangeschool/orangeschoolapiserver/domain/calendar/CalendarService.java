package com.orangeschool.orangeschoolapiserver.domain.calendar;

import com.orangeschool.orangeschoolapiserver.common.entity.CommonEntity;
import com.orangeschool.orangeschoolapiserver.common.enums.*;
import com.orangeschool.orangeschoolapiserver.common.response.CustomException;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.domain.calendar.dto.CalendarDto;
import com.orangeschool.orangeschoolapiserver.domain.calendar.dto.CalendarSearchDto;
import com.orangeschool.orangeschoolapiserver.domain.calendar.dto.GraphDto;
import com.orangeschool.orangeschoolapiserver.domain.calendar.dto.PaymentResponseDto;
import com.orangeschool.orangeschoolapiserver.domain.calendar.entity.Calendar;
import com.orangeschool.orangeschoolapiserver.domain.calendar.repository.CalendarRepository;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.repository.CommonMemberRepository;
import com.orangeschool.orangeschoolapiserver.domain.schedule.entity.Schedule;
import com.orangeschool.orangeschoolapiserver.domain.scheduleAlarm.ScheduleAlarmService;
import com.orangeschool.orangeschoolapiserver.domain.scheduleAlarm.entity.ScheduleAlarm;
import com.orangeschool.orangeschoolapiserver.domain.scheduleAlarm.repository.ScheduleAlarmRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
@Service
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final CommonMemberRepository commonMemberRepository;
    private final ScheduleAlarmRepository scheduleAlarmRepository;
    private final ScheduleAlarmService scheduleAlarmService;

    private static final Logger logger = LoggerFactory.getLogger(CalendarService.class);

    @Transactional
    public void create(Schedule schedule, Long creatorId) throws Exception {

        LocalDateTime localDateTimeNow = LocalDateTime.now();

        // 반복이 없는 경우
        if (schedule.getCycleType() == CycleType.NONE) {
            LocalDate startDate = schedule.getStartDate();
            LocalDate endDate = schedule.getEndDate();
            Calendar calendar = null;
            // 2일 이상이 아닌 경우
            if (startDate.equals(endDate)) {
                calendar = Calendar.builder()
                        .schedule(schedule)
                        .title(schedule.getTitle())
                        .startDate(startDate)
                        .startTime(schedule.getStartTime())
                        .endDate(endDate)
                        .endTime(schedule.getEndTime())
                        .isAllDay(schedule.getIsAllDay())
                        .scheduleType(schedule.getScheduleType())
                        .color(schedule.getColor())
                        .isBetween(false)
                        .amount(0L)
                        .memo(schedule.getMemo())
                        .isSingle(true)
                        .isImportant(schedule.getIsImportant())
                        .build();

                calendarRepository.save(calendar);
            } else {
                List<Calendar> calendarList = new ArrayList<>();

                Period period = Period.between(startDate, endDate);
                int days = period.getDays() + 1;
                LocalDate cycleDate = schedule.getStartDate();
                for (int i = 0; i < days; i++) {
                    Calendar betweenCalendar = Calendar.builder()
                            .schedule(schedule)
                            .title(schedule.getTitle())
                            .startDate(cycleDate)
                            .endDate(cycleDate)
                            .startTime(i + 1 == days ? LocalTime.of(0, 0, 0) : schedule.getStartTime())
                            .endTime(i == 0 ? LocalTime.of(0, 0, 0) : schedule.getEndTime())
                            .isAllDay(i == 0 || i + 1 == days ? schedule.getIsAllDay() : true)
                            .scheduleType(schedule.getScheduleType())
                            .color(schedule.getColor())
                            .isBetween(true)
                            .amount(0L)
                            .memo(schedule.getMemo())
                            .isSingle(false)
                            .isImportant(schedule.getIsImportant())
                            .build();

                    if (i == 0) {
                        calendar = betweenCalendar;
                    }
                    calendarList.add(betweenCalendar);
                    cycleDate = cycleDate.plusDays(1); // 다음 날짜로 이동
                }

                calendarRepository.saveAll(calendarList);
            }

            if (schedule.getScheduleAlarmType() != ScheduleAlarmType.NONE) {
                LocalDateTime alarmDateTime = getAlarmDateTime(schedule.getStartDate(), schedule.getStartTime(), schedule.getScheduleAlarmType());
                if (alarmDateTime != null && alarmDateTime.isAfter(localDateTimeNow)) {

                    ScheduleAlarm scheduleAlarm = ScheduleAlarm.builder()
                            .calendar(calendar)
                            .alarmDateTime(alarmDateTime)
                            .scheduleId(schedule.getId())
                            .commonMemberId(schedule.getCommonMember().getId())
                            .isPay(false)
                            .build();

                    scheduleAlarmRepository.save(scheduleAlarm);
                }
            }

            if (schedule.getScheduleAlarmTypeForParent() != ScheduleAlarmType.NONE) {
                LocalDateTime alarmDateTime = getAlarmDateTime(schedule.getStartDate(), schedule.getStartTime(), schedule.getScheduleAlarmTypeForParent());
                if (alarmDateTime != null && alarmDateTime.isAfter(localDateTimeNow)) {

                    ScheduleAlarm scheduleAlarm = ScheduleAlarm.builder()
                            .calendar(calendar)
                            .alarmDateTime(alarmDateTime)
                            .scheduleId(schedule.getId())
                            .commonMemberId(creatorId != null && Objects.equals(schedule.getCommonMember().getId(), creatorId) ? schedule.getCommonMember().getId() : schedule.getCommonMember().getParentId())
                            .isPay(false)
                            .build();

                    scheduleAlarmRepository.save(scheduleAlarm);
                }
            }

            // 요일 반복
        } else if (schedule.getCycleType() == CycleType.DAY) {
            List<Calendar> calendarList = new ArrayList<>();
            List<ScheduleAlarm> scheduleAlarmList = new ArrayList<>();
            LocalDate cycleStartDate = schedule.getStartDate();
            // 하루 일정이 아닌 경우에는 정한 기간동안 요일 반복
            LocalDate cycleEndDate = schedule.getEndDate();
            Boolean isSingle = false;

            // 하루일정인 경우
            if (schedule.getStartDate().equals(schedule.getEndDate())) {
                cycleEndDate = schedule.getCycleEndDate();
                isSingle = true;
            }

            String[] numbersArray = schedule.getCycleDays().split(",");
            List<Integer> cycleDays = Arrays.stream(numbersArray)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList()); // [1,3,5,6]

            while (!cycleStartDate.isAfter(cycleEndDate)) {
                if (cycleDays.contains(cycleStartDate.getDayOfWeek().getValue())) {
                    Calendar calendar = Calendar.builder()
                            .schedule(schedule)
                            .title(schedule.getTitle())
                            .startDate(cycleStartDate)
                            .endDate(cycleStartDate)
                            .startTime(schedule.getStartTime())
                            .endTime(schedule.getEndTime())
                            .isAllDay(schedule.getIsAllDay())
                            .scheduleType(schedule.getScheduleType())
                            .color(schedule.getColor())
                            .isBetween(false)
                            .amount(0L)
                            .memo(schedule.getMemo())
                            .isSingle(isSingle)
                            .isImportant(schedule.getIsImportant())
                            .build();

                    calendarList.add(calendar);

                    if (schedule.getScheduleAlarmType() != ScheduleAlarmType.NONE) {
                        LocalDateTime alarmDateTime = getAlarmDateTime(cycleStartDate, schedule.getStartTime(), schedule.getScheduleAlarmType());
                        if (alarmDateTime != null && alarmDateTime.isAfter(localDateTimeNow)) {
                            ScheduleAlarm scheduleAlarm = ScheduleAlarm.builder()
                                    .calendar(calendar)
                                    .alarmDateTime(alarmDateTime)
                                    .scheduleId(schedule.getId())
                                    .commonMemberId(schedule.getCommonMember().getId())
                                    .isPay(false)
                                    .build();

                            scheduleAlarmList.add(scheduleAlarm);
                        }
                    }

                    if (schedule.getScheduleAlarmTypeForParent() != ScheduleAlarmType.NONE) {
                        LocalDateTime alarmDateTime = getAlarmDateTime(cycleStartDate, schedule.getStartTime(), schedule.getScheduleAlarmTypeForParent());
                        if (alarmDateTime != null && alarmDateTime.isAfter(localDateTimeNow)) {
                            ScheduleAlarm scheduleAlarm = ScheduleAlarm.builder()
                                    .calendar(calendar)
                                    .alarmDateTime(alarmDateTime)
                                    .scheduleId(schedule.getId())
                                    .commonMemberId(creatorId != null && Objects.equals(schedule.getCommonMember().getId(), creatorId) ? schedule.getCommonMember().getId() : schedule.getCommonMember().getParentId())
                                    .isPay(false)
                                    .build();

                            scheduleAlarmList.add(scheduleAlarm);
                        }
                    }
                }
                cycleStartDate = cycleStartDate.plusDays(1); // 다음 날짜로 이동
            }

            calendarRepository.saveAll(calendarList);
            scheduleAlarmRepository.saveAll(scheduleAlarmList);
            // 주기 반복
        } else if (schedule.getCycleType() == CycleType.PERIOD) {
            List<Calendar> calendarList = new ArrayList<>();
            List<ScheduleAlarm> scheduleAlarmList = new ArrayList<>();
            LocalDate startDate = schedule.getStartDate();
            LocalDate endDate = schedule.getEndDate();
            LocalDate cycleEndDate = schedule.getCycleEndDate();

            CalendarCycle calendarCycle = schedule.getCalendarCycle();
            Calendar calendar = null;
            while (!endDate.isAfter(cycleEndDate)) {

                // 2일 이상이 아닌 경우
                if (schedule.getStartDate().equals(schedule.getEndDate())) {
                    calendar = Calendar.builder()
                            .schedule(schedule)
                            .title(schedule.getTitle())
                            .startDate(startDate)
                            .startTime(schedule.getStartTime())
                            .endDate(endDate)
                            .endTime(schedule.getEndTime())
                            .isAllDay(schedule.getIsAllDay())
                            .scheduleType(schedule.getScheduleType())
                            .color(schedule.getColor())
                            .isBetween(false)
                            .amount(0L)
                            .memo(schedule.getMemo())
                            .isSingle(true)
                            .isImportant(schedule.getIsImportant())
                            .build();

                    calendarList.add(calendar);
                } else {
                    Period period = Period.between(startDate, endDate);
                    int days = period.getDays() + 1;
                    LocalDate cycleDate = startDate;
                    for (int i = 0; i < days; i++) {
                        Calendar betweenCalendar = Calendar.builder()
                                .schedule(schedule)
                                .title(schedule.getTitle())
                                .startDate(cycleDate)
                                .endDate(cycleDate)
                                .startTime(i + 1 == days ? LocalTime.of(0, 0, 0) : schedule.getStartTime())
                                .endTime(i == 0 ? LocalTime.of(0, 0, 0) : schedule.getEndTime())
                                .isAllDay(i == 0 || i + 1 == days ? schedule.getIsAllDay() : true)
                                .scheduleType(schedule.getScheduleType())
                                .color(schedule.getColor())
                                .isBetween(true)
                                .amount(0L)
                                .memo(schedule.getMemo())
                                .isSingle(false)
                                .isImportant(schedule.getIsImportant())
                                .build();

                        if (i == 0) {
                            calendar = betweenCalendar;
                        }
                        calendarList.add(betweenCalendar);
                        cycleDate = cycleDate.plusDays(1); // 다음 날짜로 이동
                    }
                }

                if (schedule.getScheduleAlarmType() != ScheduleAlarmType.NONE) {
                    LocalDateTime alarmDateTime = getAlarmDateTime(startDate, schedule.getStartTime(), schedule.getScheduleAlarmType());
                    if (alarmDateTime != null && alarmDateTime.isAfter(localDateTimeNow)) {
                        ScheduleAlarm scheduleAlarm = ScheduleAlarm.builder()
                                .calendar(calendar)
                                .alarmDateTime(alarmDateTime)
                                .scheduleId(schedule.getId())
                                .commonMemberId(schedule.getCommonMember().getId())
                                .isPay(false)
                                .build();

                        scheduleAlarmList.add(scheduleAlarm);
                    }
                }

                if (schedule.getScheduleAlarmTypeForParent() != ScheduleAlarmType.NONE) {
                    LocalDateTime alarmDateTime = getAlarmDateTime(startDate, schedule.getStartTime(), schedule.getScheduleAlarmTypeForParent());
                    if (alarmDateTime != null && alarmDateTime.isAfter(localDateTimeNow)) {
                        ScheduleAlarm scheduleAlarm = ScheduleAlarm.builder()
                                .calendar(calendar)
                                .alarmDateTime(alarmDateTime)
                                .scheduleId(schedule.getId())
                                .commonMemberId(creatorId != null && Objects.equals(schedule.getCommonMember().getId(), creatorId) ? schedule.getCommonMember().getId() : schedule.getCommonMember().getParentId())
                                .isPay(false)
                                .build();

                        scheduleAlarmList.add(scheduleAlarm);
                    }
                }

                switch (calendarCycle) {
                    case EVERY_DAY:
                        startDate = startDate.plusDays(CalendarCycle.EVERY_DAY.getPlusValue());
                        endDate = endDate.plusDays(CalendarCycle.EVERY_DAY.getPlusValue());
                        break;
                    case EVERY_WEEK:
                        startDate = startDate.plusWeeks(CalendarCycle.EVERY_WEEK.getPlusValue());
                        endDate = endDate.plusWeeks(CalendarCycle.EVERY_WEEK.getPlusValue());
                        break;
                    case TWO_WEEK:
                        startDate = startDate.plusWeeks(CalendarCycle.TWO_WEEK.getPlusValue());
                        endDate = endDate.plusWeeks(CalendarCycle.TWO_WEEK.getPlusValue());
                        break;
                    case THREE_WEEK:
                        startDate = startDate.plusWeeks(CalendarCycle.THREE_WEEK.getPlusValue());
                        endDate = endDate.plusWeeks(CalendarCycle.THREE_WEEK.getPlusValue());
                        break;
                    case FOUR_WEEK:
                        startDate = startDate.plusWeeks(CalendarCycle.FOUR_WEEK.getPlusValue());
                        endDate = endDate.plusWeeks(CalendarCycle.FOUR_WEEK.getPlusValue());
                        break;
                    case EVERY_MONTH:
                        startDate = startDate.plusMonths(CalendarCycle.EVERY_MONTH.getPlusValue());
                        endDate = endDate.plusMonths(CalendarCycle.EVERY_MONTH.getPlusValue());
                        break;
                    case EVERY_YEAR:
                        startDate = startDate.plusYears(CalendarCycle.EVERY_YEAR.getPlusValue());
                        endDate = endDate.plusYears(CalendarCycle.EVERY_YEAR.getPlusValue());
                        break;
                }
            }

            calendarRepository.saveAll(calendarList);
            scheduleAlarmRepository.saveAll(scheduleAlarmList);
        }

        // 결제일 넣기
        if (schedule.getUsePay()) {
            if (schedule.getPayCycle() == PayCycle.NONE) {
                Calendar payCalendar = Calendar.builder()
                        .schedule(schedule)
                        .title(schedule.getTitle())
                        .startDate(schedule.getPayDate())
                        .endDate(schedule.getPayDate())
                        .isAllDay(true)
                        .scheduleType(ScheduleType.PAY)
                        .color(schedule.getColor())
                        .isBetween(false)
                        .amount(schedule.getAmount())
                        .memo(schedule.getMemo())
                        .isSingle(true)
                        .build();

                calendarRepository.save(payCalendar);

                if (schedule.getUsePaymentAlarm() && schedule.getPayAlarmType() != PayAlarmType.NONE) {
                    LocalDateTime alarmDateTime = getPayAlarmDateTime(schedule.getPayDate(), schedule.getPayAlarmType());
                    if (alarmDateTime.isAfter(localDateTimeNow)) {

                        ScheduleAlarm scheduleAlarm = ScheduleAlarm.builder()
                                .calendar(payCalendar)
                                .alarmDateTime(alarmDateTime)
                                .scheduleId(schedule.getId())
                                .commonMemberId(schedule.getCommonMember().getId())
                                .isPay(true)
                                .build();

                        scheduleAlarmRepository.save(scheduleAlarm);
                    }
                }
            } else {
                LocalDate payCycleDate = schedule.getPayDate();
                LocalDate payCycleEndDate = schedule.getPayCycleEndDate();
                List<Calendar> calendarList = new ArrayList<>();
                List<ScheduleAlarm> scheduleAlarmList = new ArrayList<>();
                while (!payCycleDate.isAfter(payCycleEndDate)) {
                    Calendar payCalendar = Calendar.builder()
                            .schedule(schedule)
                            .title(schedule.getTitle())
                            .startDate(payCycleDate)
                            .endDate(payCycleDate)
                            .isAllDay(true)
                            .scheduleType(ScheduleType.PAY)
                            .color(schedule.getColor())
                            .isBetween(false)
                            .amount(schedule.getAmount())
                            .memo(schedule.getMemo())
                            .isSingle(true)
                            .build();

                    calendarList.add(payCalendar);

                    if (schedule.getUsePaymentAlarm() && schedule.getPayAlarmType() != PayAlarmType.NONE) {
                        LocalDateTime alarmDateTime = getPayAlarmDateTime(payCycleDate, schedule.getPayAlarmType());
                        if (alarmDateTime.isAfter(localDateTimeNow)) {

                            ScheduleAlarm scheduleAlarm = ScheduleAlarm.builder()
                                    .calendar(payCalendar)
                                    .alarmDateTime(alarmDateTime)
                                    .scheduleId(schedule.getId())
                                    .commonMemberId(schedule.getCommonMember().getId())
                                    .isPay(true)
                                    .build();

                            scheduleAlarmList.add(scheduleAlarm);
                        }
                    }


                    payCycleDate = payCycleDate.plusMonths(schedule.getPayCycle().getMonth());
                }

                calendarRepository.saveAll(calendarList);
                scheduleAlarmRepository.saveAll(scheduleAlarmList);
            }
        }
    }

    @Transactional
    public void createPayment(Schedule schedule) throws Exception {

        LocalDateTime localDateTimeNow = LocalDateTime.now();

        if (schedule.getPayCycle() == PayCycle.NONE) {
            Calendar payCalendar = Calendar.builder()
                    .schedule(schedule)
                    .title(schedule.getTitle())
                    .startDate(schedule.getPayDate())
                    .endDate(schedule.getPayDate())
                    .isAllDay(true)
                    .scheduleType(ScheduleType.PAY)
                    .color(schedule.getColor())
                    .isBetween(false)
                    .amount(schedule.getAmount())
                    .memo(schedule.getMemo())
                    .isSingle(true)
                    .build();

            calendarRepository.save(payCalendar);

            if (schedule.getUsePaymentAlarm() && schedule.getPayAlarmType() != PayAlarmType.NONE) {
                LocalDateTime alarmDateTime = getPayAlarmDateTime(schedule.getPayDate(), schedule.getPayAlarmType());
                if (alarmDateTime.isAfter(localDateTimeNow)) {

                    ScheduleAlarm scheduleAlarm = ScheduleAlarm.builder()
                            .calendar(payCalendar)
                            .alarmDateTime(alarmDateTime)
                            .scheduleId(schedule.getId())
                            .commonMemberId(schedule.getCommonMember().getId())
                            .isPay(true)
                            .build();

                    scheduleAlarmRepository.save(scheduleAlarm);
                }
            }
        } else {
            LocalDate payCycleDate = schedule.getPayDate();
            LocalDate payCycleEndDate = schedule.getPayCycleEndDate();
            List<Calendar> calendarList = new ArrayList<>();
            List<ScheduleAlarm> scheduleAlarmList = new ArrayList<>();
            while (!payCycleDate.isAfter(payCycleEndDate)) {
                Calendar payCalendar = Calendar.builder()
                        .schedule(schedule)
                        .title(schedule.getTitle())
                        .startDate(payCycleDate)
                        .endDate(payCycleDate)
                        .isAllDay(true)
                        .scheduleType(ScheduleType.PAY)
                        .color(schedule.getColor())
                        .isBetween(false)
                        .amount(schedule.getAmount())
                        .memo(schedule.getMemo())
                        .isSingle(true)
                        .build();

                calendarList.add(payCalendar);

                if (schedule.getUsePaymentAlarm() && schedule.getPayAlarmType() != PayAlarmType.NONE) {
                    LocalDateTime alarmDateTime = getPayAlarmDateTime(payCycleDate, schedule.getPayAlarmType());
                    if (alarmDateTime.isAfter(localDateTimeNow)) {

                        ScheduleAlarm scheduleAlarm = ScheduleAlarm.builder()
                                .calendar(payCalendar)
                                .alarmDateTime(alarmDateTime)
                                .scheduleId(schedule.getId())
                                .commonMemberId(schedule.getCommonMember().getId())
                                .isPay(true)
                                .build();

                        scheduleAlarmList.add(scheduleAlarm);
                    }
                }


                payCycleDate = payCycleDate.plusMonths(schedule.getPayCycle().getMonth());
            }

            calendarRepository.saveAll(calendarList);
            scheduleAlarmRepository.saveAll(scheduleAlarmList);
        }
    }

    @Transactional(readOnly = true)
    public Map<LocalDate, List<CalendarDto>> getMonth(Long commonMemberId, CalendarSearchDto calendarSearchDto) throws Exception {

        List<CalendarDto> calendarDtoList = calendarRepository.searchMonth(commonMemberId, calendarSearchDto);

        return calendarDtoList.stream().collect(groupingBy(CalendarDto::getStartDate));
    }

    @Transactional(readOnly = true)
    public List<CalendarDto> getWeek(Long commonMemberId, CalendarSearchDto calendarSearchDto) throws Exception {

        return calendarRepository.searchWeek(commonMemberId, calendarSearchDto);
    }

    @Transactional(readOnly = true)
    public List<CalendarDto> getWeekByIsAllDay(Long commonMemberId, CalendarSearchDto calendarSearchDto) throws Exception {

        return calendarRepository.searchWeekByIsAllDay(commonMemberId, calendarSearchDto);
    }

    @Transactional(readOnly = true)
    public List<CalendarDto> getDay(Long commonMemberId, CalendarSearchDto calendarSearchDto) throws Exception {

        return calendarRepository.searchDay(commonMemberId, calendarSearchDto);
    }

    @Transactional(readOnly = true)
    public List<CalendarDto> getTimeTable(CalendarSearchDto calendarSearchDto) throws Exception {

        List<CalendarDto> calendarDtoList = calendarRepository.searchTimeTable(calendarSearchDto);
//        Map<ScheduleType, Long> timeTableMap = calendarDtoList.stream().collect(Collectors.groupingBy(CalendarDto::getScheduleType, Collectors.counting()));

        return calendarDtoList;
    }

    @Transactional(readOnly = true)
    public PaymentResponseDto getPayment(Long commonMemberId, CalendarSearchDto calendarSearchDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        List<Long> childList = new ArrayList<>();
        if (calendarSearchDto.getCommonMemberIdList().isEmpty()) {
            List<CommonMember> commonMemberList = commonMemberRepository.findByParentId(commonMemberId);
            childList = commonMemberList.stream().map(CommonEntity::getId).collect(Collectors.toList());
            childList.add(commonMemberOptional.get().getId());
        } else {
            childList = calendarSearchDto.getCommonMemberIdList();
        }

        List<CalendarDto> calendarDtoList = calendarRepository.searchPayment(childList, calendarSearchDto);

        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
        paymentResponseDto.setPaySchedule(calendarDtoList);
        paymentResponseDto.setPaySum(calendarDtoList.stream().mapToLong(CalendarDto::getAmount).sum());

        CalendarSearchDto prevCalendarSearchDto = new CalendarSearchDto();
        prevCalendarSearchDto.setMonthStartDate(calendarSearchDto.getMonthStartDate().minusMonths(1));

        List<CalendarDto> prevCalendarDtoList = calendarRepository.searchPayment(childList, prevCalendarSearchDto);
        paymentResponseDto.setPreviousMonth(prevCalendarDtoList.stream().mapToLong(CalendarDto::getAmount).sum());

        return paymentResponseDto;
    }

    @Transactional(readOnly = true)
    public Map<String, List<GraphDto>> getGraphInfo(Long commonMemberId, CalendarSearchDto calendarSearchDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        List<CommonMember> commonMemberList = commonMemberRepository.findByParentId(commonMemberId);
        List<Long> childList = commonMemberList.stream().map(CommonEntity::getId).collect(Collectors.toList());
        childList.add(commonMemberOptional.get().getId());

        List<GraphDto> graphDtoList = calendarRepository.searchGraphInfo(childList, calendarSearchDto);
        graphDtoList.stream().map(graphDto -> {
            graphDto.setDate(graphDto.getYear() + "." + graphDto.getMonth());
            return graphDto;
        }).collect(Collectors.toList());

        Map<String, List<GraphDto>> groupedByMonth = graphDtoList.stream()
                .collect(Collectors.groupingBy(GraphDto::getDate));

        // 키를 월 순서로 정렬하기 위한 Comparator를 정의합니다.
        Comparator<String> monthComparator = (date1, date2) -> {
            String[] parts1 = date1.split("\\.");
            String[] parts2 = date2.split("\\.");

            int year1 = Integer.parseInt(parts1[0]);
            int year2 = Integer.parseInt(parts2[0]);
            int month1 = Integer.parseInt(parts1[1]);
            int month2 = Integer.parseInt(parts2[1]);

            // 연도가 다른 경우 연도를 기준으로 정렬합니다.
            if (year1 != year2) {
                return Integer.compare(year1, year2);
            }

            // 연도가 같은 경우 월을 기준으로 정렬합니다.
            return Integer.compare(month1, month2);
        };

        // 맵을 월 순서로 정렬합니다.
        Map<String, List<GraphDto>> sortedByMonth = groupedByMonth.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(monthComparator))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        return sortedByMonth;
    }

    private LocalDateTime getAlarmDateTime(LocalDate startDate, LocalTime startTime, ScheduleAlarmType scheduleAlarmType) {

        LocalDateTime alarmDateTime = null;

        if (startTime != null) {
            LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);

            switch (scheduleAlarmType) {
                case ZERO_MINUTES_AGO:
                    alarmDateTime = startDateTime;
                    break;
                case FIVE_MINUTES_AGO:
                    alarmDateTime = startDateTime.minusMinutes(5);
                    break;
                case FIFTEEN_MINUTES_AGO:
                    alarmDateTime = startDateTime.minusMinutes(15);
                    break;
                case THIRTY_MINUTES_AGO:
                    alarmDateTime = startDateTime.minusMinutes(30);
                    break;
                case ONE_HOURS_AGO:
                    alarmDateTime = startDateTime.minusHours(1);
                    break;
                case TWO_HOURS_AGO:
                    alarmDateTime = startDateTime.minusHours(2);
                    break;
                case ONE_DAY_AGO:
                    alarmDateTime = startDateTime.minusDays(1);
                    break;
            }
        }

        return alarmDateTime;
    }

    private LocalDateTime getPayAlarmDateTime(LocalDate startDate, PayAlarmType payAlarmType) {

        LocalDateTime alarmDateTime = null;

        if (startDate != null) {
            switch (payAlarmType) {
                case YESTERDAY_9AM:
                    alarmDateTime = startDate.minusDays(1).atTime(9, 0);
                    break;
                case TODAY_9AM:
                    alarmDateTime = startDate.atTime(9, 0);
                    break;
            }
        }

        return alarmDateTime;
    }

    private String getMapKey(LocalDateTime alarmDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return alarmDateTime.format(formatter);
    }
}
