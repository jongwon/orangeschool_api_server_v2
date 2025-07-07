# Orange School API Server 멀티 모듈 마이그레이션 가이드

## 현재 상태

멀티 모듈 프로젝트 구조가 설정되었습니다:

```
orangeschool-api-server/
├── settings.gradle          # 멀티 모듈 설정
├── build.gradle            # 루트 빌드 설정
├── core-module/
│   ├── common-core/        # 공통 엔티티, Enum, 유틸리티
│   └── auth-core/          # 인증/보안 관련
├── domain-modules/
│   ├── member-domain/      # 회원 관리
│   ├── schedule-domain/    # 일정 관리
│   ├── education-domain/   # 교육 관련
│   ├── community-domain/   # 커뮤니티
│   └── support-domain/     # 지원 기능
├── infra-module/           # 인프라 (Redis, FCM 등)
└── api-application/        # REST API 애플리케이션

```

## 마이그레이션 단계

### 1단계: Common 모듈 이동
```bash
# CommonEntity, enums, exceptions 이동
cp -r src/main/java/com/orangeschool/orangeschoolapiserver/common/entity/* \
  core-module/common-core/src/main/java/com/orangeschool/common/entity/

cp -r src/main/java/com/orangeschool/orangeschoolapiserver/common/enums/* \
  core-module/common-core/src/main/java/com/orangeschool/common/enums/

# 패키지명 변경 필요: com.orangeschool.orangeschoolapiserver.common -> com.orangeschool.common
```

### 2단계: Auth 모듈 이동
```bash
# Security config, JWT 관련 클래스 이동
cp -r src/main/java/com/orangeschool/orangeschoolapiserver/common/config/security/* \
  core-module/auth-core/src/main/java/com/orangeschool/auth/config/

# 패키지명 변경 필요
```

### 3단계: 도메인 모듈 이동
각 도메인별로:
- Entity 클래스
- Repository 인터페이스
- Service 클래스
- DTO 클래스

### 4단계: Controller 이동
모든 Controller는 api-application 모듈로:
```bash
cp -r src/main/java/com/orangeschool/orangeschoolapiserver/domain/*/controller/* \
  api-application/src/main/java/com/orangeschool/orangeschoolapiserver/controller/
```

## 주의사항

1. **패키지 구조 변경**
   - 기존: `com.orangeschool.orangeschoolapiserver.common.*`
   - 변경: `com.orangeschool.common.*` (core-module)
   - 변경: `com.orangeschool.auth.*` (auth-core)

2. **의존성 관리**
   - 도메인 간 직접 참조 제거
   - Service 인터페이스를 통한 통신

3. **QueryDSL Q클래스**
   - 각 모듈에서 독립적으로 생성
   - Entity가 있는 모듈에서 생성

4. **빌드 순서**
   1. core-module
   2. infra-module
   3. domain-modules
   4. api-application

## 테스트 방법

```bash
# 전체 빌드
./gradlew clean build

# 특정 모듈만 빌드
./gradlew :core-module:common-core:build

# 애플리케이션 실행
./gradlew :api-application:bootRun
```

## 다음 단계

1. 기존 코드를 단계별로 이동
2. 패키지명 일괄 변경
3. import 문 수정
4. 도메인 간 의존성 정리
5. 통합 테스트 수행