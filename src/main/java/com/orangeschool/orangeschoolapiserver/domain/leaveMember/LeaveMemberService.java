package com.orangeschool.orangeschoolapiserver.domain.leaveMember;

import com.orangeschool.orangeschoolapiserver.common.dto.request.IdListDto;
import com.orangeschool.orangeschoolapiserver.common.dto.request.KeywordSearchDto;
import com.orangeschool.orangeschoolapiserver.common.response.CustomException;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.repository.CommonMemberRepository;
import com.orangeschool.orangeschoolapiserver.domain.leaveMember.dto.CreateLeaveMemberDto;
import com.orangeschool.orangeschoolapiserver.domain.leaveMember.dto.LeaveMemberDto;
import com.orangeschool.orangeschoolapiserver.domain.leaveMember.entity.LeaveMember;
import com.orangeschool.orangeschoolapiserver.domain.leaveMember.repository.LeaveMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LeaveMemberService {

    private final LeaveMemberRepository leaveMemberRepository;
    private final CommonMemberRepository commonMemberRepository;

    @Transactional
    public void create(Long commonMemberId, CreateLeaveMemberDto createLeaveMemberDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember commonMember = commonMemberOptional.get();

        LeaveMember leaveMember = LeaveMember.builder()
                .memberType(commonMember.getMemberType())
                .email(commonMember.getEmail())
                .name(commonMember.getName())
                .leaveType(createLeaveMemberDto.getLeaveType())
                .reasonDetail(createLeaveMemberDto.getReasonDetail())
                .build();

        leaveMemberRepository.save(leaveMember);

        List<CommonMember> childList = commonMemberRepository.findByParentId(commonMember.getId());
        for(CommonMember child : childList){
            LeaveMember childLeaveMember = LeaveMember.builder()
                    .memberType(child.getMemberType())
                    .email(child.getEmail())
                    .name(child.getName())
                    .leaveType(createLeaveMemberDto.getLeaveType())
                    .reasonDetail(createLeaveMemberDto.getReasonDetail())
                    .build();

            leaveMemberRepository.save(childLeaveMember);
        }

        commonMemberRepository.deleteById(commonMemberId);
        commonMemberRepository.deleteByParentId(commonMemberId);
    }

    @Transactional(readOnly = true)
    public Page<LeaveMemberDto> get(Pageable pageable, KeywordSearchDto keywordSearchDto) throws Exception {
        return leaveMemberRepository.search(pageable, keywordSearchDto);
    }

    @Transactional(readOnly = true)
    public LeaveMemberDto getById(Long leaveMemberId) throws Exception {

        Optional<LeaveMember> leaveMemberOptional = leaveMemberRepository.findById(leaveMemberId);

        if (!leaveMemberOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        return LeaveMemberDto.create(leaveMemberOptional.get());
    }

    @Transactional
    public void delete(Long leaveMemberId) throws Exception {
        leaveMemberRepository.deleteById(leaveMemberId);
    }
}
