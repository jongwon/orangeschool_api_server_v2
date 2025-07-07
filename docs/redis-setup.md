# Redis 설정 가이드

## 개요
Orange School API 서버는 다음 용도로 Redis를 사용합니다:
- 관리자 로그인 시 휴대폰 인증번호 임시 저장 (TTL: 5분)
- 기타 캐싱 목적

## Docker를 사용한 Redis 실행

### 1. Docker Compose 실행
```bash
# docs 디렉토리로 이동
cd docs

# Redis 컨테이너 실행
docker-compose up -d

# 실행 상태 확인
docker-compose ps
```

### 2. Redis 컨테이너 중지
```bash
docker-compose down
```

### 3. Redis 컨테이너 및 데이터 완전 삭제
```bash
docker-compose down -v
```

## Redis 연결 정보

### 기본 연결 정보
- **Host**: localhost
- **Port**: 6379
- **Password**: 없음 (기본 설정)

### Spring Boot 설정
application.yml에서 Redis 연결 설정:
```yaml
spring:
  redis:
    host: localhost
    port: 6379
```

## Redis CLI 접속

### Docker를 통한 접속
```bash
# Redis CLI 접속
docker exec -it orangeschool-redis redis-cli

# 모든 키 조회
127.0.0.1:6379> keys *

# 특정 키 조회
127.0.0.1:6379> get "1111"

# TTL 확인
127.0.0.1:6379> ttl "1111"

# 종료
127.0.0.1:6379> exit
```

## 주요 사용 사례

### 1. 관리자 휴대폰 인증
- 인증번호를 키와 값으로 동일하게 저장
- TTL: 5분
- 예시: 키 "123456" = 값 "123456"

### 2. 데이터 저장 패턴
```java
// 데이터 저장 (TTL 5분)
redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(5));

// 데이터 조회
Object value = redisTemplate.opsForValue().get(key);

// 데이터 삭제
redisTemplate.delete(key);
```

## 운영 환경 고려사항

### 1. 데이터 영속성
- 현재 설정: AOF(Append Only File) 활성화로 재시작 시에도 데이터 유지
- `--appendonly yes` 옵션 사용

### 2. 메모리 관리
- Redis는 모든 데이터를 메모리에 저장
- 필요시 maxmemory 설정 추가 권장

### 3. 보안
- 운영 환경에서는 반드시 비밀번호 설정
- 네트워크 접근 제한 설정

### 4. 백업
- 정기적인 RDB 스냅샷 또는 AOF 백업 권장
- 볼륨 마운트된 `/data` 디렉토리 백업

## 문제 해결

### Redis 연결 실패
1. Docker 컨테이너 실행 상태 확인
   ```bash
   docker-compose ps
   ```

2. 포트 충돌 확인
   ```bash
   lsof -i :6379
   ```

3. 로그 확인
   ```bash
   docker-compose logs redis
   ```

### 메모리 부족
1. Redis 메모리 사용량 확인
   ```bash
   docker exec -it orangeschool-redis redis-cli info memory
   ```

2. 불필요한 키 삭제
   ```bash
   docker exec -it orangeschool-redis redis-cli flushdb
   ```