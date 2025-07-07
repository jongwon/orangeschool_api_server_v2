package com.orangeschool.support.visitor;

import com.orangeschool.common.enums.MemberType;
import com.orangeschool.member.commonMember.repository.CommonMemberRepository;
import com.orangeschool.leaveMember.repository.LeaveMemberRepository;
import com.orangeschool.support.visitor.dto.CreateVisitorDto;
import com.orangeschool.support.visitor.dto.MemberCountDto;
import com.orangeschool.support.visitor.dto.VisitorDto;
import com.orangeschool.support.visitor.dto.VisitorFilterDto;
import com.orangeschool.support.visitor.entity.Visitor;
import com.orangeschool.support.visitor.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VisitorService {

    private final VisitorRepository visitorRepository;
    private final CommonMemberRepository commonMemberRepository;
    private final LeaveMemberRepository leaveMemberRepository;

    @Transactional
    public void create(CreateVisitorDto createVisitorDto) throws Exception {

        Optional<Visitor> visitorOptional = visitorRepository.findByTodayAndMemberType(LocalDate.now(), createVisitorDto.getMemberType());
        if (visitorOptional.isPresent()) {
            Visitor visitor = visitorOptional.get();
            visitor.updateVisitors();
            visitorRepository.save(visitor);
        } else {
            Visitor visitor = Visitor.builder()
                    .today(LocalDate.now())
                    .memberType(createVisitorDto.getMemberType())
                    .count(1)
                    .build();
            visitorRepository.save(visitor);
        }
    }

    @Transactional(readOnly = true)
    public Page<VisitorDto> get(Pageable pageable, VisitorFilterDto visitorFilterDto) throws Exception {
        Page<VisitorDto> visitorList = visitorRepository.search(pageable, visitorFilterDto.getMemberType());

        return visitorList;
    }

    @Transactional(readOnly = true)
    public MemberCountDto getMemberCount() throws Exception {

        Long totalCount = commonMemberRepository.count();
        Long joinCount = commonMemberRepository.countByIsActive(true);
        Long leaveCount = leaveMemberRepository.count();

        Long parentCount = commonMemberRepository.countByMemberType(MemberType.PARENT);
        Long parentJoinCount = commonMemberRepository.countByMemberTypeAndIsActive(MemberType.PARENT, true);
        Long parentLeaveCount = leaveMemberRepository.countByMemberType(MemberType.PARENT);

        Long childCount = commonMemberRepository.countByMemberType(MemberType.CHILD);
        Long childJoinCount = commonMemberRepository.countByMemberTypeAndIsActive(MemberType.CHILD, true);
        Long childLeaveCount = leaveMemberRepository.countByMemberType(MemberType.CHILD);

        MemberCountDto memberCountDto = new MemberCountDto();
        memberCountDto.setTotalCount(totalCount);
        memberCountDto.setJoinCount(joinCount);
        memberCountDto.setLeaveCount(leaveCount);

        memberCountDto.setParentCount(parentCount);
        memberCountDto.setParentJoinCount(parentJoinCount);
        memberCountDto.setParentLeaveCount(parentLeaveCount);

        memberCountDto.setChildCount(childCount);
        memberCountDto.setChildJoinCount(childJoinCount);
        memberCountDto.setChildLeaveCount(childLeaveCount);

        return memberCountDto;
    }
}
