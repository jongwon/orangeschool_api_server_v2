# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Orange School API Server는 부모와 자녀가 함께 사용하는 교육 플랫폼 백엔드입니다. Spring Boot 3.3.6 기반의 REST API 서버로, 일정 관리, 학습 챌린지, 커뮤니티 기능을 제공합니다. Java 21을 사용합니다.

## 개발 환경 설정

### 빌드 및 실행
```bash
# 프로젝트 빌드
./gradlew build

# 애플리케이션 실행 (dev 프로파일)
./gradlew bootRun

# 테스트 실행
./gradlew test

# QueryDSL Q클래스 생성 (엔티티 변경 시)
./gradlew clean compileQuerydsl

# 클린 빌드
./gradlew clean build
```

### 로컬 개발 환경
```bash
# Redis 실행 (Docker 사용 시)
docker run -d -p 6379:6379 redis

# 로컬 프로파일로 실행
./gradlew bootRun -Dspring.profiles.active=local
```

## 아키텍처 구조

### 패키지 구조
```
com.orangeschool.orangeschoolapiserver/
├── common/           # 공통 모듈
│   ├── config/       # Spring 설정 (Security, JWT, Redis, Swagger 등)
│   ├── dto/          # 공통 DTO (요청/응답)
│   ├── entity/       # 공통 엔티티 (BaseEntity)
│   ├── enums/        # 전역 Enum 정의
│   ├── response/     # 응답 처리 (ResponseDto, CustomException)
│   └── utils/        # 유틸리티 (JWT, 파일, SMS, FCM 등)
└── domain/           # 도메인별 모듈
    ├── {domain}/
    │   ├── {Domain}Controller.java
    │   ├── {Domain}Service.java
    │   ├── dto/
    │   ├── entity/
    │   └── repository/
```

### 도메인 모듈 구성
각 도메인은 독립적인 Controller-Service-Repository 구조를 가집니다:
- **Controller**: REST API 엔드포인트 정의
- **Service**: 비즈니스 로직 처리
- **Repository**: JPA + QueryDSL 데이터 접근
- **DTO**: 요청/응답 데이터 전송 객체
- **Entity**: JPA 엔티티

## 주요 도메인 모듈

### 1. CommonMember (회원 관리)
- 부모/자녀 회원 가입 및 로그인
- JWT 토큰 기반 인증
- 소셜 로그인 (카카오, 네이버, 애플)
- 가족 구성원 관리
- 동네친구 기능

### 2. Schedule/Calendar (일정 관리)
- 학원, 병원, 기타 일정 관리
- 반복 일정 지원 (매일, 매주, 매월)
- 지출 관리 기능
- 일정 알림 설정

### 3. Challenge (챌린지)
- 부모-자녀 간 도전 과제
- 도장 시스템
- 오렌지(포인트) 관리
- 챌린지 승인/거절

### 4. Story/Pick (커뮤니티)
- 게시물 작성 및 관리
- 댓글, 대댓글, 좋아요
- 팔로우 시스템
- 응원 메시지

### 5. Alarm/MemberAlarm (알림)
- FCM 푸시 알림
- 알림 유형별 관리
- 읽음 상태 관리

## API 규칙

### 응답 형식
모든 API는 통일된 ResponseDto 형식을 사용:
```java
{
    "code": "S200",
    "message": "성공",
    "data": { ... }
}
```

### 인증
- JWT Bearer 토큰 사용 (유효기간 6시간)
- Header: `Authorization: Bearer {token}`
- 인증 불필요 엔드포인트: 로그인, 회원가입, 이메일 중복 체크 등

### 페이징
- Pageable 파라미터 사용
- 예: `?page=0&size=20&sort=createdDate,desc`

## 데이터베이스 작업

### JPA 사용 시 주의사항
- N+1 문제 방지: fetch join 또는 @EntityGraph 사용
- 벌크 연산: QueryDSL 사용
- 복잡한 조회: Repository Custom 인터페이스 구현

### QueryDSL 사용
```java
// Repository Custom 구현체에서 사용
QCommonMember member = QCommonMember.commonMember;
return queryFactory
    .selectFrom(member)
    .where(member.email.eq(email))
    .fetchOne();
```

## 외부 연동

### AWS S3 파일 업로드
- FileManagement 유틸리티 클래스 사용
- 버킷: orangeschool-bucket
- 최대 파일 크기: 2GB

### FCM 푸시 알림
- PushMessageManagement 클래스 사용
- 설정 파일: `resources/fcm/fcm-key.json`

### Redis 캐싱
- 세션 관리
- SMS 인증번호 저장 (TTL 5분)
- 임시 데이터 저장

## 환경별 설정

### 프로파일
- **local**: 로컬 개발 (H2 DB)
- **dev**: 개발 서버 (외부 MariaDB)
- **prod**: 운영 서버

### 주요 설정
- Swagger UI: `/api/swagger`
- 로그 파일: `logs/spring.log` (최대 1GB, 60일 보관)
- 타임존: Asia/Seoul

## 테스트 작성

### 단위 테스트
```java
@SpringBootTest
@AutoConfigureMockMvc
class SomeServiceTest {
    @MockBean
    private SomeRepository repository;
    
    @Test
    void testMethod() {
        // given
        // when
        // then
    }
}
```

### 통합 테스트
- @SpringBootTest 사용
- 테스트 DB: H2 (인메모리)
- 테스트 데이터: @Sql 또는 @BeforeEach에서 설정

## 보안 고려사항

- 비밀번호: BCrypt 암호화
- SQL Injection: Prepared Statement 사용 (JPA/QueryDSL)
- XSS: 입력값 검증 및 이스케이프
- CORS: SecurityConfig에서 설정
- 민감 정보: application-*.yml에 저장 (git 제외)

## 트러블슈팅

### QueryDSL Q클래스 미생성
```bash
./gradlew clean compileQuerydsl
```

### Redis 연결 실패
- Redis 서버 실행 확인
- application-{profile}.yml의 Redis 설정 확인

### JWT 토큰 인증 실패
- 토큰 만료 시간 확인 (6시간)
- Bearer 프리픽스 확인
- JwtAuthenticationFilter 로그 확인