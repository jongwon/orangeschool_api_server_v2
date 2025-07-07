# Orange School API Server 요구사항 명세서

## 1. 시스템 개요

Orange School API Server는 교육 관련 서비스를 제공하는 종합 플랫폼으로, 학생, 학부모, 교사, 관리자를 위한 다양한 기능을 제공합니다.

### 주요 사용자 유형
- **학생 (Child)**: 챌린지 참여, 일정 관리, 친구 기능 사용
- **학부모 (Parent)**: 자녀 관리, 일정 승인, 챌린지 관리
- **관리자 (Admin)**: 시스템 전체 관리, 콘텐츠 관리
- **매니저 (Manager)**: 학원 관리, 공지사항 관리

### 핵심 기술 스택
- Spring Boot 2.7.x
- Spring Security + JWT
- JPA/Hibernate
- QueryDSL
- Redis (세션 및 캐싱)
- FCM (푸시 알림)
- AWS S3 (파일 저장)

## 2. 도메인별 기능 명세

### 2.1 회원 관리 (CommonMember Domain)

#### 2.1.1 인증 및 보안 (AuthController)
- **일반 로그인**: `POST /api/common/login`
  - 이메일/비밀번호 인증
  - JWT 토큰 발급 (유효기간: 6시간)
  - Push Token 업데이트
- **소셜 로그인**: `POST /api/common/login/social`
  - 카카오 등 소셜 로그인 지원
  - JoinType enum으로 플랫폼 구분
- **이메일 찾기**: `GET /api/common/find/email`
  - 이름, 휴대폰 번호로 이메일 조회
- **비밀번호 찾기/재설정**: `GET /api/common/find/password`, `POST /api/common/reset/password`
  - 이메일, 이름, 휴대폰 번호 검증

#### 2.1.2 회원가입 (RegistrationController)
- **회원가입**: `POST /api/common/join`
  - 회원 타입: 부모(PARENT), 자녀(CHILD)
  - 프로필 이미지 업로드 지원
  - 초대코드 기능
- **중복 체크**
  - 이메일: `POST /api/common/check/email`
  - 닉네임: `POST /api/common/check/nickname`
  - 초대코드: `POST /api/common/check/referralCode`
- **SMS 인증**: `POST /api/common/check/phoneNumber`
  - 6자리 랜덤 번호 생성
  - Redis 저장 (TTL: 5분)

#### 2.1.3 프로필 관리 (MemberProfileController)
- **프로필 조회/수정**: `GET/PUT /api/user/commonMember`
- **설정 관리**
  - 알림 설정: `PUT /api/user/commonMember/setting/alarm`
  - 지역 설정: `PUT /api/user/v2/commonMember/setting/region`
  - 초대코드 재설정: `PUT /api/user/v2/commonMember/setting/referralCode`
- **닉네임 수정**: `PUT /api/user/v2/commonMember`

#### 2.1.4 자녀 관리 (ChildManagementController)
- **자녀 목록 조회**: `GET /api/user/commonMembers`
- **자녀 정보 관리**: `GET/PUT/DELETE /api/user/commonMember/{id}`
- **학교 정보 수정**: `PUT /api/user/commonMember/school/{id}`
- **시간표 관리**
  - 편집: `PUT /api/user/v2/commonMember/setting/timetable`
  - 조회: `GET /api/user/v2/timetable/{id}`

#### 2.1.5 가족/구성원 관리 (FamilyManagementController)
- **구성원 요청 목록**: `GET /api/user/v2/referral/request`
- **구성원 승인/반려**: `POST /api/user/v2/referral/request/confirm/{id}`
- **가족 구성원 조회**: `GET /api/admin/v2/commonMember/family/{id}`

#### 2.1.6 동네 친구 (TownFriendController)
- **프로필 조회**: `GET /api/user/my/profile/{id}`
- **친구 목록**: `GET /api/user/townFriends/{id}`
- **최다 오렌지 보유자**: `GET /api/user/townFriends/top/{id}`
- **친구 검색**: `GET /api/user/search/townFriends/{id}`

