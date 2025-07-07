#!/bin/bash

# 복잡한 도메인 구조를 마이그레이션하는 스크립트
# Usage: ./migrate-complex-domain.sh <source-domain> <target-module>

SOURCE_DOMAIN=$1
TARGET_MODULE=$2

if [ -z "$SOURCE_DOMAIN" ] || [ -z "$TARGET_MODULE" ]; then
    echo "Usage: ./migrate-complex-domain.sh <source-domain> <target-module>"
    echo "Example: ./migrate-complex-domain.sh pick community-domain"
    exit 1
fi

SOURCE_BASE="/Users/jongwon/dev/works/orange-school/orangeschool_api_server/src/main/java/com/orangeschool/orangeschoolapiserver/domain"
TARGET_BASE="/Users/jongwon/dev/works/orange-school/orangeschool_api_server_v2/domain-modules"

echo "Migrating $SOURCE_DOMAIN to $TARGET_MODULE..."

# 소스 도메인 디렉토리 확인
if [ ! -d "${SOURCE_BASE}/${SOURCE_DOMAIN}" ]; then
    echo "Source domain directory not found: ${SOURCE_BASE}/${SOURCE_DOMAIN}"
    exit 1
fi

# 타겟 모듈 디렉토리 확인
if [ ! -d "${TARGET_BASE}/${TARGET_MODULE}" ]; then
    echo "Target module directory not found: ${TARGET_BASE}/${TARGET_MODULE}"
    exit 1
fi

# 대상 패키지명 추출 (예: community-domain -> community)
TARGET_PACKAGE=$(echo $TARGET_MODULE | sed 's/-domain//')

# 하위 모듈들 처리
for submodule in $(ls -d ${SOURCE_BASE}/${SOURCE_DOMAIN}/*/ 2>/dev/null | xargs -n 1 basename); do
    echo "Processing submodule: $submodule"
    
    # 대상 디렉토리 생성
    TARGET_DIR="${TARGET_BASE}/${TARGET_MODULE}/src/main/java/com/orangeschool/${TARGET_PACKAGE}/${SOURCE_DOMAIN}/${submodule}"
    mkdir -p "${TARGET_DIR}/controller"
    mkdir -p "${TARGET_DIR}/service"
    mkdir -p "${TARGET_DIR}/dto"
    mkdir -p "${TARGET_DIR}/entity"
    mkdir -p "${TARGET_DIR}/repository"
    
    # 파일 복사 및 패키지명 변경
    for file_type in controller service dto entity repository; do
        SOURCE_FILES="${SOURCE_BASE}/${SOURCE_DOMAIN}/${submodule}/${file_type}"
        if [ -d "$SOURCE_FILES" ]; then
            cp -r ${SOURCE_FILES}/* "${TARGET_DIR}/${file_type}/" 2>/dev/null || true
        else
            # 직접 파일이 있는 경우
            # Controller, Service 등의 파일 처리
            case $file_type in
                controller) cp ${SOURCE_BASE}/${SOURCE_DOMAIN}/${submodule}/*Controller*.java "${TARGET_DIR}/${file_type}/" 2>/dev/null || true ;;
                service) cp ${SOURCE_BASE}/${SOURCE_DOMAIN}/${submodule}/*Service*.java "${TARGET_DIR}/${file_type}/" 2>/dev/null || true ;;
                entity) cp ${SOURCE_BASE}/${SOURCE_DOMAIN}/${submodule}/*.java "${TARGET_DIR}/${file_type}/" 2>/dev/null || true ;;
            esac
        fi
    done
    
    # 패키지명 변경
    find "${TARGET_DIR}" -name "*.java" -exec sed -i '' \
        -e "s/package com\.orangeschool\.orangeschoolapiserver\.domain\.${SOURCE_DOMAIN}\.${submodule}/package com.orangeschool.${TARGET_PACKAGE}.${SOURCE_DOMAIN}.${submodule}/g" \
        -e "s/import com\.orangeschool\.orangeschoolapiserver\./import com.orangeschool./g" \
        -e "s/\.domain\./.${TARGET_PACKAGE}./g" \
        {} \;
done

echo "Migration completed for $SOURCE_DOMAIN!"
echo "Don't forget to:"
echo "1. Update build.gradle dependencies"
echo "2. Fix any cross-domain references"
echo "3. Update repository interfaces to extend JpaRepository"