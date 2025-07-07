# Schedule 모듈 분석

## 1. 모듈이 담당하는 역할

Schedule 모듈은 오렌지스쿨의 종합적인 일정 관리 시스템으로, 다음과 같은 기능을 담당합니다:

- 학원, 학교, 차량, 병원 등 다양한 유형의 일정 생성 및 관리
- 단일, 반복, 다일간 일정 지원 (일/주/월/년 단위)
- 결제 일정 관리 및 주기적 결제 추적
- 부모-자녀 일정 승인 워크플로우
- 일정별 알림 설정 (부모/자녀 개별 설정)
- 하루 메시지, 수면 정보, 할 일 목록 등 부가 기능
- Calendar 모듈과 연동하여 실제 캘린더 이벤트 생성

## 2. 도메인 객체 구조도

```mermaid
classDiagram
    class CommonEntity {
        +Long id
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
    }
    
    class Schedule {
        +CommonMember commonMember
        +ScheduleType scheduleType
        +String title
        +LocalDate startDate
        +LocalTime startTime
        +LocalDate endDate
        +LocalTime endTime
        +Boolean isAllDay
        +String color
        +CycleType cycleType
        +CalendarCycle calendarCycle
        +String cycleDays
        +Integer cycleWeek
        +Boolean usePay
        +Long amount
        +PayCycle payCycle
        +ScheduleAlarmType alarmType
        +Boolean memberAlarm
        +Boolean parentAlarm
        +String memo
        +Boolean isSingle
        +Boolean isImportant
        +Academy academy
        +Set~Calendar~ calendars
    }
    
    class ScheduleTemp {
        +CommonMember commonMember
        +ConfirmStatus confirmStatus
        +ScheduleRequestType scheduleRequestType
        +ScheduleUpdateType scheduleUpdateType
        +Long scheduleId
        <<모든 Schedule 필드 포함>>
    }
    
    class Calendar {
        +Schedule schedule
        +String title
        +LocalDate startDate
        +LocalTime startTime
        +LocalDate endDate
        +LocalTime endTime
        +Boolean isAllDay
        +ScheduleType scheduleType
        +String color
        +Boolean isBetween
        +String memo
        +Long amount
        +Set~ScheduleAlarm~ scheduleAlarms
    }
    
    class ScheduleAlarm {
        +Calendar calendar
        +Long scheduleId
        +Long commonMemberId
        +LocalDateTime alarmDateTime
        +Boolean isPay
    }
    
    class DayMessage {
        +CommonMember commonMember
        +LocalDate date
        +String childMessage
        +String parentMessage
    }
    
    class SleepInfo {
        +CommonMember commonMember
        +LocalTime wakeTime
        +LocalTime sleepTime
    }
    
    class TodoList {
        +CommonMember commonMember
        +LocalDate date
        +String todoList
    }
    
    CommonEntity <|-- Schedule
    CommonEntity <|-- ScheduleTemp
    CommonEntity <|-- Calendar
    CommonEntity <|-- ScheduleAlarm
    CommonEntity <|-- DayMessage
    CommonEntity <|-- SleepInfo
    CommonEntity <|-- TodoList
    
    Schedule "1" --> "*" Calendar : calendars
    Calendar "1" --> "*" ScheduleAlarm : scheduleAlarms
    Schedule "*" --> "1" CommonMember : commonMember
    ScheduleTemp "*" --> "1" CommonMember : commonMember
    DayMessage "*" --> "1" CommonMember : commonMember
    SleepInfo "1" --> "1" CommonMember : commonMember
    TodoList "*" --> "1" CommonMember : commonMember
```

## 3. 모듈 연관성 분석

### 모듈 간 협업 구조
```mermaid
graph TD
    S[Schedule Module] -->|템플릿 제공| C[Calendar Module]
    S -->|알림 생성| A[Alarm Module]
    S -->|회원 정보| CM[CommonMember Module]
    S -->|학원 정보| AC[Academy Module]
    
    ST[ScheduleTemp] -->|승인 후| S
    S -->|삭제 시| C
    C -->|알림 설정| SA[ScheduleAlarm]
    
    P[부모] -->|승인/거절| ST
    CH[자녀] -->|생성 요청| ST
    
    S -->|부가 기능| DM[DayMessage]
    S -->|부가 기능| SI[SleepInfo]
    S -->|부가 기능| TL[TodoList]
```

