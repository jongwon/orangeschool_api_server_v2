#!/bin/bash

echo "=========================================="
echo "Community Domain 최종 수정"
echo "=========================================="

# 1. MemberInfoProvider import 수정
echo "1. MemberInfoProvider import 수정..."
find domain-modules/community-domain/src/main/java -name "*.java" -type f | while read file; do
    sed -i '' 's/import com\.orangeschool\.member\.api\.MemberInfoProvider;/import com.orangeschool.member.api.service.MemberInfoProvider;/g' "$file"
done

# 2. utilss -> utils 수정
echo "2. utilss -> utils 수정..."
find domain-modules/community-domain/src/main/java -name "*.java" -type f | while read file; do
    sed -i '' 's/com\.orangeschool\.common\.utilss/com.orangeschool.common.utils/g' "$file"
done

# 3. StoryImageRepository 수정
echo "3. StoryImageRepository 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/story/story/repository/StoryImageRepository.java << 'EOF'
package com.orangeschool.community.story.story.repository;

import com.orangeschool.community.story.story.entity.StoryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import java.util.List;

@Repository
public interface StoryImageRepository extends JpaRepository<StoryImage, Long> {
    List<StoryImage> findByStoryId(Long storyId);
    
    @Transactional
    void deleteByImageUrl(String imageUrl);
}
EOF

# 4. PickImageRepository 수정 (jakarta.transaction으로 변경)
echo "4. PickImageRepository jakarta 패키지로 수정..."
if [ -f "domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/pick/repository/PickImageRepository.java" ]; then
    sed -i '' 's/import javax\.transaction/import jakarta.transaction/g' \
        domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/pick/repository/PickImageRepository.java
fi

# 5. FileManagement import를 infra 모듈로 수정
echo "5. FileManagement import 경로 수정..."
find domain-modules/community-domain/src/main/java -name "*.java" -type f | while read file; do
    if grep -q "FileManagement" "$file"; then
        # common.utils에서 infra.util로 변경
        sed -i '' 's/import com\.orangeschool\.common\.utils\.FileManagement;/import com.orangeschool.infra.util.FileManagement;/g' "$file"
    fi
done

# 6. PickService에서 manager 관련 로직 제거/수정
echo "6. PickService manager 관련 코드 수정..."
if [ -f "domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/pick/service/PickService.java" ]; then
    # Manager 관련 필드와 로직 제거 (필요시 MemberInfoProvider로 대체)
    sed -i '' '/private final ManagerRepository managerRepository;/d' \
        domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/pick/service/PickService.java
fi

# 7. @Repository 어노테이션 정리
echo "7. @Repository 어노테이션 정리..."
find domain-modules/community-domain/src/main/java -name "*Repository.java" -type f | while read file; do
    # @Repositorypublic 같은 잘못된 형태 수정
    sed -i '' 's/@Repositorypublic interface/@Repository\
public interface/g' "$file"
done

# 8. QueryDSL 재생성을 위한 clean
echo "8. QueryDSL clean..."
./gradlew :domain-modules:community-domain:clean

# 9. build.gradle 확인 메시지
echo "=========================================="
echo "수정 완료!"
echo ""
echo "build.gradle에 다음 dependency가 있는지 확인하세요:"
echo "  implementation project(':infra-module')"
echo ""
echo "컴파일 명령어:"
echo "./gradlew :domain-modules:community-domain:compileJava"
echo "=========================================="