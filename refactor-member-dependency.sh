#!/bin/bash

# community-domain에서 CommonMemberRepository를 MemberInfoProvider로 리팩토링하는 스크립트

TARGET_DIR="/Users/jongwon/dev/works/orange-school/orangeschool_api_server_v2/domain-modules/community-domain"

echo "Refactoring CommonMemberRepository to MemberInfoProvider..."

# 1. Service 클래스에서 CommonMemberRepository 필드를 MemberInfoProvider로 변경
find "${TARGET_DIR}" -name "*Service.java" -exec sed -i '' \
    -e 's/private final CommonMemberRepository commonMemberRepository;/private final MemberInfoProvider memberInfoProvider;/g' \
    {} \;

# 2. Service 메소드에서 commonMemberRepository를 memberInfoProvider로 변경
find "${TARGET_DIR}" -name "*Service.java" -exec sed -i '' \
    -e 's/commonMemberRepository\./memberInfoProvider./g' \
    {} \;

# 3. findById를 getMemberInfo로 변경
find "${TARGET_DIR}" -name "*Service.java" -exec sed -i '' \
    -e 's/memberInfoProvider\.findById(/memberInfoProvider.getMemberInfo(/g' \
    {} \;

# 4. Entity에서 CommonMember를 직접 참조하는 부분을 Long memberId로 변경하기 위한 준비
echo "Note: Entity files need manual review for @ManyToOne CommonMember references"

# 5. Import 정리
find "${TARGET_DIR}" -name "*.java" -exec sed -i '' \
    -e '/import com\.orangeschool\.member\.commonMember\.repository\.CommonMemberRepository;/d' \
    {} \;

echo "Refactoring completed!"
echo ""
echo "Manual tasks required:"
echo "1. Update entity relationships from @ManyToOne CommonMember to Long memberId"
echo "2. Update service logic to use MemberInfo DTOs instead of CommonMember entities"
echo "3. Fix any compilation errors related to changed method signatures"