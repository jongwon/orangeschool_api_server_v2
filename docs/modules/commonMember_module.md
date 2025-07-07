# CommonMember 모듈 분석

## 1. 모듈이 담당하는 역할

CommonMember 모듈은 오렌지스쿨 플랫폼의 핵심 사용자 관리 시스템으로, 다음과 같은 기능을 담당합니다:

- 부모/자녀 이중 계정 체계 관리
- 일반/소셜 로그인 인증 시스템
- 부모-자녀 계정 연결 및 관리
- 추천인 코드 시스템 운영
- 지역 기반 커뮤니티 (동네친구) 기능
- 프로필 및 학교 정보 관리
- 알림 설정 및 푸시 토큰 관리
- SMS 인증 시스템

## 2. 도메인 객체 구조도

```mermaid
classDiagram
    class CommonEntity {
        +Long id
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
    }
    
    class CommonMember {
        +JoinType joinType
        +MemberType memberType
        +String email
        +String password
        +String socialToken
        +String name
        +LocalDate birth
        +String gender
        +String phoneNumber
        +String pushToken
        
        +String nickName
        +String parentNickName
        +String intro
        +String fileUrl
        
        +String address
        +String locationCode
        +String regionNameTag
        +String regionCodeTag
        
        +String schoolCode
        +String schoolName
        +Integer grade
        +Integer schoolClass
        +Integer classNumber
        +String timetableEdit
        
        +Long parentId
        +String parentName
        
        +String myReferralCode
        +String referralCode
        +String referralCodeTemp
        
        +Boolean challengeProgress
        +Integer totalOrange
        +Integer totalOrangeAll
        
        +Boolean agreeToSms
        +Boolean agreeToService
        +Boolean agreeToAd
        +Boolean agreeToSchedule
        +Boolean isActive
    }
    
    CommonEntity <|-- CommonMember
    
    CommonMember "1" --> "0..*" CommonMember : children (parentId)
    
    class MemberType {
        <<enumeration>>
        NONE("")
        PARENT("보호자")
        CHILD("자녀")
    }
    
    class JoinType {
        <<enumeration>>
        NORMAL("일반")
        KAKAO("카카오")
        GOOGLE("구글")
        APPLE("애플")
        NAVER("네이버")
    }
    
    CommonMember --> MemberType
    CommonMember --> JoinType
    
    note for CommonMember "부모와 자녀 모두를 표현하는 통합 엔티티\nparentId=0L이면 부모 계정"
```

## 3. 모듈 연관성 분석

### 중심 허브 역할
```mermaid
graph TD
    CM[CommonMember] -->|소유| A[Academy]
    CM -->|생성| S[Schedule]
    CM -->|수신| AL[Alarm]
    CM -->|참여| CH[Challenge]
    CM -->|작성| ST[Story]
    CM -->|팔로우| F[Follow]
    CM -->|응원| CHR[Cheering]
    CM -->|신고| R[Report]
    CM -->|조회| B[Banner]
    CM -->|보유| MA[MemberAcademy]
    CM -->|읽음| MN[MemberNotice]
    
    P[Parent CommonMember] -->|관리| C[Child CommonMember]
    
    style CM fill:#f9f,stroke:#333,stroke-width:4px
```

### 인증 및 보안 흐름
```mermaid
graph LR
    A[Client] -->|로그인 요청| B[CommonController]
    B -->|인증| C[CommonMemberService]
    C -->|검증| D[Database]
    C -->|토큰 생성| E[JwtTokenProvider]
    E -->|JWT| A
    
    A -->|API 요청 + JWT| F[JwtAuthenticationFilter]
    F -->|검증| E
    F -->|인증 성공| G[Protected Resources]
```

## 4. 서비스에서 하는 일과 시퀀스 다이어그램

### 주요 서비스 메소드

#### CommonMemberService (v1)
1. **join()**: 회원가입 (이메일/전화번호 검증)
2. **login()**: 일반 로그인
3. **socialLogin()**: 소셜 로그인
4. **findEmail()**: 이메일 찾기
5. **resetPassword()**: 비밀번호 재설정
6. **update()**: 프로필 업데이트
7. **createChildAccountByParent()**: 자녀 계정 생성

#### CommonMemberServiceV2
1. **updateSettingRegion()**: 지역 설정 업데이트
2. **updateParentNickname()**: 부모 닉네임 변경
3. **updateSettingReferralCode()**: 추천인 코드 요청
4. **updateReferralConfirm()**: 추천인 승인/거절
5. **checkReferralCode()**: 추천인 코드 유효성 검증

### 회원가입 시퀀스 다이어그램

```mermaid
sequenceDiagram
    participant U as User
    participant C as CommonController
    participant CS as CommonMemberService
    participant SMS as SmsManagement
    participant DB as Database
    participant JWT as JwtTokenProvider
    
    U->>C: POST /api/common/join
    C->>CS: join(CreateCommonMemberDto)
    
    CS->>DB: 이메일 중복 체크
    CS->>DB: 전화번호 중복 체크
    
    alt memberType == PARENT
        CS->>DB: parentNickname 중복 체크
    end
    
    CS->>SMS: 전화번호 인증 확인
    SMS-->>CS: 인증 성공
    
    CS->>CS: 비밀번호 암호화
    CS->>CS: 추천인 코드 생성 (8자리)
    CS->>DB: CommonMember 저장
    
    CS->>JWT: 토큰 생성
    JWT-->>CS: AuthDto
    
    CS-->>C: AuthDto
    C-->>U: 200 OK with JWT
```

