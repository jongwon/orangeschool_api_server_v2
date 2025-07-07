#!/bin/bash

echo "=========================================="
echo "Community Domain 서비스 및 리포지토리 수정"
echo "=========================================="

# 1. FollowService 수정
echo "1. FollowService 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/follow/service/FollowService.java << 'EOF'
package com.orangeschool.community.follow.service;

import com.orangeschool.member.api.service.MemberInfoProvider;
import com.orangeschool.member.api.dto.MemberInfo;
import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.community.follow.entity.Follow;
import com.orangeschool.community.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberInfoProvider memberInfoProvider;

    @Transactional
    public void follow(Long followingMemberId, Long followerMemberId) {
        // 회원 존재 여부 확인
        MemberInfo followingMember = memberInfoProvider.getMemberInfo(followingMemberId);
        MemberInfo followerMember = memberInfoProvider.getMemberInfo(followerMemberId);

        if (followingMember == null || followerMember == null) {
            throw new CustomException(ResponseCode.NOT_FOUND_USER);
        }

        // 이미 팔로우 중인지 확인
        Optional<Follow> existingFollow = followRepository.findByFollowingMemberIdAndFollowerMemberId(
            followingMemberId, followerMemberId
        );

        if (existingFollow.isPresent()) {
            throw new CustomException(ResponseCode.ALREADY_FOLLOWING);
        }

        Follow follow = Follow.builder()
            .followingMemberId(followingMemberId)
            .followerMemberId(followerMemberId)
            .build();

        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(Long followingMemberId, Long followerMemberId) {
        Follow follow = followRepository.findByFollowingMemberIdAndFollowerMemberId(
            followingMemberId, followerMemberId
        ).orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_FOLLOW));

        followRepository.delete(follow);
    }

    public boolean isFollowing(Long followingMemberId, Long followerMemberId) {
        return followRepository.existsByFollowingMemberIdAndFollowerMemberId(
            followingMemberId, followerMemberId
        );
    }

    public Long countFollowers(Long memberId) {
        return followRepository.countByFollowerMemberId(memberId);
    }

    public Long countFollowing(Long memberId) {
        return followRepository.countByFollowingMemberId(memberId);
    }
}
EOF

# 2. CheeringService 수정
echo "2. CheeringService 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/cheering/service/CheeringService.java << 'EOF'
package com.orangeschool.community.cheering.service;

import com.orangeschool.member.api.service.MemberInfoProvider;
import com.orangeschool.member.api.dto.MemberInfo;
import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.community.cheering.dto.CheeringDto;
import com.orangeschool.community.cheering.dto.CheeringRequestDto;
import com.orangeschool.community.cheering.entity.Cheering;
import com.orangeschool.community.cheering.repository.CheeringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CheeringService {

    private final CheeringRepository cheeringRepository;
    private final MemberInfoProvider memberInfoProvider;

    @Transactional
    public CheeringDto createCheering(CheeringRequestDto requestDto) {
        // 회원 존재 여부 확인
        MemberInfo cheeringMember = memberInfoProvider.getMemberInfo(requestDto.getCheeringMemberId());
        MemberInfo cheeredMember = memberInfoProvider.getMemberInfo(requestDto.getCheeredMemberId());

        if (cheeringMember == null || cheeredMember == null) {
            throw new CustomException(ResponseCode.NOT_FOUND_USER);
        }

        // 오늘 이미 응원했는지 확인
        Optional<Cheering> todayCheering = cheeringRepository.findTodayCheeringBetweenMembers(
            requestDto.getCheeringMemberId(), 
            requestDto.getCheeredMemberId()
        );

        if (todayCheering.isPresent()) {
            throw new CustomException(ResponseCode.ALREADY_CHEERED_TODAY);
        }

        Cheering cheering = Cheering.builder()
            .cheeringMemberId(requestDto.getCheeringMemberId())
            .cheeredMemberId(requestDto.getCheeredMemberId())
            .message(requestDto.getMessage())
            .build();

        cheering = cheeringRepository.save(cheering);

        return convertToDto(cheering, cheeringMember, cheeredMember);
    }

    public Page<CheeringDto> getCheeringList(Long memberId, Pageable pageable) {
        Page<Cheering> cheeringPage = cheeringRepository.findByCheeredMemberId(memberId, pageable);

        return cheeringPage.map(cheering -> {
            MemberInfo cheeringMember = memberInfoProvider.getMemberInfo(cheering.getCheeringMemberId());
            MemberInfo cheeredMember = memberInfoProvider.getMemberInfo(cheering.getCheeredMemberId());
            return convertToDto(cheering, cheeringMember, cheeredMember);
        });
    }

    private CheeringDto convertToDto(Cheering cheering, MemberInfo cheeringMember, MemberInfo cheeredMember) {
        return CheeringDto.builder()
            .id(cheering.getId())
            .cheeringMember(cheeringMember)
            .cheeredMember(cheeredMember)
            .message(cheering.getMessage())
            .createdAt(cheering.getCreatedAt())
            .build();
    }
}
EOF

# 3. FollowRepository 인터페이스 수정
echo "3. FollowRepository 인터페이스 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/follow/repository/FollowRepository.java << 'EOF'
package com.orangeschool.community.follow.repository;

