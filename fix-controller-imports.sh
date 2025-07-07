#!/bin/bash

echo "=========================================="
echo "Community Domain Controller Import 수정"
echo "=========================================="

# 1. Follow Controller
echo "1. FollowController import 수정..."
# 이미 수정됨

# 2. Cheering Controller
echo "2. CheeringController import 수정..."
# 이미 수정됨

# 3. Pick 관련 Controller들
echo "3. Pick 관련 Controller import 추가..."

# PickController
sed -i '' '/^package com.orangeschool.community.pick.pick;/a\
\
import com.orangeschool.community.pick.pick.service.PickService;' \
    domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/pick/controller/PickController.java

# PickCommentController
sed -i '' '/^package com.orangeschool.community.pick.comment;/a\
\
import com.orangeschool.community.pick.comment.service.PickCommentService;' \
    domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/comment/controller/PickCommentController.java

# PickReplyController
sed -i '' '/^package com.orangeschool.community.pick.reply;/a\
\
import com.orangeschool.community.pick.reply.service.PickReplyService;' \
    domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/reply/controller/PickReplyController.java

# PickLikeController
sed -i '' '/^package com.orangeschool.community.pick.like;/a\
\
import com.orangeschool.community.pick.like.service.PickLikeService;' \
    domain-modules/community-domain/src/main/java/com/orangeschool/community/pick/like/controller/PickLikeController.java

# 4. Story 관련 Controller들
echo "4. Story 관련 Controller import 추가..."

# StoryController
sed -i '' '/^package com.orangeschool.community.story.story;/a\
\
import com.orangeschool.community.story.story.service.StoryService;' \
    domain-modules/community-domain/src/main/java/com/orangeschool/community/story/story/controller/StoryController.java

# StoryCommentController
sed -i '' '/^package com.orangeschool.community.story.comment;/a\
\
import com.orangeschool.community.story.comment.service.StoryCommentService;' \
    domain-modules/community-domain/src/main/java/com/orangeschool/community/story/comment/controller/StoryCommentController.java

# StoryReplyController
sed -i '' '/^package com.orangeschool.community.story.reply;/a\
\
import com.orangeschool.community.story.reply.service.StoryReplyService;' \
    domain-modules/community-domain/src/main/java/com/orangeschool/community/story/reply/controller/StoryReplyController.java

# StoryLikeController
sed -i '' '/^package com.orangeschool.community.story.like;/a\
\
import com.orangeschool.community.story.like.service.StoryLikeService;' \
    domain-modules/community-domain/src/main/java/com/orangeschool/community/story/like/controller/StoryLikeController.java

echo "=========================================="
echo "Controller import 수정 완료!"
echo "=========================================="