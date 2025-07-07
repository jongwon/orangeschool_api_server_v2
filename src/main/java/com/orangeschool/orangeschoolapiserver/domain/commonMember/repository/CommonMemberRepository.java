package com.orangeschool.orangeschoolapiserver.domain.commonMember.repository;

import com.orangeschool.orangeschoolapiserver.common.enums.JoinType;
import com.orangeschool.orangeschoolapiserver.common.enums.MemberType;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CommonMemberRepository extends PagingAndSortingRepository<CommonMember, Long>, CommonMemberRepositoryCustom {

    Optional<CommonMember> findByEmail(String email);
    Optional<CommonMember> findByParentNickName(String parentNickName);

    Optional<CommonMember> findByPhoneNumber(String phoneNumber);

    void deleteByParentId(Long parentId);

    List<CommonMember> findByNameAndPhoneNumber(String name, String phoneNumber);

    Optional<CommonMember> findByEmailAndNameAndPhoneNumber(String email, String name, String phoneNumber);

    Optional<CommonMember> findByJoinTypeAndSocialTokenAndIsActive(JoinType joinType, String socialToken, Boolean isActive);

    List<CommonMember> findByParentId(Long parentId);

    List<CommonMember> findAll();

    List<CommonMember> findByAgreeToServiceAndMemberType(Boolean agreeToService, MemberType memberType);

    List<CommonMember> findByAgreeToAdAndMemberType(Boolean agreeToAd, MemberType memberType);

    List<CommonMember> findTop10ByLocationCodeAndChallengeProgressOrderByTotalOrangeDesc(long locationCode, Boolean challengeProgress);

    Long countByIsActive(Boolean isActive);

    Long countByMemberType(MemberType memberType);

    Long countByMemberTypeAndIsActive(MemberType memberType, Boolean isActive);

    List<CommonMember> findByIdIn(List<Long> ids);

    List<CommonMember> findByMemberType(MemberType memberType);

    @Transactional
    @Modifying
    @Query("update CommonMember a set pushToken = '' where a.pushToken = :pushToken and a.email != :email")
    void deletePushToken(@Param("pushToken") String pushToken, @Param("email") String email);

    @Modifying
    @Query("update CommonMember m set m.totalOrange = 0")
    void resetOrange();

    Optional<CommonMember> findByMyReferralCode(String myReferralCode);

    @Query("SELECT cm FROM CommonMember cm WHERE cm.referralCode = :myReferralCode OR cm.referralCodeTemp = :myReferralCode")
    List<CommonMember> findByMatchingReferralCodes(@Param("myReferralCode") String myReferralCode);

    Long countByReferralCode(String myReferralCode);
}