#### 2.1.7 관리자 기능 (AdminMemberController)
- **회원 관리**: 목록 조회, 상세 조회, 수정, 삭제
- **활성화 상태 관리**: `PUT /api/admin/commonMember/activation/{id}`
- **자녀 목록 조회**: `GET /api/admin/commonMembers/{id}`

### 2.2 학원 관리 (Academy Domain)

#### 기능 목록
- **학원 등록**: `POST /api/user/academy`, `POST /api/admin/academy`
- **학원 목록 조회**: `GET /api/user/academies`, `GET /api/admin/academies`
- **학원 상세 조회**: `GET /api/admin/academy/{id}`
- **학원 정보 수정**: `PUT /api/admin/academy/{id}`
- **학원 삭제**: `DELETE /api/admin/academy/{id}`, `DELETE /api/admin/academies`

#### 회원별 학원 관리 (MemberAcademy)
- **회원별 학원 목록**: `GET /api/admin/commonMember/memberAcademies/{memberId}`
- **학원별 회원 목록**: `GET /api/admin/academy/memberAcademies/{academyId}`

### 2.3 일정 관리 (Schedule Domain)

#### 일정 기능
- **일정 생성**: `POST /api/user/schedule`
  - 일정 유형: 일반, 학원, 병원, 기타
  - 반복 설정 지원
  - 알림 설정
- **일정 조회**
  - 월간: `GET /api/user/month/calendars`
  - 주간: `GET /api/user/week/calendars`
  - 일간: `GET /api/user/day/calendars`
  - 시간표: `GET /api/user/timeTable`
- **일정 수정/삭제**: `PUT/DELETE /api/user/schedule/{id}`
- **지출 관리**
  - 목록: `GET /api/user/payment`
  - 그래프: `GET /api/user/payment/graph`

#### 일정 임시 저장 (ScheduleTemp)
- **임시 일정 조회**: `GET /api/user/schedule/request/{id}`
- **수정 요청**: `PUT /api/user/schedule/request/{id}`
- **승인/반려**: `POST /api/user/schedule/confirm/{id}`

#### 학교 일정 (SchoolSchedule)
- **학교 일정 등록**: `POST /api/admin/schoolSchedule`
- **학교 일정 조회**: `GET /api/user/schoolSchedules`

### 2.4 챌린지 시스템 (Challenge Domain)

#### 챌린지 기능
- **챌린지 생성**: `POST /api/user/challenge`
  - 목표 설정
  - 보상 설정 (오렌지)
  - 기간 설정
- **도장 관리**
  - 도장 요청: `POST /api/user/challenge/stamp/{id}`
  - 도장 추가: `POST /api/user/challenge/stamp/add/{id}`
  - 도장 제거: `POST /api/user/challenge/stamp/remove/{id}`
- **챌린지 완료**: `POST /api/user/challenge/complete/{id}`
- **챌린지 조회/수정/삭제**: `GET/PUT/DELETE /api/user/challenge/{id}`

#### 챌린지 임시 저장 (ChallengeTemp)
- **수정 요청 조회**: `GET /api/user/challenge/request/{id}`
- **수정 요청**: `PUT /api/user/challenge/request/{id}`
- **승인/반려**: `POST /api/user/challenge/confirm/{id}`

### 2.5 커뮤니티 기능

#### 2.5.1 O's Life (Pick Domain)
- **게시물 관리**
  - 작성: `POST /api/admin/pick`
  - 목록: `GET /api/user/picks`, `GET /api/admin/picks`
  - 상세: `GET /api/user/pick/{id}`
  - 수정/삭제: `PUT/DELETE /api/admin/pick/{id}`
- **댓글 기능** (PickComment)
  - 작성: `POST /api/user/pick/comment`
  - 목록: `GET /api/user/pick/comments`
  - 수정/삭제: `PUT/DELETE /api/user/pick/comment/{id}`
- **대댓글 기능** (PickReply)
  - 작성: `POST /api/user/pick/reply`
  - 목록: `GET /api/user/pick/replies`