import com.orangeschool.community.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryCustom {
    
    Optional<Follow> findByFollowingMemberIdAndFollowerMemberId(Long followingMemberId, Long followerMemberId);
    
    boolean existsByFollowingMemberIdAndFollowerMemberId(Long followingMemberId, Long followerMemberId);
    
    Long countByFollowerMemberId(Long followerMemberId);
    
    Long countByFollowingMemberId(Long followingMemberId);
}
EOF

# 4. FollowRepositoryCustom 수정
echo "4. FollowRepositoryCustom 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/follow/repository/FollowRepositoryCustom.java << 'EOF'
package com.orangeschool.community.follow.repository;

import com.orangeschool.member.api.dto.MemberInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FollowRepositoryCustom {
    Page<MemberInfo> searchFollowers(Long memberId, Pageable pageable);
    Page<MemberInfo> searchFollowing(Long memberId, Pageable pageable);
}
EOF

# 5. FollowRepositoryImpl 수정
echo "5. FollowRepositoryImpl 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/follow/repository/FollowRepositoryImpl.java << 'EOF'
package com.orangeschool.community.follow.repository;

import com.orangeschool.member.api.dto.MemberInfo;
import com.orangeschool.member.api.service.MemberInfoProvider;
import com.orangeschool.community.follow.entity.Follow;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.orangeschool.community.follow.entity.QFollow.follow;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final MemberInfoProvider memberInfoProvider;

    @Override
    public Page<MemberInfo> searchFollowers(Long memberId, Pageable pageable) {
        List<Follow> follows = queryFactory
            .selectFrom(follow)
            .where(follow.followerMemberId.eq(memberId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory
            .select(follow.count())
            .from(follow)
            .where(follow.followerMemberId.eq(memberId))
            .fetchOne();

        List<MemberInfo> memberInfos = follows.stream()
            .map(f -> memberInfoProvider.getMemberInfo(f.getFollowingMemberId()))
            .filter(info -> info != null)
            .collect(Collectors.toList());

        return new PageImpl<>(memberInfos, pageable, total != null ? total : 0L);
    }

    @Override
    public Page<MemberInfo> searchFollowing(Long memberId, Pageable pageable) {
        List<Follow> follows = queryFactory
            .selectFrom(follow)
            .where(follow.followingMemberId.eq(memberId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory
            .select(follow.count())
            .from(follow)
            .where(follow.followingMemberId.eq(memberId))
            .fetchOne();

        List<MemberInfo> memberInfos = follows.stream()
            .map(f -> memberInfoProvider.getMemberInfo(f.getFollowerMemberId()))
            .filter(info -> info != null)
            .collect(Collectors.toList());

        return new PageImpl<>(memberInfos, pageable, total != null ? total : 0L);
    }
}
EOF

# 6. CheeringRepository 인터페이스 수정
echo "6. CheeringRepository 인터페이스 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/cheering/repository/CheeringRepository.java << 'EOF'
package com.orangeschool.community.cheering.repository;

import com.orangeschool.community.cheering.entity.Cheering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CheeringRepository extends JpaRepository<Cheering, Long>, CheeringRepositoryCustom {
    
    Page<Cheering> findByCheeredMemberId(Long cheeredMemberId, Pageable pageable);
    
    @Query("SELECT c FROM Cheering c WHERE c.cheeringMemberId = :cheeringMemberId " +
           "AND c.cheeredMemberId = :cheeredMemberId " +
           "AND DATE(c.createdAt) = CURRENT_DATE")
    Optional<Cheering> findTodayCheeringBetweenMembers(
        @Param("cheeringMemberId") Long cheeringMemberId, 
        @Param("cheeredMemberId") Long cheeredMemberId
    );
}
EOF

# 7. CheeringRepositoryImpl 수정
echo "7. CheeringRepositoryImpl 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/cheering/repository/CheeringRepositoryImpl.java << 'EOF'
package com.orangeschool.community.cheering.repository;

import com.orangeschool.community.cheering.dto.CheeringDto;
import com.orangeschool.community.cheering.entity.Cheering;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.orangeschool.community.cheering.entity.QCheering.cheering;

@Repository
@RequiredArgsConstructor
public class CheeringRepositoryImpl implements CheeringRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CheeringDto> searchCheering(Long memberId, Pageable pageable) {
        List<Cheering> cheeringList = queryFactory
            .selectFrom(cheering)
            .where(cheering.cheeredMemberId.eq(memberId))
            .orderBy(cheering.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory
            .select(cheering.count())
            .from(cheering)
            .where(cheering.cheeredMemberId.eq(memberId))
            .fetchOne();

        // Note: CheeringDto 변환은 서비스 레이어에서 처리
        return new PageImpl<>(cheeringList, pageable, total != null ? total : 0L);
    }
}
EOF

# 8. CheeringDto 수정
echo "8. CheeringDto 수정..."
cat > domain-modules/community-domain/src/main/java/com/orangeschool/community/cheering/dto/CheeringDto.java << 'EOF'
package com.orangeschool.community.cheering.dto;

import com.orangeschool.common.enums.CheeringMessage;
import com.orangeschool.member.api.dto.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheeringDto {
    private Long id;
    private MemberInfo cheeringMember;
    private MemberInfo cheeredMember;
    private CheeringMessage message;
    private LocalDateTime createdAt;
}
EOF

# 9. QueryDSL 재생성
echo "9. QueryDSL Q클래스 재생성..."
./gradlew :domain-modules:community-domain:clean :domain-modules:community-domain:compileQuerydsl

echo "=========================================="
echo "서비스 및 리포지토리 수정 완료!"
echo "=========================================="