#!/bin/bash

echo "=========================================="
echo "Community Domain 엔티티 수정 시작"
echo "=========================================="

# 1. Follow 엔티티 수정
echo "1. Follow 엔티티 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/follow/entity/Follow.java << 'EOF'
package com.orangeschool.community.follow.entity;

import com.orangeschool.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Follow extends BaseEntity {

    // 팔로우 하려는 사람
    private Long followingMemberId;

    // 팔로우 당하는 사용자
    private Long followerMemberId;
}
EOF

# 2. Cheering 엔티티 수정
echo "2. Cheering 엔티티 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/cheering/entity/Cheering.java << 'EOF'
package com.orangeschool.community.cheering.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.common.enums.CheeringMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Cheering extends BaseEntity {

    // 응원을 보내는 사람
    private Long cheeringMemberId;

    // 응원을 받는 사람
    private Long cheeredMemberId;

    @Enumerated(EnumType.STRING)
    private CheeringMessage message;
}
EOF

# 3. PickLike 엔티티 수정
echo "3. PickLike 엔티티 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/like/entity/PickLike.java << 'EOF'
package com.orangeschool.community.pick.like.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.community.pick.pick.entity.Pick;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PickLike extends BaseEntity {

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickId")
    private Pick pick;
}
EOF

# 4. PickComment 엔티티 수정
echo "4. PickComment 엔티티 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/comment/entity/PickComment.java << 'EOF'
package com.orangeschool.community.pick.comment.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.community.pick.pick.entity.Pick;
import com.orangeschool.community.pick.reply.entity.PickReply;
import lombok.*;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PickComment extends BaseEntity {

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickId")
    private Pick pick;

    private String content;

    @OneToMany(mappedBy = "pickComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<PickReply> pickReplies = new HashSet<>();
}
EOF

# 5. PickReply 엔티티 수정
echo "5. PickReply 엔티티 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/reply/entity/PickReply.java << 'EOF'
package com.orangeschool.community.pick.reply.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.community.pick.comment.entity.PickComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PickReply extends BaseEntity {

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickCommentId")
    private PickComment pickComment;

    private String content;
}
EOF

# 6. StoryLike 엔티티 수정
echo "6. StoryLike 엔티티 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/story/like/entity/StoryLike.java << 'EOF'
package com.orangeschool.community.story.like.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.community.story.story.entity.Story;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class StoryLike extends BaseEntity {

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storyId")
    private Story story;
}
EOF

# 7. StoryComment 엔티티 수정
echo "7. StoryComment 엔티티 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/story/comment/entity/StoryComment.java << 'EOF'
package com.orangeschool.community.story.comment.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.community.story.story.entity.Story;
import com.orangeschool.community.story.reply.entity.StoryReply;
import lombok.*;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class StoryComment extends BaseEntity {

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storyId")
    private Story story;

    private String content;

    @OneToMany(mappedBy = "storyComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<StoryReply> storyReplies = new HashSet<>();
}
EOF

# 8. StoryReply 엔티티 수정
echo "8. StoryReply 엔티티 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/story/reply/entity/StoryReply.java << 'EOF'
package com.orangeschool.community.story.reply.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.community.story.comment.entity.StoryComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class StoryReply extends BaseEntity {

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storyCommentId")
    private StoryComment storyComment;

    private String content;
}
EOF

# 9. Pick 엔티티 확인 및 수정
echo "9. Pick 엔티티 CommonEntity import 수정..."
sed -i '' 's/import com\.orangeschool\.common\.entity\.CommonEntity;/import com.orangeschool.common.entity.BaseEntity;/g' \
    domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/pick/entity/Pick.java
sed -i '' 's/extends CommonEntity/extends BaseEntity/g' \
    domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/pick/entity/Pick.java

# 10. PickImage 엔티티 수정
echo "10. PickImage 엔티티 CommonEntity import 수정..."
sed -i '' 's/import com\.orangeschool\.common\.entity\.CommonEntity;/import com.orangeschool.common.entity.BaseEntity;/g' \
    domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/pick/entity/PickImage.java
sed -i '' 's/extends CommonEntity/extends BaseEntity/g' \
    domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/pick/entity/PickImage.java

# 11. Story 엔티티 수정
echo "11. Story 엔티티 수정..."
find domain-modules/community-domain/src/main/java/com/orangeschool/community/story -name "*.java" -type f | while read file; do
    # CommonEntity를 BaseEntity로 변경
    sed -i '' 's/import com\.orangeschool\.common\.entity\.CommonEntity;/import com.orangeschool.common.entity.BaseEntity;/g' "$file"
    sed -i '' 's/extends CommonEntity/extends BaseEntity/g' "$file"
done

echo "=========================================="
echo "엔티티 수정 완료!"
echo "=========================================="