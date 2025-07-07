package com.orangeschool.support.api.service;

import com.google.firebase.messaging.Notification;
import com.orangeschool.support.api.dto.AlarmNotificationRequest;
import com.orangeschool.support.api.dto.MemberAlarmInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 회원 알림 관련 기능을 제공하는 인터페이스
 * 다른 도메인에서 알림 발송이 필요할 때 사용
 */
public interface MemberAlarmProvider {
    
    /**
     * 시스템 알림 생성 및 푸시 발송
     *
     * @param request 알림 발송 요청 정보
     * @throws Exception 알림 생성 중 오류 발생 시
     */
    void createSystemAlarm(AlarmNotificationRequest request) throws Exception;
    
    /**
     * 간단한 시스템 알림 생성 (제목, 내용, 회원ID만 사용)
     *
     * @param title 알림 제목
     * @param content 알림 내용
     * @param memberId 수신 회원 ID
     * @throws Exception 알림 생성 중 오류 발생 시
     */
    void createSystemAlarm(String title, String content, Long memberId) throws Exception;
    
    /**
     * 특정 회원의 알림 목록 조회
     *
     * @param memberId 회원 ID
     * @param pageable 페이징 정보
     * @return 알림 정보 페이지
     * @throws Exception 조회 중 오류 발생 시
     */
    Page<MemberAlarmInfo> getMemberAlarms(Long memberId, Pageable pageable) throws Exception;
    
    /**
     * 특정 회원의 새 알림 존재 여부 확인
     *
     * @param memberId 회원 ID
     * @return 새 알림 존재 여부
     * @throws Exception 확인 중 오류 발생 시
     */
    Boolean hasNewAlarm(Long memberId) throws Exception;
    
    /**
     * 알림 읽음 처리
     *
     * @param alarmId 알림 ID
     * @return 읽음 처리된 알림 정보
     * @throws Exception 처리 중 오류 발생 시
     */
    MemberAlarmInfo markAsRead(Long alarmId) throws Exception;
}