# 회원 관련 컨트롤러 재구성 분석

## 현재 상황
3개의 컨트롤러에 총 **40개의 엔드포인트**가 분산되어 있음:
- CommonMemberController: 26개
- CommonMemberControllerV2: 9개
- TownFriendController: 5개

## 재구성 방안

### 1. AuthController (인증 관련) - 5개 엔드포인트
- 로그인 (`POST /api/common/login`)
- 소셜 로그인 (`POST /api/common/login/social`)
- 이메일 찾기 (`GET /api/common/find/email`)
- 비밀번호 찾기 (`GET /api/common/find/password`)
- 비밀번호 재설정 (`POST /api/common/reset/password`)

### 2. RegistrationController (회원가입 관련) - 6개 엔드포인트
- 회원가입 (`POST /api/common/join`)
- 이메일 중복 체크 (`POST /api/common/check/email`)
- 닉네임 중복 체크 (`POST /api/common/check/nickname`)
- SMS 인증 요청 (`POST /api/common/check/phoneNumber`)
- SMS 인증 요청 - 이메일찾기 (`POST /api/common/find/email/check/phoneNumber`)
- 초대코드 체크 (`POST /api/common/check/referralCode`) - V2에서 이동

### 3. MemberProfileController (회원 프로필 관리) - 6개 엔드포인트
- 본인 정보 조회 (`GET /api/user/commonMember`)
- 회원 수정 (`PUT /api/user/commonMember`)
- 회원 알림 설정 (`PUT /api/user/commonMember/setting/alarm`)
- 회원 우리 동네 설정 (`PUT /api/user/v2/commonMember/setting/region`) - V2에서 이동
- 회원 닉네임 수정 (`PUT /api/user/v2/commonMember`) - V2에서 이동
- 초대코드 재설정 (`PUT /api/user/v2/commonMember/setting/referralCode`) - V2에서 이동

### 4. ChildManagementController (자녀 관리) - 7개 엔드포인트
- 자녀 목록 조회 (`GET /api/user/commonMembers`)
- 자녀 정보 조회 (`GET /api/user/commonMember/{id}`)
- 자녀 회원 수정 (`PUT /api/user/commonMember/{id}`)
- 자녀 학교 수정 (`PUT /api/user/commonMember/school/{id}`)
- 자녀 삭제 (`DELETE /api/user/child/{id}`)
- 학교 시간 편집 (`PUT /api/user/v2/commonMember/setting/timetable`) - V2에서 이동
- 학교 시간 편집 조회 (`GET /api/user/v2/timetable/{id}`) - V2에서 이동

### 5. FamilyManagementController (가족/구성원 관리) - 4개 엔드포인트
- 구성원 요청 목록 조회 (`GET /api/user/v2/referral/request`) - V2에서 이동
- 구성원 요청 목록 조회 (관리자) (`GET /api/admin/v2/commonMember/family/{id}`) - V2에서 이동
- 구성원 승인/반려/삭제 (`POST /api/user/v2/referral/request/confirm/{id}`) - V2에서 이동
- 가족 구성원 조회 (기존 기능 확장)

### 6. TownFriendController (동네 친구 기능) - 5개 엔드포인트 (그대로 유지)
- 내 프로필 (`GET /api/user/my/profile/{id}`)
- 친구 챌린지 목록 (`GET /api/user/townFriends/{id}`)
- 최다 오렌지 보유 친구 목록 (`GET /api/user/townFriends/top/{id}`)
- 동네 친구들 검색 (`GET /api/user/search/townFriends/{id}`)
- 프로필 조회 (`GET /api/user/townFriend/{childId}/{id}`)

### 7. AdminMemberController (관리자 기능) - 7개 엔드포인트
- 목록 조회 (`GET /api/admin/commonMembers`)
- 단일 조회 (`GET /api/admin/commonMember/{id}`)
- 자녀 목록 조회 (`GET /api/admin/commonMembers/{id}`)
- 수정 (`PUT /api/admin/commonMember/{id}`)
- 활성상태 수정 (`PUT /api/admin/commonMember/activation/{id}`)
- 삭제 (`DELETE /api/admin/commonMember/{id}`)
- 가족 구성원 조회 (`GET /api/admin/v2/commonMember/family/{id}`) - FamilyManagementController와 중복 검토 필요

## 주요 변경사항
1. CommonMemberControllerV2의 기능들을 적절한 컨트롤러로 분산
2. 가족/구성원 관리를 위한 새로운 컨트롤러 추가 (FamilyManagementController)
3. 자녀 관련 기능에 시간표 관리 기능 통합
4. TownFriendController는 독립적인 기능이므로 그대로 유지