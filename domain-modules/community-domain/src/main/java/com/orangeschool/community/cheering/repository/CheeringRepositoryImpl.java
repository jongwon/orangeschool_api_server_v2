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