- **좋아요 기능** (PickLike)
  - 좋아요/취소: `POST /api/user/pick/like/{pickId}`
  - 목록: `GET /api/user/pick/likes`

#### 2.5.2 Story Domain
- **스토리 관리**
  - 작성: `POST /api/user/story`
  - 목록/상세: `GET /api/user/stories`, `GET /api/user/story/{id}`
  - 수정/삭제: `PUT/DELETE /api/user/story/{id}`
- **댓글/대댓글/좋아요**: Pick과 동일한 구조

#### 2.5.3 응원 시스템 (Cheering)
- **응원하기/취소**: `POST /api/user/cheering/{memberId}`
- **응원 목록**: `GET /api/user/cheerings/{id}`

#### 2.5.4 팔로우 시스템 (Follow)
- **팔로우/언팔로우**: `POST /api/user/follow/{myId}/{followerId}`

### 2.6 알림 시스템

#### 2.6.1 알림 관리 (Alarm)
- **알림 생성**: `POST /api/admin/alarm`
- **알림 목록**: `GET /api/admin/alarms`
- **알림 삭제**: `DELETE /api/admin/alarm/{id}`

#### 2.6.2 회원 알림 (MemberAlarm)
- **알림 목록**: `GET /api/user/memberAlarm`
- **알림 상세**: `GET /api/user/memberAlarm/{id}`
- **새 알림 확인**: `GET /api/user/memberAlarm/new/exist`

### 2.7 공지사항 시스템

#### 2.7.1 공지사항 관리 (Notice)
- **공지 작성**: `POST /api/admin/notice`
- **공지 목록**: `GET /api/admin/notices`
- **공지 삭제**: `DELETE /api/admin/notice/{id}`

#### 2.7.2 회원 공지 (MemberNotice)
- **공지 목록**: `GET /api/user/memberNotices`
- **공지 상세**: `GET /api/user/memberNotice/{id}`

### 2.8 배너 시스템 (Banner)

#### Banner V1
- **배너 등록**: `POST /api/admin/banner/{locationId}`
- **배너 조회**: `GET /api/user/banners/{locationCode}`
- **배너 수정/삭제**: `PUT/DELETE /api/admin/banner/{id}`

#### Banner V2
- **배너 관리**: 생성, 조회, 수정, 삭제
- **클릭 추적**: `GET /api/user/bannerV2/click/{id}`
- **활성화 관리**: `PUT /api/admin/bannerV2/activation/{id}`

### 2.9 기타 기능

#### 2.9.1 관리자 (Manager)
- **관리자 가입/로그인**: `POST /api/common/manager`, `POST /api/common/manager/login`
- **2단계 인증**: `POST /api/common/manager/check/phone`
- **관리자 관리**: 목록, 상세, 수정, 삭제
- **승인 관리**: `PUT /api/admin/manager/approve/{id}`

#### 2.9.2 신고 시스템 (Report)
- **신고하기**: `POST /api/user/report`
- **신고 목록**: `GET /api/admin/reports`
- **신고 처리**: `PUT /api/admin/report/{id}`

#### 2.9.3 제안 시스템 (Suggest)
- **제안하기**: `POST /api/user/suggest`
- **제안 목록**: `GET /api/admin/suggests`

#### 2.9.4 방문자 관리 (Visitor)
- **방문 기록**: `POST /api/user/visitor`
- **방문자 통계**: `GET /api/admin/visitors`
- **회원 수 조회**: `GET /api/admin/visitor/memberCount`

#### 2.9.5 팝업 관리 (Popup)
- **팝업 생성**: `POST /api/admin/popup`
- **팝업 조회**: `GET /api/user/popups`
- **팝업 관리**: 수정, 삭제, 활성화

#### 2.9.6 약관 관리 (Terms)
- **약관 등록**: `POST /api/admin/terms`
- **약관 조회**: `GET /api/common/terms`
- **약관 수정**: `PUT /api/admin/terms/{id}`

#### 2.9.7 기본 정보 (BaseInfo)
- **기본 정보 조회**: `GET /api/common/baseInfo`
- **기본 정보 수정**: `PUT /api/admin/baseInfo`

