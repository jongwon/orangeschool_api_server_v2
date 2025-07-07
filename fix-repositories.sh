#!/bin/bash

# Repository 인터페이스들을 JpaRepository로 변경하는 스크립트

TARGET_DIR="/Users/jongwon/dev/works/orange-school/orangeschool_api_server_v2/domain-modules/community-domain"

echo "Fixing Repository interfaces in community-domain..."

# Repository 파일들 찾기 (Custom과 Impl 제외)
find "${TARGET_DIR}" -name "*Repository.java" | grep -v "Custom" | grep -v "Impl" | while read file; do
    echo "Processing: $file"
    
    # PagingAndSortingRepository를 JpaRepository로 변경
    sed -i '' -e 's/import org\.springframework\.data\.repository\.PagingAndSortingRepository;/import org.springframework.data.jpa.repository.JpaRepository;/g' "$file"
    sed -i '' -e 's/extends PagingAndSortingRepository</extends JpaRepository</g' "$file"
    
    # CrudRepository를 JpaRepository로 변경
    sed -i '' -e 's/import org\.springframework\.data\.repository\.CrudRepository;/import org.springframework.data.jpa.repository.JpaRepository;/g' "$file"
    sed -i '' -e 's/extends CrudRepository</extends JpaRepository</g' "$file"
done

echo "Repository interfaces fixed!"