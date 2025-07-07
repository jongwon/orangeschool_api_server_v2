# Orange School API Server v2

교육 플랫폼 Orange School의 백엔드 API 서버입니다.

## 기술 스택

- Java 21
- Spring Boot 2.7.18
- MariaDB
- Redis
- FCM (Firebase Cloud Messaging)

## 시작하기

### 필수 환경

- Java 21
- MariaDB 또는 MySQL
- Redis

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
- 파일 업로드/다운로드 (로컬 파일 시스템)

## 파일 저장 설정

파일은 로컬 파일 시스템에 저장됩니다. `application.yml`에서 다음 설정을 환경에 맞게 수정하세요:

```yaml
# 파일 저장 경로
folderPath: "/home/orangeschool/files"
# 파일 접근 URL 경로
resourcePath: "/files"
# 서버 URL
serverUri: "http://localhost:8080"
```

Rocky OS나 기타 Linux 서버에 배포 시 파일 저장 디렉토리에 적절한 권한을 설정해야 합니다:

```bash
sudo mkdir -p /home/orangeschool/files
sudo chown -R orangeschool:orangeschool /home/orangeschool/files
sudo chmod 755 /home/orangeschool/files
```

## 개발 가이드

자세한 개발 가이드는 [CLAUDE.md](./CLAUDE.md) 파일을 참고하세요.