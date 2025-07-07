# Orange School API Server v2

교육 플랫폼 Orange School의 백엔드 API 서버입니다.

## 기술 스택

- Java 11
- Spring Boot 2.7.15
- MariaDB
- Redis
- AWS S3
- FCM (Firebase Cloud Messaging)

## 시작하기

### 필수 환경 변수

다음 환경 변수를 설정해야 합니다:

```bash
export AWS_ACCESS_KEY=your_aws_access_key
export AWS_SECRET_KEY=your_aws_secret_key
```

### 로컬 환경 실행

1. Redis 실행
```bash
docker run -d -p 6379:6379 redis
```

2. 애플리케이션 실행
```bash
./gradlew bootRun
```

### 빌드

```bash
./gradlew clean build
```

## API 문서

애플리케이션 실행 후 아래 주소에서 Swagger UI를 확인할 수 있습니다:
- http://localhost:8080/api/swagger

## 프로젝트 구조

```
src/main/java/com/orangeschool/orangeschoolapiserver/
├── common/           # 공통 모듈 (설정, 유틸리티 등)
└── domain/           # 도메인별 모듈
    ├── academy/      # 학원 관리
    ├── alarm/        # 알림
    ├── calendar/     # 캘린더
    ├── challenge/    # 챌린지
    ├── commonMember/ # 회원 관리
    └── ...           # 기타 도메인
```

## 주요 기능

- 회원 관리 (부모/자녀)
- 일정 관리 (학원, 병원, 기타)
- 챌린지 시스템
- 커뮤니티 (게시물, 댓글)
- 푸시 알림
- 파일 업로드/다운로드

## 개발 가이드

자세한 개발 가이드는 [CLAUDE.md](./CLAUDE.md) 파일을 참고하세요.