### 일정 타입별 처리 흐름
```mermaid
graph LR
    A[일정 생성] --> B{반복 타입?}
    
    B -->|NONE| C[단일/다일간]
    B -->|DAY| D[요일 반복]
    B -->|PERIOD| E[주기 반복]
    
    C -->|단일| F[1개 Calendar]
    C -->|다일간| G[N개 Calendar]
    
    D --> H[선택 요일별 Calendar]
    
    E -->|EVERY_DAY| I[매일]
    E -->|EVERY_WEEK| J[매주]
    E -->|EVERY_MONTH| K[매월]
    E -->|EVERY_YEAR| L[매년]
    
    F --> M[알림 설정]
    G --> M
    H --> M
    I --> M
    J --> M
    K --> M
    L --> M
```

## 4. 서비스에서 하는 일과 시퀀스 다이어그램

### 주요 서비스 메소드

#### ScheduleService
1. **create()**: 일정 생성 및 캘린더 엔트리 생성
2. **update()**: 일정 수정 (전체/현재 이후)
3. **delete()**: 일정 삭제 (개별 캘린더/전체)
4. **getSchedulesGroupedByScheduleType()**: 타입별 일정 조회
5. **createDayMessage()**: 하루 메시지 생성
6. **createOrUpdateSleepInfo()**: 수면 정보 관리
7. **createOrUpdateTodoList()**: 할 일 목록 관리

#### ScheduleTempService
1. **create()**: 자녀의 일정 생성 요청
2. **update()**: 자녀의 일정 수정 요청
3. **updateConfirm()**: 부모의 승인/거절 처리

### 일정 생성 승인 워크플로우

```mermaid
sequenceDiagram
    participant Child as 자녀
    participant STC as ScheduleTempController
    participant STS as ScheduleTempService
    participant Parent as 부모
    participant SS as ScheduleService
    participant CS as CalendarService
    
    Child->>STC: POST /api/user/schedule/request
    STC->>STS: create(dto, childEmail)
    STS->>STS: ScheduleTemp 생성 (confirmStatus=WAIT)
    STS-->>STC: ScheduleTempDto
    STC-->>Child: 201 Created
    
    Note over Parent: 승인 대기 목록 확인
    
    Parent->>STC: POST /api/user/schedule/confirm/{id}
    STC->>STS: updateConfirm(id, COMPLETE, parentEmail)
    
    STS->>SS: create(scheduleData)
    SS->>SS: Schedule 엔티티 생성
    SS->>CS: create(schedule, memberId)
    
    CS->>CS: 반복 규칙 해석
    CS->>CS: Calendar 엔트리 생성
    CS->>CS: ScheduleAlarm 생성
    
    STS->>STS: ScheduleTemp 삭제
    STS-->>STC: 완료
    STC-->>Parent: 200 OK
```

### 반복 일정 업데이트 시퀀스

```mermaid
sequenceDiagram
    participant U as User
    participant SC as ScheduleController
    participant SS as ScheduleService
    participant CS as CalendarService
    participant DB as Database
    
    U->>SC: PUT /api/user/schedule/{id}
    SC->>SS: update(id, dto, userEmail)
    
    alt updateType == ALL
        SS->>DB: 기존 Calendar 모두 삭제
        SS->>CS: 전체 기간 재생성
    else updateType == CURRENT_ALL
        SS->>SS: 새 Schedule 생성
        SS->>DB: 오늘 이후 Calendar 삭제
        SS->>CS: 오늘부터 재생성
        SS->>DB: 기존 Schedule 종료일 = 어제
    end
    
    CS->>CS: 새 Calendar 엔트리 생성
    CS->>CS: 알림 재설정
    
    SS-->>SC: ScheduleDto
    SC-->>U: 200 OK
```

