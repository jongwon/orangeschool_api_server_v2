#!/bin/bash

# Usage: ./migrate-domain.sh <source-domain> <target-module> <target-package>
# Example: ./migrate-domain.sh location support location

SOURCE_DOMAIN=$1
TARGET_MODULE=$2
TARGET_PACKAGE=$3

SOURCE_BASE="src.backup/main/java/com/orangeschool/orangeschoolapiserver/domain"
TARGET_BASE="domain-modules/${TARGET_MODULE}-domain/src/main/java/com/orangeschool/${TARGET_MODULE}"

# Create target directories
mkdir -p "${TARGET_BASE}/${TARGET_PACKAGE}/"{entity,repository,service,dto,controller}

# Copy files
cp "${SOURCE_BASE}/${SOURCE_DOMAIN}/entity/"*.java "${TARGET_BASE}/${TARGET_PACKAGE}/entity/" 2>/dev/null || true
cp "${SOURCE_BASE}/${SOURCE_DOMAIN}/repository/"*.java "${TARGET_BASE}/${TARGET_PACKAGE}/repository/" 2>/dev/null || true
cp "${SOURCE_BASE}/${SOURCE_DOMAIN}/dto/"*.java "${TARGET_BASE}/${TARGET_PACKAGE}/dto/" 2>/dev/null || true
cp "${SOURCE_BASE}/${SOURCE_DOMAIN}/"*Service.java "${TARGET_BASE}/${TARGET_PACKAGE}/service/" 2>/dev/null || true
cp "${SOURCE_BASE}/${SOURCE_DOMAIN}/"*Controller.java "${TARGET_BASE}/${TARGET_PACKAGE}/controller/" 2>/dev/null || true

# Update package names
find "${TARGET_BASE}/${TARGET_PACKAGE}" -name "*.java" -exec sed -i '' \
    -e "s/package com\.orangeschool\.orangeschoolapiserver\.domain\.${SOURCE_DOMAIN}/package com.orangeschool.${TARGET_MODULE}.${TARGET_PACKAGE}/g" \
    {} \;

# Update imports
find "${TARGET_BASE}/${TARGET_PACKAGE}" -name "*.java" -exec sed -i '' \
    -e "s/import com\.orangeschool\.orangeschoolapiserver\.common\./import com.orangeschool.common./g" \
    -e "s/import com\.orangeschool\.orangeschoolapiserver\.domain\.${SOURCE_DOMAIN}\./import com.orangeschool.${TARGET_MODULE}.${TARGET_PACKAGE}./g" \
    {} \;

echo "Migrated ${SOURCE_DOMAIN} to ${TARGET_MODULE}-domain/${TARGET_PACKAGE}"