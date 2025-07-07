# CommonMemberController 기능 분석

## 개요
CommonMemberController는 Orange School API 서버의 핵심 컨트롤러로, 회원 관련 거의 모든 기능을 담당하고 있습니다. 현재 **26개의 엔드포인트**를 관리하고 있어 단일 책임 원칙(SRP) 관점에서 리팩토링이 필요해 보입니다.

## 주요 기능 분류

### 1. 인증 및 회원가입 (Authentication) - 9개 엔드포인트
| 메서드 | 경로 | 설명 | 권한 |
|--------|------|------|------|
| POST | `/api/common/join` | 회원가입 | 공개 |
| POST | `/api/common/check/email` | 이메일 중복 체크 | 공개 |
| POST | `/api/common/check/nickname` | 닉네임 중복 체크 | 공개 |
| POST | `/api/common/check/phoneNumber` | SMS 인증 요청 | 공개 |
| POST | `/api/common/find/email/check/phoneNumber` | SMS 인증 요청 (이메일 찾기용) | 공개 |
| POST | `/api/common/login` | 로그인 | 공개 |
| POST | `/api/common/login/social` | 소셜 로그인 | 공개 |
| GET | `/api/common/find/email` | 이메일 찾기 | 공개 |
| GET | `/api/common/find/password` | 비밀번호 찾기 | 공개 |
| POST | `/api/common/reset/password` | 비밀번호 재설정 | 공개 |

### 2. 회원 정보 조회 (Member Read) - 6개 엔드포인트
| 메서드 | 경로 | 설명 | 권한 |
|--------|------|------|------|
| GET | `/api/user/commonMember` | 본인 정보 조회 | USER |
| GET | `/api/user/commonMembers` | 자녀 목록 조회 | USER |
| GET | `/api/user/commonMember/{id}` | 자녀 정보 조회 | USER |
| GET | `/api/admin/commonMembers` | 전체 회원 목록 조회 | ADMIN |
| GET | `/api/admin/commonMember/{id}` | 특정 회원 조회 | ADMIN |
| GET | `/api/admin/commonMembers/{id}` | 특정 회원의 자녀 목록 | ADMIN |

### 3. 회원 정보 수정 (Member Update) - 6개 엔드포인트
| 메서드 | 경로 | 설명 | 권한 |
|--------|------|------|------|
| PUT | `/api/user/commonMember` | 본인 정보 수정 | USER |
| PUT | `/api/user/commonMember/{id}` | 자녀 정보 수정 | USER |
| PUT | `/api/user/commonMember/school/{id}` | 자녀 학교 정보 수정 | USER |
| PUT | `/api/user/commonMember/setting/alarm` | 알림 설정 수정 | USER |
| PUT | `/api/admin/commonMember/{id}` | 회원 정보 수정 (관리자) | ADMIN |
| PUT | `/api/admin/commonMember/activation/{id}` | 회원 활성화 상태 수정 | ADMIN |

### 4. 회원 삭제 (Member Delete) - 2개 엔드포인트
| 메서드 | 경로 | 설명 | 권한 |
|--------|------|------|------|
| DELETE | `/api/admin/commonMember/{id}` | 회원 삭제 (관리자) | ADMIN |
| DELETE | `/api/user/child/{id}` | 자녀 삭제 | USER |

## 문제점 분석

### 1. 과도한 책임
- 인증, 회원가입, 프로필 관리, 자녀 관리 등 다양한 책임을 하나의 컨트롤러가 담당
- 26개의 엔드포인트는 단일 컨트롤러로는 너무 많음

### 2. 일관성 부족
- 자녀 삭제는 `/user/child/{id}`인데 자녀 조회는 `/user/commonMember/{id}`
- 경로 명명 규칙이 일관되지 않음

### 3. 관심사 혼재
- 공개 API(인증), 사용자 API, 관리자 API가 하나의 컨트롤러에 혼재

## 리팩토링 제안

### 1. 컨트롤러 분리 방안

#### AuthController (인증 관련)
```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    // 로그인, 소셜 로그인
    // 이메일/비밀번호 찾기
    // 비밀번호 재설정
}
```

#### RegistrationController (회원가입 관련)
```java
@RestController
@RequestMapping("/api/registration")
public class RegistrationController {
    // 회원가입
    // 이메일/닉네임 중복 체크
    // SMS 인증
}
```

#### MemberProfileController (회원 프로필 관리)
```java
@RestController
@RequestMapping("/api/user/profile")
public class MemberProfileController {
    // 본인 정보 조회/수정
    // 알림 설정
}
```

#### ChildManagementController (자녀 관리)
```java
@RestController
@RequestMapping("/api/user/children")
public class ChildManagementController {
    // 자녀 목록 조회
    // 자녀 정보 조회/수정/삭제
    // 자녀 학교 정보 수정
}
```

#### AdminMemberController (관리자 기능)
```java
@RestController
@RequestMapping("/api/admin/members")
public class AdminMemberController {
    // 전체 회원 관리
    // 회원 활성화 상태 관리
}
```

### 2. 서비스 레이어 분리
현재 CommonMemberService도 너무 많은 책임을 가지고 있으므로, 각 도메인별로 서비스를 분리:
- AuthService
- RegistrationService
- MemberProfileService
- ChildManagementService
- MemberAdminService

### 3. 일관된 URL 패턴
```
/api/auth/login
/api/auth/logout
/api/auth/forgot-password

/api/registration/signup
/api/registration/check-email
/api/registration/verify-phone

/api/user/profile
/api/user/profile/settings

/api/user/children
/api/user/children/{childId}
/api/user/children/{childId}/school

/api/admin/members
/api/admin/members/{memberId}
/api/admin/members/{memberId}/activation
```

## 우선순위 제안

1. **단계 1**: 인증 관련 기능 분리 (AuthController)
   - 가장 독립적이고 명확한 도메인
   - 보안 관련 로직 집중 관리 가능

2. **단계 2**: 회원가입 기능 분리 (RegistrationController)
   - 인증과 밀접하지만 별도 관심사
   - 검증 로직 집중화

3. **단계 3**: 자녀 관리 기능 분리 (ChildManagementController)
   - 명확한 도메인 경계
   - 부모-자녀 관계 로직 집중화

4. **단계 4**: 관리자 기능 분리 (AdminMemberController)
   - 권한별 분리로 보안 강화
   - 관리자 전용 로직 격리

5. **단계 5**: 나머지 프로필 관리 기능 정리

## 기대 효과

1. **유지보수성 향상**: 각 컨트롤러가 명확한 책임을 가짐
2. **테스트 용이성**: 작은 단위로 분리되어 단위 테스트 작성 용이
3. **확장성**: 새로운 기능 추가 시 적절한 컨트롤러 선택 용이
4. **가독성**: 각 파일이 작아져 코드 이해도 향상
5. **보안**: 권한별 분리로 접근 제어 명확화