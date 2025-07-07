package com.orangeschool.community.api.service;

import com.orangeschool.common.enums.CheeringMessage;
import com.orangeschool.community.api.dto.CheeringInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 응원 정보를 제공하는 인터페이스
 * 다른 도메인에서 응원 관련 정보가 필요할 때 사용
 */
public interface CheeringInfoProvider {
    
    /**
     * 특정 회원이 받은 응원 목록 조회
     *
     * @param cheeredMemberId 응원받은 회원 ID
     * @param pageable 페이징 정보
     * @return 응원 정보 페이지
     */
    Page<CheeringInfo> getCheeringsForMember(Long cheeredMemberId, Pageable pageable);
    
    /**
     * 특정 회원이 받은 응원 개수 조회
     *
     * @param cheeredMemberId 응원받은 회원 ID
     * @return 응원 개수
     */
    Long getCheeringCount(Long cheeredMemberId);
    
    /**
     * 두 회원 간 응원 여부 확인
     *
     * @param cheeringMemberId 응원하는 회원 ID
     * @param cheeredMemberId 응원받는 회원 ID
     * @return 응원 여부
     */
    boolean isCheeringExists(Long cheeringMemberId, Long cheeredMemberId);
    
    /**
     * 특정 타입의 응원 개수 조회
     *
     * @param cheeredMemberId 응원받은 회원 ID
     * @param cheeringMessage 응원 메시지 타입
     * @return 해당 타입의 응원 개수
     */
    int countCheeringByType(Long cheeredMemberId, CheeringMessage cheeringMessage);
    
    /**
     * 특정 회원이 특정 회원에게 특정 타입의 응원을 했는지 확인
     *
     * @param cheeringMemberId 응원하는 회원 ID
     * @param cheeredMemberId 응원받는 회원 ID
     * @param cheeringMessage 응원 메시지 타입
     * @return 응원 여부
     */
    boolean hasCheered(Long cheeringMemberId, Long cheeredMemberId, CheeringMessage cheeringMessage);
}