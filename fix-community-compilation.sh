#!/bin/bash

# Community Domain 컴파일 에러 수정 스크립트

echo "=========================================="
echo "Community Domain 컴파일 에러 수정 시작"
echo "=========================================="

# 1. javax.persistence -> jakarta.persistence 변경
echo "1. JPA 패키지 변경 (javax -> jakarta)..."
find domain-modules/community-domain/src/main/java -name "*.java" -type f | while read file; do
    sed -i '' 's/import javax\.persistence\./import jakarta.persistence./g' "$file"
done

# 2. org.springframework.data.community -> org.springframework.data.domain 변경
echo "2. Spring Data 패키지 수정..."
find domain-modules/community-domain/src/main/java -name "*.java" -type f | while read file; do
    sed -i '' 's/org\.springframework\.data\.community/org.springframework.data.domain/g' "$file"
done

# 3. CommonMember 엔티티 직접 참조를 memberId로 변경
echo "3. CommonMember 엔티티 참조를 memberId로 변경..."

# Entity 파일들 수정
for entity in Follow Cheering PickLike PickComment PickReply StoryLike StoryComment StoryReply; do
    file="domain-modules/community-domain/src/main/java/com/orangeschool/community/*/*/entity/${entity}.java"
    for f in $file; do
        if [ -f "$f" ]; then
            echo "Processing $f..."
            # CommonMember import 제거
            sed -i '' '/import com\.orangeschool\.member\.commonMember\.entity\.CommonMember;/d' "$f"
            
            # @ManyToOne CommonMember를 Long memberId로 변경
            sed -i '' '/@ManyToOne/,/private CommonMember.*member;/{
                /@ManyToOne/d
                /@JoinColumn/d
                s/private CommonMember.*member;/private Long memberId;/
            }' "$f"
            
            # getter/setter 메서드 이름 변경 (필요한 경우)
            sed -i '' 's/getFromMember()/getMemberId()/g' "$f"
            sed -i '' 's/getToMember()/getMemberId()/g' "$f"
            sed -i '' 's/getMember()/getMemberId()/g' "$f"
        fi
    done
done

# 4. MemberInfoProvider 사용하도록 서비스 수정
echo "4. Service 클래스에서 MemberInfoProvider 사용..."

# 서비스 파일들에 import 추가
find domain-modules/community-domain/src/main/java -name "*Service.java" -type f | while read file; do
    # CommonMember import 제거
    sed -i '' '/import com\.orangeschool\.member\.commonMember\.entity\.CommonMember;/d' "$file"
    
    # MemberInfoProvider import 추가 (이미 없는 경우에만)
    if ! grep -q "import com.orangeschool.member.api.service.MemberInfoProvider;" "$file"; then
        # package 선언 다음에 import 추가
        sed -i '' '/^package/a\
\
import com.orangeschool.member.api.service.MemberInfoProvider;\
import com.orangeschool.member.api.dto.MemberInfo;' "$file"
    fi
done

# 5. Repository 구현체 수정
echo "5. Repository 구현체 수정..."

# FollowRepositoryImpl 수정
follow_repo="domain-modules/community-domain/src/main/java/com/orangeschool/community/follow/repository/FollowRepositoryImpl.java"
if [ -f "$follow_repo" ]; then
    # CommonMemberProfileDto import 제거
    sed -i '' '/import com\.orangeschool\.member\.commonMember\.dto\.CommonMemberProfileDto;/d' "$follow_repo"
    
    # QueryDSL import 수정
    sed -i '' 's/import static com\.orangeschool\.member\.commonMember\.entity\.QCommonMember\.commonMember;/\/\/ QCommonMember removed - use memberId instead/g' "$follow_repo"
fi

# CheeringRepositoryImpl 수정
cheering_repo="domain-modules/community-domain/src/main/java/com/orangeschool/community/cheering/repository/CheeringRepositoryImpl.java"
if [ -f "$cheering_repo" ]; then
    # QueryDSL import 수정
    sed -i '' 's/import static com\.orangeschool\.member\.commonMember\.entity\.QCommonMember\.commonMember;/\/\/ QCommonMember removed - use memberId instead/g' "$cheering_repo"
fi

# 6. DTO 클래스 수정
echo "6. DTO 클래스 수정..."

# CheeringDto 수정
cheering_dto="domain-modules/community-domain/src/main/java/com/orangeschool/community/cheering/dto/CheeringDto.java"
if [ -f "$cheering_dto" ]; then
    # CommonMember 관련 import 제거
    sed -i '' '/import com\.orangeschool\.member\.commonMember/d' "$cheering_dto"
    
    # CommonMemberDto를 MemberInfo로 변경
    sed -i '' 's/CommonMemberDto/MemberInfo/g' "$cheering_dto"
    
    # MemberInfo import 추가
    if ! grep -q "import com.orangeschool.member.api.dto.MemberInfo;" "$cheering_dto"; then
        sed -i '' '/^package/a\
\
import com.orangeschool.member.api.dto.MemberInfo;' "$cheering_dto"
    fi
fi

# 7. Controller 수정
echo "7. Controller 클래스 수정..."

# CheeringController 수정
cheering_controller="domain-modules/community-domain/src/main/java/com/orangeschool/community/cheering/controller/CheeringController.java"
if [ -f "$cheering_controller" ]; then
    # CommonMember 관련 import 제거
    sed -i '' '/import com\.orangeschool\.member\.commonMember/d' "$cheering_controller"
fi

# 8. 누락된 import 수정
echo "8. 기타 누락된 import 수정..."

# com.orangeschool.common.util -> com.orangeschool.common.utils 변경
find domain-modules/community-domain/src/main/java -name "*.java" -type f | while read file; do
    sed -i '' 's/com\.orangeschool\.common\.util/com.orangeschool.common.utils/g' "$file"
done

echo "=========================================="
echo "수정 완료! 컴파일을 다시 시도해보세요."
echo "./gradlew :domain-modules:community-domain:compileJava"
echo "=========================================="