### 결제 일정 처리

```mermaid
sequenceDiagram
    participant U as User
    participant SS as ScheduleService
    participant CS as CalendarService
    participant AS as AlarmService
    
    U->>SS: create(scheduleDto with usePay=true)
    SS->>SS: Schedule 생성
    
    SS->>CS: create(schedule)
    CS->>CS: 일반 Calendar 생성
    
    alt usePay == true
        CS->>CS: PayCycle에 따른 결제 Calendar 생성
        loop 각 결제일
            CS->>CS: PAY 타입 Calendar 생성
            CS->>AS: 결제 알림 생성
        end
    end
    
    CS-->>SS: 완료
    SS-->>U: ScheduleDto
```

## 5. 개선점

### 1. **ScheduleTemp 자동 정리**
```java
@Scheduled(cron = "0 0 3 * * *")
public void cleanupExpiredScheduleRequests() {
    LocalDateTime expiry = LocalDateTime.now().minusDays(30);
    scheduleTempRepository.deleteByCreatedAtBeforeAndConfirmStatus(
        expiry, ConfirmStatus.WAIT
    );
}
```

### 2. **소프트 삭제 구현**
```java
@Entity
public class Schedule extends CommonEntity {
    @Column
    private Boolean isDeleted = false;
    
    @Column
    private LocalDateTime deletedAt;
    
    @Column
    private String deletedBy;
}
```

### 3. **TodoList 정규화**
```java
@Entity
public class TodoItem extends CommonEntity {
    @ManyToOne
    private TodoList todoList;
    
    private String content;
    private Boolean isCompleted;
    private Integer orderIndex;
}
```

### 4. **일정 충돌 검사**
```java
public boolean hasScheduleConflict(Schedule newSchedule) {
    List<Calendar> conflicts = calendarRepository.findOverlapping(
        newSchedule.getCommonMember().getId(),
        newSchedule.getStartDateTime(),
        newSchedule.getEndDateTime()
    );
    return !conflicts.isEmpty();
}
```

### 5. **일정 템플릿 기능**
```java
@Entity
public class ScheduleTemplate {
    private String name;
    private ScheduleType type;
    private String defaultTitle;
    private String defaultColor;
    private Duration defaultDuration;
    private CycleType suggestedCycle;
}
```

### 6. **알림 삭제 코드 복원**
```java
// 현재 주석 처리된 알림 삭제 로직 복원
private void deleteScheduleAlarms(List<Calendar> calendars) {
    calendars.forEach(calendar -> {
        scheduleAlarmRepository.deleteAllByCalendar(calendar);
    });
}
```

### 7. **업데이트 전략 개선**
```java
// CURRENT_ALL 업데이트 시 새 Schedule 생성 대신 기존 수정
public void updateFromDate(Schedule schedule, LocalDate fromDate) {
    // 기존 Calendar만 수정
    List<Calendar> futureCalendars = calendarRepository
        .findByScheduleAndStartDateAfter(schedule, fromDate);
    
    futureCalendars.forEach(calendar -> {
        updateCalendarFromSchedule(calendar, schedule);
    });
}
```

### 8. **일정 동기화**
```java
public interface ScheduleSyncService {
    void syncWithGoogleCalendar(CommonMember member);
    void importFromICS(MultipartFile icsFile, CommonMember member);
    void exportToICS(List<Schedule> schedules);
}
```

### 9. **일정 공유 기능**
```java
@Entity
public class ScheduleShare {
    @ManyToOne
    private Schedule schedule;
    
    @ManyToOne
    private CommonMember sharedWith;
    
    private SharePermission permission;  // VIEW, EDIT
    private LocalDateTime expiresAt;
}
```

### 10. **성능 최적화**
```java
// N+1 문제 해결
@Query("SELECT s FROM Schedule s " +
       "LEFT JOIN FETCH s.calendars " +
       "LEFT JOIN FETCH s.academy " +
       "WHERE s.commonMember.id = :memberId")
List<Schedule> findByMemberIdWithDetails(@Param("memberId") Long memberId);
```