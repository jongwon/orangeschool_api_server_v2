#!/bin/bash

echo "=========================================="
echo "Community Domain 최종 수정 (Story 엔티티 포함)"
echo "=========================================="

# 1. Story 엔티티 수정
echo "1. Story 엔티티 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/story/story/entity/Story.java << 'EOF'
package com.orangeschool.community.story.story.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.community.story.comment.entity.StoryComment;
import com.orangeschool.community.story.like.entity.StoryLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Story extends BaseEntity {

    private Long memberId;

    private String title;
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    // 지역 태그
    private String regionTag;

    // 이미지 모음
    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<StoryImage> images = new HashSet<>();

    // 댓글 모음
    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<StoryComment> storyComments = new HashSet<>();

    // 좋아요 모음
    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<StoryLike> storyLikes = new HashSet<>();

    // 부모 타입
    @Enumerated(EnumType.STRING)
    private com.orangeschool.common.enums.MemberType memberType;

    // 활성화 여부
    @Column(columnDefinition = "boolean default false")
    private boolean activation;

    // 차단 여부
    @Column(columnDefinition = "boolean default false")
    private boolean isBlock;

    // 수정
    public void update(String title, String content, String regionTag) {
        this.title = title;
        this.content = content;
        this.regionTag = regionTag;
    }

    public void addImages(Set<StoryImage> images) {
        this.images.addAll(images);
        images.forEach(image -> image.setStory(this));
    }

    public void removeImage(StoryImage image) {
        this.images.remove(image);
        image.setStory(null);
    }

    public void updateActivation(boolean activation) {
        this.activation = activation;
    }
}
EOF

# 2. StoryDto 수정 (Functions import 제거)
echo "2. StoryDto 수정..."
sed -i '' '/import com\.orangeschool\.common\.utils\.Functions;/d' \
    domain-modules/community-domain/src/main/java/com/orangeschool/community/story/story/dto/StoryDto.java

# 3. StoryCommentService에서 MemberAlarmService 제거
echo "3. StoryCommentService에서 MemberAlarmService 제거..."
sed -i '' '/import com\.orangeschool\.community\.memberAlarm/d' \
    domain-modules/community-domain/src/main/java/com/orangeschool/community/story/comment/service/StoryCommentService.java
sed -i '' '/private final MemberAlarmService memberAlarmService;/d' \
    domain-modules/community-domain/src/main/java/com/orangeschool/community/story/comment/service/StoryCommentService.java

# 4. PickService에서 Manager 관련 코드 제거
echo "4. PickService에서 Manager 관련 코드 수정..."
if [ -f "domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/pick/service/PickService.java" ]; then
    # managerId를 memberId로 변경하는 더 간단한 방법
    sed -i '' 's/Long managerId/Long memberId/g' \
        domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/pick/service/PickService.java
    sed -i '' 's/managerId/memberId/g' \
        domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/pick/service/PickService.java
    sed -i '' '/Manager manager/d' \
        domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/pick/service/PickService.java
    sed -i '' '/managerRepository/d' \
        domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/pick/service/PickService.java
fi

# 5. package 선언 수정 (service 패키지)
echo "5. 서비스 패키지 선언 수정..."
for service_file in domain-modules/community-domain/src/main/java/com/orangeschool/community/*/*/service/*.java; do
    if [ -f "$service_file" ]; then
        # 현재 패키지 경로 추출
        dir_path=$(dirname "$service_file")
        package_path=$(echo "$dir_path" | sed 's/.*src\/main\/java\///' | tr '/' '.' | sed 's/\.service$//')
        
        # 첫 줄의 패키지 선언 수정
        sed -i '' "1s/^package .*/package ${package_path}.service;/" "$service_file"
    fi
done

# 6. 중복 import 제거
echo "6. 중복 import 제거..."
find domain-modules/community-domain/src/main/java -name "*.java" -type f | while read file; do
    # MemberInfoProvider 중복 제거
    awk '!seen[$0]++ || !/import com\.orangeschool\.member\.api\.service\.MemberInfoProvider;/' "$file" > "$file.tmp" && mv "$file.tmp" "$file"
done

# 7. @Builder.Default import 추가
echo "7. @Builder.Default import 추가..."
for entity_file in domain-modules/community-domain/src/main/java/com/orangeschool/community/*/*/entity/*.java; do
    if [ -f "$entity_file" ]; then
        if grep -q "@Builder.Default" "$entity_file" && ! grep -q "import lombok.Builder;" "$entity_file"; then
            sed -i '' '/import lombok/a\
import lombok.Builder;' "$entity_file"
        fi
    fi
done

echo "=========================================="
echo "최종 수정 완료!"
echo "컴파일 명령어:"
echo "./gradlew :domain-modules:community-domain:clean :domain-modules:community-domain:compileJava"
echo "=========================================="