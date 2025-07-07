# Challenge 모듈 분석

## 1. 모듈이 담당하는 역할

Challenge 모듈은 오렌지스쿨의 핵심 게이미피케이션 시스템으로, 부모-자녀 간 교육적 상호작용을 촉진합니다:

- 부모가 자녀를 위한 미션(챌린지) 생성 및 관리
- 도장(스탬프) 시스템을 통한 진행도 추적
- 도장을 오렌지로 변환하는 보상 시스템 (5도장 = 1오렌지)
- 자녀의 챌린지 수정 요청에 대한 부모 승인 워크플로우
- 챌린지 공개/비공개 설정을 통한 프라이버시 제어
- 푸시 알림을 통한 실시간 상호작용

## 2. 도메인 객체 구조도

```mermaid
classDiagram
    class CommonEntity {
        +Long id
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
    }
    
    class Challenge {
        +CommonMember commonMember
        +ChallengeStatus challengeStatus
        +String mission
        +Integer currentStampCount
        +Integer currentOrangeCount
        +Integer requiredOrangeCount
        +String reward
        +Boolean isShow
    }
    
    class ChallengeTemp {
        +CommonMember commonMember
        +String mission
        +Integer requiredOrangeCount
        +String reward
        +Boolean isShow
        +ConfirmStatus confirmStatus
        +Long challengeId
    }
    
    class CommonMember {
        +String email
        +MemberType memberType
        +Long parentId
        +Boolean challengeProgress
        +Integer totalOrange
        +Integer totalOrangeAll
    }
    
    CommonEntity <|-- Challenge
    CommonEntity <|-- ChallengeTemp
    
    Challenge "*" --> "1" CommonMember : commonMember(자녀)
    ChallengeTemp "*" --> "1" CommonMember : commonMember(자녀)
    
    class ChallengeStatus {
        <<enumeration>>
        NONE("")
        PROGRESS("진행중")
        END("종료")
    }
    
    class ConfirmStatus {
        <<enumeration>>
        NONE("")
        WAIT("승인대기")
        REJECT("거절됨")
        COMPLETE("승인됨")
        DELETE("삭제됨")
    }
    
    Challenge --> ChallengeStatus
    ChallengeTemp --> ConfirmStatus
    
    note for ChallengeTemp "챌린지 수정 요청을 위한 임시 저장소\n부모 승인 후 원본 Challenge 업데이트"
```

## 3. 모듈 연관성 분석

### 모듈 간 상호작용
```mermaid
graph TD
    A[부모] -->|챌린지 생성| B[Challenge Module]
    C[자녀] -->|도장 요청| B
    C -->|수정 요청| D[ChallengeTemp]
    
    B -->|알림 발송| E[Alarm Module]
    B -->|회원 정보| F[CommonMember Module]
    
    A -->|도장 추가/제거| B
    A -->|수정 승인/거절| D
    
    D -->|승인 시| B
    
    B -->|진행상태 업데이트| F
    B -->|오렌지 적립| F
    
    G[다른 사용자] -.->|공개 챌린지 조회| B
```

### 데이터 흐름
```mermaid
sequenceDiagram
    participant Parent as 부모
    participant Child as 자녀
    participant CS as ChallengeService
    participant Alarm as AlarmService
    participant DB as Database
    
    Parent->>CS: 챌린지 생성
    CS->>DB: Challenge 저장
    CS->>DB: 자녀 challengeProgress=true
    
    Child->>CS: 도장 요청
    CS->>Alarm: 부모에게 알림 발송
    CS->>Parent: 푸시 알림
    
    Parent->>CS: 도장 추가
    CS->>DB: currentStampCount++
    
    alt 5개 도장 달성
        CS->>DB: currentOrangeCount++
        CS->>DB: currentStampCount = 0
        CS->>DB: 자녀 totalOrange++
    end
    
    CS->>Child: 업데이트된 챌린지 정보
```

## 4. 서비스에서 하는 일과 시퀀스 다이어그램

### 주요 서비스 메소드

#### ChallengeService
1. **create()**: 부모가 자녀를 위한 챌린지 생성
2. **challengeStamp()**: 자녀의 도장 요청 및 부모 알림
3. **challengeAddStamp()**: 부모의 도장 추가 (5개마다 오렌지 변환)
4. **challengeRemoveStamp()**: 부모의 도장 제거
5. **challengeComplete()**: 챌린지 완료 처리
6. **update()**: 챌린지 정보 수정
7. **delete()**: 챌린지 삭제

#### ChallengeTempService
1. **update()**: 자녀의 수정 요청 생성
2. **updateConfirm()**: 부모의 승인/거절 처리

### 도장 시스템 시퀀스 다이어그램

