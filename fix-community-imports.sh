#!/bin/bash

# community-domain의 import 문제를 수정하는 스크립트

TARGET_DIR="/Users/jongwon/dev/works/orange-school/orangeschool_api_server_v2/domain-modules/community-domain"

echo "Fixing import statements in community-domain..."

# 1. CommonMember 관련 import 수정
find "${TARGET_DIR}" -name "*.java" -exec sed -i '' \
    -e 's/import com\.orangeschool\.community\.commonMember\./import com.orangeschool.member.commonMember./g' \
    -e 's/import com\.orangeschool\.member\.commonMember\.repository\.CommonMemberRepository;/import com.orangeschool.member.api.MemberInfoProvider;/g' \
    {} \;

# 2. JwtTokenProvider 경로 수정
find "${TARGET_DIR}" -name "*.java" -exec sed -i '' \
    -e 's/import com\.orangeschool\.common\.utils\.JwtTokenProvider;/import com.orangeschool.auth.util.JwtTokenProvider;/g' \
    {} \;

# 3. Functions 유틸리티 경로 수정
find "${TARGET_DIR}" -name "*.java" -exec sed -i '' \
    -e 's/import com\.orangeschool\.common\.utils\.Functions;/import com.orangeschool.common.util.Functions;/g' \
    {} \;

# 4. QueryDSL Q클래스 경로 수정
find "${TARGET_DIR}" -name "*RepositoryImpl.java" -exec sed -i '' \
    -e 's/import static com\.orangeschool\.orangeschoolapiserver\./import static com.orangeschool./g' \
    -e 's/\.domain\./.community./g' \
    {} \;

# 5. QueryDSL static import 수정
find "${TARGET_DIR}" -name "*RepositoryImpl.java" -exec sed -i '' \
    -e 's/import static com\.orangeschool\.community\.\([^.]*\)\.\([^.]*\)\.entity\.Q/import static com.orangeschool.community.\1.\2.entity.Q/g' \
    {} \;

echo "Import statements fixed!"