#!/bin/bash

echo "=========================================="
echo "Community Domain 전체 수정 마스터 스크립트"
echo "=========================================="

# 1. @EqualsAndHashCode import 추가
echo "1. DTO 클래스에 @EqualsAndHashCode import 추가..."
find domain-modules/community-domain/src/main/java -name "*Dto.java" -type f | while read file; do
    if grep -q "@EqualsAndHashCode" "$file" && ! grep -q "import lombok.EqualsAndHashCode;" "$file"; then
        sed -i '' '/import lombok/a\
import lombok.EqualsAndHashCode;' "$file"
    fi
done

# 2. javax -> jakarta 변경
echo "2. javax -> jakarta 패키지 변경..."
find domain-modules/community-domain/src/main/java -name "*.java" -type f | while read file; do
    sed -i '' 's/import javax\.validation/import jakarta.validation/g' "$file"
    sed -i '' 's/import javax\.transaction/import jakarta.transaction/g' "$file"
done

# 3. common.util -> common.utils 변경
echo "3. common.util -> common.utils 변경..."
find domain-modules/community-domain/src/main/java -name "*.java" -type f | while read file; do
    sed -i '' 's/com\.orangeschool\.common\.util/com.orangeschool.common.utils/g' "$file"
    sed -i '' 's/com\.orangeschool\.common\.utilss/com.orangeschool.common.utils/g' "$file"
done

# 4. CommonMember 엔티티 참조 제거
echo "4. CommonMember 엔티티 참조 제거..."
find domain-modules/community-domain/src/main/java -name "*.java" -type f | while read file; do
    sed -i '' '/import com\.orangeschool\.member\.commonMember\.entity\.CommonMember;/d' "$file"
done

# 5. PickCommentDto 수정
echo "5. PickCommentDto 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/comment/dto/PickCommentDto.java << 'EOF'
package com.orangeschool.community.pick.comment.dto;

import com.orangeschool.member.api.dto.MemberInfo;
import com.orangeschool.community.pick.reply.dto.PickReplyDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class PickCommentDto {
    private Long id;
    private Long pickId;
    private MemberInfo member;
    private String content;
    private LocalDateTime createdAt;
    private List<PickReplyDto> replies;
}
EOF

# 6. PickReplyDto 수정
echo "6. PickReplyDto 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/reply/dto/PickReplyDto.java << 'EOF'
package com.orangeschool.community.pick.reply.dto;

import com.orangeschool.member.api.dto.MemberInfo;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class PickReplyDto {
    private Long id;
    private Long pickCommentId;
    private MemberInfo member;
    private String content;
    private LocalDateTime createdAt;
}
EOF

# 7. StoryCommentDto 수정
echo "7. StoryCommentDto 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/story/comment/dto/StoryCommentDto.java << 'EOF'
package com.orangeschool.community.story.comment.dto;

import com.orangeschool.member.api.dto.MemberInfo;
import com.orangeschool.community.story.reply.dto.StoryReplyDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class StoryCommentDto {
    private Long id;
    private Long storyId;
    private MemberInfo member;
    private String content;
    private LocalDateTime createdAt;
    private List<StoryReplyDto> replies;
}
EOF

# 8. StoryReplyDto 수정
echo "8. StoryReplyDto 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/story/reply/dto/StoryReplyDto.java << 'EOF'
package com.orangeschool.community.story.reply.dto;

import com.orangeschool.member.api.dto.MemberInfo;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class StoryReplyDto {
    private Long id;
    private Long storyCommentId;
    private MemberInfo member;
    private String content;
    private LocalDateTime createdAt;
}
EOF

# 9. CheeringRequestDto 생성
echo "9. CheeringRequestDto 생성..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/cheering/dto/CheeringRequestDto.java << 'EOF'
package com.orangeschool.community.cheering.dto;

import com.orangeschool.common.enums.CheeringMessage;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class CheeringRequestDto {
    private Long cheeringMemberId;
    private Long cheeredMemberId;
    private CheeringMessage message;
}
EOF

# 10. PickService 수정 (manager 패키지 제거)
echo "10. PickService manager 패키지 import 제거..."
sed -i '' '/import com\.orangeschool\.community\.manager/d' \
    domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/pick/service/PickService.java

# 11. 모든 Repository 인터페이스에 @Repository 추가
echo "11. Repository 인터페이스에 @Repository 어노테이션 추가..."
find domain-modules/community-domain/src/main/java -name "*Repository.java" -type f | while read file; do
    if ! grep -q "@Repository" "$file" && ! grep -q "RepositoryImpl" "$file" && ! grep -q "RepositoryCustom" "$file"; then
        if ! grep -q "import org.springframework.stereotype.Repository;" "$file"; then
            sed -i '' '/import org.springframework.data.jpa.repository.JpaRepository;/a\
import org.springframework.stereotype.Repository;' "$file"
        fi
        sed -i '' '/public interface.*Repository/i\
@Repository' "$file"
    fi
done

# 12. QueryDSL Q클래스 import 수정
echo "12. QueryDSL Q클래스 import 수정..."
for repo_impl in $(find domain-modules/community-domain/src/main/java -name "*RepositoryImpl.java" -type f); do
    # Q클래스 static import 추가
    entity_name=$(basename "$repo_impl" | sed 's/RepositoryImpl.java//')
    if grep -q "Q${entity_name}" "$repo_impl"; then
        if ! grep -q "import static.*Q${entity_name}" "$repo_impl"; then
            package_path=$(dirname "$repo_impl" | sed 's/repository/entity/' | sed 's/.*src\/main\/java\///' | tr '/' '.')
            sed -i '' "/import com.querydsl/a\\
import static ${package_path}.Q${entity_name}.$(echo ${entity_name} | sed 's/\([A-Z]\)/\L\1/g');" "$repo_impl"
        fi
    fi
done

echo "=========================================="
echo "전체 수정 완료!"
echo "컴파일을 다시 시도하세요:"
echo "./gradlew :domain-modules:community-domain:clean :domain-modules:community-domain:compileJava"
echo "=========================================="