```mermaid
sequenceDiagram
    participant C as 자녀
    participant CC as ChallengeController
    participant CS as ChallengeService
    participant AS as AlarmService
    participant P as 부모
    
    C->>CC: POST /api/user/challenge/stamp/{id}
    CC->>CS: challengeStamp(challengeId, childEmail)
    
    CS->>CS: 챌린지 유효성 검증
    CS->>AS: createToSystem("도장 요청", "자녀가 도장을 요청했습니다")
    AS->>P: 푸시 알림 발송
    
    CS-->>CC: ChallengeDto
    CC-->>C: 200 OK
    
    Note over P: 부모가 알림 확인 후
    
    P->>CC: POST /api/user/challenge/stamp/add/{id}
    CC->>CS: challengeAddStamp(challengeId, parentEmail)
    
    CS->>CS: 부모-자녀 관계 검증
    CS->>CS: currentStampCount++
    
    alt currentStampCount == 5
        CS->>CS: currentOrangeCount++
        CS->>CS: currentStampCount = 0
        CS->>CS: 자녀.totalOrange++
        CS->>CS: 자녀.totalOrangeAll++
    end
    
    CS-->>CC: ChallengeDto
    CC-->>P: 200 OK
```

### 챌린지 수정 승인 워크플로우

```mermaid
sequenceDiagram
    participant C as 자녀
    participant CTC as ChallengeTempController
    participant CTS as ChallengeTempService
    participant CS as ChallengeService
    participant P as 부모
    
    C->>CTC: PUT /api/user/challenge/request/{id}
    CTC->>CTS: update(challengeId, dto, childEmail)
    
    CTS->>CTS: ChallengeTemp 생성 (confirmStatus=WAIT)
    CTS-->>CTC: ChallengeTempDto
    CTC-->>C: 200 OK
    
    P->>CTC: POST /api/user/challenge/confirm/{id}
    CTC->>CTS: updateConfirm(challengeId, dto, parentEmail)
    
    alt confirmStatus == COMPLETE (승인)
        CTS->>CS: 원본 Challenge 업데이트
        CTS->>CTS: ChallengeTemp 삭제
    else confirmStatus == REJECT (거절)
        CTS->>CTS: ChallengeTemp 삭제
    end
    
    CTS-->>CTC: ChallengeTempDto
    CTC-->>P: 200 OK
```

## 5. 개선점

### 1. **트랜잭션 관리 강화**
```java
@Transactional(isolation = Isolation.SERIALIZABLE)
public ChallengeDto challengeAddStamp(Long challengeId, String userEmail) {
    // 동시 도장 추가 방지를 위한 격리 수준 설정
}
```

### 2. **도장 이력 추적**
```java
@Entity
public class StampHistory extends CommonEntity {
    @ManyToOne
    private Challenge challenge;
    
    @ManyToOne
    private CommonMember addedBy;
    
    private StampAction action;  // ADD, REMOVE
    private Integer stampCountBefore;
    private Integer stampCountAfter;
    private String reason;
}
```

### 3. **챌린지 템플릿 시스템**
```java
@Entity
public class ChallengeTemplate {
    private String title;
    private String missionTemplate;
    private Integer suggestedOrangeCount;
    private String category;  // 학습, 생활습관, 운동 등
    private Integer ageMin;
    private Integer ageMax;
}
```

### 4. **도장 제거 제약 조건 구현**
```java
public void validateStampRemoval(Challenge challenge) {
    LocalDateTime lastStampTime = getLastStampAddTime(challenge);
    if (lastStampTime.isBefore(LocalDateTime.now().minusHours(24))) {
        throw new CustomException(ResponseCode.STAMP_REMOVAL_TIME_EXCEEDED);
    }
}
```

### 5. **ChallengeTemp 자동 정리**
```java
@Scheduled(cron = "0 0 2 * * *")  // 매일 새벽 2시
public void cleanupAbandonedRequests() {
    LocalDateTime threshold = LocalDateTime.now().minusDays(7);
    challengeTempRepository.deleteByCreatedAtBeforeAndConfirmStatus(
        threshold, ConfirmStatus.WAIT
    );
}
```

### 6. **도장-오렌지 변환율 설정**
```java
@Entity
public class ChallengeConfig extends CommonEntity {
    private Integer stampsPerOrange = 5;  // 기본값
    private Boolean allowCustomRatio = false;
    
    @ManyToOne
    private CommonMember member;  // 회원별 커스텀 설정
}
```

### 7. **챌린지 통계 기능**
```java
public class ChallengeStatistics {
    private Double averageCompletionTime;
    private Integer totalChallengesCompleted;
    private Integer totalOrangesEarned;
    private Map<String, Integer> challengesByCategory;
    private List<Integer> stampProgressByDay;
}
```

### 8. **배지 시스템 도입**
```java
@Entity
public class Badge {
    private String name;
    private String description;
    private String imageUrl;
    private BadgeType type;  // CHALLENGE_COUNT, ORANGE_COUNT, STREAK
    private Integer requirement;
}
```

### 9. **챌린지 공유 기능**
```java
public class ChallengeShareDto {
    private String shareCode;
    private String shareUrl;
    private LocalDateTime expiresAt;
    
    public String generateShareLink(Challenge challenge) {
        String code = UUID.randomUUID().toString();
        // Redis에 임시 저장
        return "https://orangeschool.com/challenge/share/" + code;
    }
}
```

### 10. **알림 커스터마이징**
```java
public class ChallengeNotificationSettings {
    private Boolean notifyOnStampRequest = true;
    private Boolean notifyOnCompletion = true;
    private Boolean dailyProgressReminder = false;
    private LocalTime reminderTime = LocalTime.of(20, 0);
}
```