#!/bin/bash

echo "=========================================="
echo "Community Domain Import 문제 수정"
echo "=========================================="

# 1. FollowController import 수정
echo "1. FollowController import 수정..."
sed -i '' '/^package com.orangeschool.community.follow;/a\
\
import com.orangeschool.community.follow.service.FollowService;' \
    domain-modules/community-domain/src/main/java/com/orangeschool/community/follow/controller/FollowController.java

# 2. CheeringController import 수정
echo "2. CheeringController import 수정..."
sed -i '' 's/import com\.orangeschool\.community\.cheering\.CheeringService;/import com.orangeschool.community.cheering.service.CheeringService;/g' \
    domain-modules/community-domain/src/main/java/com/orangeschool/community/cheering/controller/CheeringController.java

# 3. Pick 관련 서비스들 import 추가 및 CommonMember 제거
echo "3. Pick 관련 서비스 수정..."
for service_file in domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/*/service/*.java; do
    if [ -f "$service_file" ]; then
        echo "Processing $service_file..."
        # CommonMember import 제거
        sed -i '' '/import com\.orangeschool\.member\.commonMember\.entity\.CommonMember;/d' "$service_file"
        
        # MemberInfoProvider import 추가 (없는 경우)
        if ! grep -q "import com.orangeschool.member.api.service.MemberInfoProvider;" "$service_file"; then
            sed -i '' '/^package/a\
\
import com.orangeschool.member.api.service.MemberInfoProvider;\
import com.orangeschool.member.api.dto.MemberInfo;' "$service_file"
        fi
    fi
done

# 4. Story 관련 서비스들 수정
echo "4. Story 관련 서비스 수정..."
for service_file in domain-modules/community-domain/src/main/java/com/orangeschool/community/story/*/service/*.java; do
    if [ -f "$service_file" ]; then
        echo "Processing $service_file..."
        # CommonMember import 제거
        sed -i '' '/import com\.orangeschool\.member\.commonMember\.entity\.CommonMember;/d' "$service_file"
        
        # MemberInfoProvider import 추가 (없는 경우)
        if ! grep -q "import com.orangeschool.member.api.service.MemberInfoProvider;" "$service_file"; then
            sed -i '' '/^package/a\
\
import com.orangeschool.member.api.service.MemberInfoProvider;\
import com.orangeschool.member.api.dto.MemberInfo;' "$service_file"
        fi
    fi
done

# 5. Repository Implementation 파일들 수정
echo "5. Repository Implementation 파일들 QueryDSL import 수정..."

# PickLikeRepositoryImpl 수정
if [ -f "domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/like/repository/PickLikeRepositoryImpl.java" ]; then
    sed -i '' 's/import static com\.orangeschool\.community\.pick\.like\.entity\.QPickLike\.\*;/import static com.orangeschool.community.pick.like.entity.QPickLike.pickLike;/g' \
        domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/like/repository/PickLikeRepositoryImpl.java
fi

# 6. ResponseCode 누락 추가
echo "6. ResponseCode 관련 에러 수정..."
# ALREADY_FOLLOWING, NOT_FOUND_FOLLOW, ALREADY_CHEERED_TODAY 등 추가 필요한 코드들 확인

# 7. DTO 클래스들의 @EqualsAndHashCode 추가
echo "7. DTO 클래스들의 @EqualsAndHashCode 추가..."
find domain-modules/community-domain/src/main/java -name "*Dto.java" -type f | while read file; do
    # @Data 어노테이션이 있고 @EqualsAndHashCode가 없는 경우 추가
    if grep -q "@Data" "$file" && ! grep -q "@EqualsAndHashCode" "$file"; then
        sed -i '' '/@Data/i\
@EqualsAndHashCode(callSuper = false)' "$file"
    fi
done

# 8. @Builder.Default 추가
echo "8. @Builder.Default 추가..."
# Pick 엔티티 수정
if [ -f "domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/pick/entity/Pick.java" ]; then
    sed -i '' 's/private Set<PickImage> images = new HashSet<>();/@Builder.Default\
    private Set<PickImage> images = new HashSet<>();/g' \
        domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/pick/entity/Pick.java
    
    sed -i '' 's/private Set<PickComment> pickComments = new HashSet<>();/@Builder.Default\
    private Set<PickComment> pickComments = new HashSet<>();/g' \
        domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/pick/entity/Pick.java
    
    sed -i '' 's/private Set<PickLike> pickLikes = new HashSet<>();/@Builder.Default\
    private Set<PickLike> pickLikes = new HashSet<>();/g' \
        domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/pick/entity/Pick.java
fi

# Story 엔티티 수정
if [ -f "domain-modules/community-domain/src/main/java/com/orangeschool/community/story/story/entity/Story.java" ]; then
    sed -i '' 's/private Set<StoryImage> images = new HashSet<>();/@Builder.Default\
    private Set<StoryImage> images = new HashSet<>();/g' \
        domain-modules/community-domain/src/main/java/com/orangeschool/community/story/story/entity/Story.java
    
    sed -i '' 's/private Set<StoryComment> storyComments = new HashSet<>();/@Builder.Default\
    private Set<StoryComment> storyComments = new HashSet<>();/g' \
        domain-modules/community-domain/src/main/java/com/orangeschool/community/story/story/entity/Story.java
    
    sed -i '' 's/private Set<StoryLike> storyLikes = new HashSet<>();/@Builder.Default\
    private Set<StoryLike> storyLikes = new HashSet<>();/g' \
        domain-modules/community-domain/src/main/java/com/orangeschool/community/story/story/entity/Story.java
fi

echo "=========================================="
echo "Import 문제 수정 완료!"
echo "=========================================="