### 부모-자녀 연결 시퀀스 다이어그램

```mermaid
sequenceDiagram
    participant P as Parent
    participant CC as CommonController
    participant CS as CommonMemberService
    participant DB as Database
    
    P->>CC: POST /api/user/child
    CC->>CS: createChildAccountByParent(dto, parentEmail)
    
    CS->>DB: 부모 계정 조회
    DB-->>CS: Parent CommonMember
    
    CS->>CS: 자녀 계정 생성
    Note over CS: parentId = parent.getId()<br/>memberType = CHILD<br/>부모 주소 정보 복사
    
    CS->>DB: 자녀 CommonMember 저장
    
    CS-->>CC: CommonMemberDto
    CC-->>P: 201 Created
```

### 추천인 코드 승인 워크플로우

```mermaid
sequenceDiagram
    participant A as 신규 회원
    participant B as 기존 회원
    participant S as ServiceV2
    participant DB as Database
    participant AL as AlarmService
    
    Note over A: 가입 시 추천인 코드 입력
    A->>S: updateSettingReferralCode(referralCode)
    S->>DB: 추천인 코드 소유자 조회
    S->>DB: 이미 3명 이상 사용 확인
    
    alt 사용 가능
        S->>DB: referralCodeTemp 설정
        S->>AL: 승인 요청 알림 발송
        AL->>B: 푸시 알림
    else 사용 불가
        S-->>A: 에러 (이미 3명 사용)
    end
    
    B->>S: updateReferralConfirm(COMPLETE)
    S->>DB: referralCode 정식 설정
    S->>DB: referralCodeTemp 제거
    S->>AL: 승인 완료 알림
    AL->>A: 푸시 알림
```

## 5. 개선점

### 1. **엔티티 분리**
```java
// 현재: 하나의 거대한 엔티티
// 개선: 관심사별 분리
@Entity
public class MemberProfile {
    @OneToOne
    private CommonMember member;
    private String nickName;
    private String intro;
    private String fileUrl;
}

@Entity
public class MemberSchool {
    @OneToOne
    private CommonMember member;
    private String schoolCode;
    private String schoolName;
    private Integer grade;
}
```

### 2. **상속 구조 도입**
```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "memberType")
public abstract class CommonMember extends CommonEntity {
    // 공통 필드
}

@Entity
@DiscriminatorValue("PARENT")
public class ParentMember extends CommonMember {
    private String parentNickName;
    @OneToMany(mappedBy = "parent")
    private List<ChildMember> children;
}

@Entity
@DiscriminatorValue("CHILD")
public class ChildMember extends CommonMember {
    @ManyToOne
    private ParentMember parent;
    private MemberSchool school;
}
```

### 3. **매직 넘버 제거**
```java
// 현재: parentId = 0L은 부모를 의미
// 개선: Optional 사용
@Column
private Long parentId;  // null이면 부모

public Optional<Long> getParentId() {
    return Optional.ofNullable(parentId);
}
```

### 4. **추천인 코드 설정 분리**
```java
@Service
public class ReferralService {
    private static final int MAX_REFERRALS = 3;
    
    @Value("${referral.max-uses:3}")
    private int maxReferralUses;
    
    public void processReferral(CommonMember member, String referralCode) {
        // 추천인 로직 중앙화
    }
}
```

### 5. **이벤트 기반 아키텍처**
```java
@Component
public class MemberEventPublisher {
    @EventListener
    public void handleMemberCreated(MemberCreatedEvent event) {
        // 회원 생성 시 추천인 코드 생성
        // 웰컴 알림 발송
        // 통계 업데이트
    }
}
```

### 6. **검증 로직 분리**
```java
@Component
public class MemberValidator {
    public void validateParentNickname(String nickname) {
        // 부모 닉네임 검증 규칙
    }
    
    public void validateSchoolInfo(MemberSchoolDto school) {
        // 학교 정보 검증 규칙
    }
}
```

### 7. **쿼리 최적화**
```java
@Query("SELECT m FROM CommonMember m " +
       "LEFT JOIN FETCH m.challenges " +
       "LEFT JOIN FETCH m.schedules " +
       "WHERE m.id = :id")
CommonMember findByIdWithDetails(@Param("id") Long id);
```

### 8. **캐싱 전략**
```java
@Cacheable(value = "memberProfile", key = "#email")
public CommonMemberProfileDto getProfile(String email) {
    // 자주 조회되는 프로필 정보 캐싱
}
```

### 9. **소셜 로그인 통합**
```java
@Component
public class SocialLoginAdapter {
    private Map<JoinType, SocialLoginProvider> providers;
    
    public AuthDto authenticate(JoinType type, String token) {
        return providers.get(type).authenticate(token);
    }
}
```

### 10. **감사 로그**
```java
@EntityListeners(AuditingEntityListener.class)
public class CommonMember extends CommonEntity {
    @CreatedBy
    private String createdBy;
    
    @LastModifiedBy
    private String modifiedBy;
    
    @Version
    private Long version;  // 낙관적 잠금
}
```