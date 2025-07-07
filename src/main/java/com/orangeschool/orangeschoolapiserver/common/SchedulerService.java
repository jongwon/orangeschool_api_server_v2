//package com.orangeschool.orangeschoolapiserver.common;
//
//import com.orangeschool.orangeschoolapiserver.common.enums.ScheduleType;
//import com.orangeschool.orangeschoolapiserver.common.utils.PushMessageManagement;
//import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
//import com.orangeschool.orangeschoolapiserver.domain.commonMember.repository.CommonMemberRepository;
//import com.orangeschool.orangeschoolapiserver.domain.schedule.entity.Schedule;
//import com.orangeschool.orangeschoolapiserver.domain.schedule.repository.ScheduleRepository;
//import com.orangeschool.orangeschoolapiserver.domain.scheduleAlarm.entity.ScheduleAlarm;
//import com.orangeschool.orangeschoolapiserver.domain.scheduleAlarm.repository.ScheduleAlarmRepository;
//import com.orangeschool.orangeschoolapiserver.domain.story.story.repository.StoryRepository;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@RequiredArgsConstructor
//@Service
//public class SchedulerService {
//    private final ScheduleAlarmRepository scheduleAlarmRepository;
//    private final ScheduleRepository scheduleRepository;
//    private final CommonMemberRepository commonMemberRepository;
//    private final StoryRepository storyRepository;
//
//    private final PushMessageManagement pushMessageManagement;
//
//    private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);
//
//    // 매일 자정, 스케줄 알람 삭제
//    @Scheduled(cron = "0 0 0 * * *")
//    public void setScheduleAlarm() {
//        scheduleAlarmRepository.deleteToDay();
//    }
//
//    // 1분마다 스케줄 알람 발송
//    @Scheduled(cron = "0 * * * * *")
//    public void sendScheduleAlarm() {
//        List<ScheduleAlarm> currentAlarms = scheduleAlarmRepository.searchByNow();
//        if (!currentAlarms.isEmpty()) {
//            for (ScheduleAlarm scheduleAlarm : currentAlarms) {
//                // 알람 처리 작업 수행
//                Optional<Schedule> scheduleOptional = scheduleRepository.findById(scheduleAlarm.getScheduleId());
//                Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(scheduleAlarm.getCommonMemberId());
//                if (scheduleOptional.isPresent() && commonMemberOptional.isPresent()) {
//
//                    Schedule schedule = scheduleOptional.get();
//                    CommonMember commonMember = commonMemberOptional.get();
//
//                    Optional<CommonMember> parentOptional = commonMemberRepository.findById(commonMember.getParentId());
//                    if (parentOptional.isPresent()) {
//                        CommonMember parent = parentOptional.get();
//                        if (parent.getAgreeToSchedule() && !parent.getPushToken().isEmpty()) {
//                            pushMessageManagement.sendToSchedule(schedule, parent, commonMember.getName());
//                        }
//                    }
//
//                    if (schedule.getScheduleType() != ScheduleType.PAY) {
//                        if (commonMember.getAgreeToSchedule() && !commonMember.getPushToken().isEmpty()) {
//                            pushMessageManagement.sendToSchedule(schedule, commonMember, commonMember.getName());
//                        }
//                    }
//
//                }
//            }
//        }
//    }
//
//    // 매월 1일 자정, 챌린지 오렌지 초기화
//    @Scheduled(cron = "0 0 0 1 * *")
//    public void resetOrange() {
//        commonMemberRepository.resetOrange();
//    }
//
//    // 매일 4시, 10시, 16시, 22시에 todayViewCount초기화
//    @Scheduled(cron = "0 0 4,10,16,22 * * *")
//    public void setTodayViewCountSchedule() {
//        storyRepository.resetTodayViewCount();
//    }
//}