#### 2.9.8 지역 정보 (Location)
- **지역 목록**: `GET /api/admin/locations`

#### 2.9.9 탈퇴 회원 (LeaveMember)
- **회원 탈퇴**: `POST /api/user/leaveMember`
- **탈퇴 회원 관리**: `GET/DELETE /api/admin/leaveMember/{id}`

## 3. 공통 기능 및 서비스

### 3.1 파일 관리
- **지원 형식**: 이미지(JPG, PNG), 동영상(MOV, MP4)
- **저장 위치**: AWS S3 또는 로컬 스토리지
- **파일명 관리**: 서버 파일명(UUID), 원본 파일명 분리 저장

### 3.2 푸시 알림
- **FCM 연동**: Firebase Cloud Messaging
- **알림 유형**
  - 일정 알림
  - 챌린지 도장 요청
  - 구성원 참여 요청
  - 댓글/좋아요 알림

### 3.3 이메일/SMS
- **이메일 발송**: 비밀번호 재설정, 공지사항
- **SMS 발송**: 인증번호, 중요 알림

### 3.4 엑셀 다운로드
- **회원 목록 엑셀 다운로드**
- **통계 데이터 엑셀 다운로드**

### 3.5 스케줄러
- **일정 알림 발송**
- **챌린지 만료 처리**
- **통계 데이터 집계**

## 4. 보안 요구사항

### 4.1 인증/인가
- **JWT 토큰 기반 인증**
  - Access Token 유효기간: 6시간
  - Refresh Token: 미구현 (추후 구현 예정)
- **권한 구분**
  - ROLE_USER: 일반 사용자
  - ROLE_ADMIN: 관리자

### 4.2 데이터 보안
- **비밀번호**: BCrypt 암호화
- **민감 정보**: 개인정보 암호화 저장
- **API 접근 제어**: Spring Security 기반

### 4.3 보안 정책
- **CORS 설정**: 허용된 도메인만 접근
- **SQL Injection 방지**: Prepared Statement 사용
- **XSS 방지**: 입력값 검증 및 이스케이프

## 5. 성능 요구사항

### 5.1 응답 시간
- **일반 API**: 1초 이내
- **목록 조회**: 2초 이내
- **파일 업로드**: 10초 이내

### 5.2 동시 접속
- **최대 동시 사용자**: 1,000명
- **일일 활성 사용자**: 10,000명

### 5.3 데이터 용량
- **데이터베이스**: 100GB 이상
- **파일 스토리지**: 1TB 이상

## 6. 운영 요구사항

### 6.1 모니터링
- **서버 상태 모니터링**
- **API 응답 시간 모니터링**
- **에러 로그 수집 및 분석**

### 6.2 백업
- **데이터베이스**: 일일 백업
- **파일 스토리지**: 주간 백업
- **백업 보관 기간**: 30일

### 6.3 로깅
- **API 요청/응답 로깅**
- **에러 로깅**
- **보안 이벤트 로깅**

## 7. 개발 환경

### 7.1 개발 도구
- **IDE**: IntelliJ IDEA
- **빌드 도구**: Gradle
- **버전 관리**: Git

### 7.2 환경 구분
- **local**: 로컬 개발 환경
- **dev**: 개발 서버
- **prod**: 운영 서버

### 7.3 외부 서비스
- **Redis**: 캐싱 및 세션 관리
- **FCM**: 푸시 알림
- **AWS S3**: 파일 스토리지
- **SMTP**: 이메일 발송

## 8. 향후 개선 사항

### 8.1 기능 개선
- **리프레시 토큰 구현**
- **소셜 로그인 확대** (네이버, 구글 등)
- **실시간 채팅 기능**
- **화상 수업 기능**

### 8.2 성능 개선
- **캐싱 전략 고도화**
- **데이터베이스 쿼리 최적화**
- **CDN 도입**

### 8.3 보안 강화
- **2단계 인증 확대**
- **로그인 시도 제한**
- **API Rate Limiting**

### 8.4 사용성 개선
- **다국어 지원**
- **접근성 개선**
- **모바일 최적화**