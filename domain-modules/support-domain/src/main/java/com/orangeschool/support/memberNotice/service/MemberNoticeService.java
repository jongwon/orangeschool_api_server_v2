package com.orangeschool.support.memberNotice;

import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.member.commonMember.repository.CommonMemberRepository;
import com.orangeschool.support.memberNotice.dto.MemberNoticeDto;
import com.orangeschool.support.memberNotice.entity.MemberNotice;
import com.orangeschool.support.memberNotice.repository.MemberNoticeRepository;
import com.orangeschool.notice.entity.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberNoticeService {

    private final CommonMemberRepository commonMemberRepository;
    private final MemberNoticeRepository memberNoticeRepository;

    @Transactional
    @Async
    public void create(Notice notice) throws Exception {

        List<CommonMember> memberList = commonMemberRepository.findAll();
        List<MemberNotice> memberNoticeList = new ArrayList<>();
        String title = notice.getTitle();
        String content = notice.getContent();
        Long noticeId = notice.getId();

        for (CommonMember member : memberList) {
            MemberNotice memberNotice = MemberNotice.builder()
                    .commonMember(member)
                    .title(title)
                    .content(content)
                    .isRead(false)
                    .noticeId(noticeId)
                    .build();

            memberNoticeList.add(memberNotice);
        }

        memberNoticeRepository.saveAll(memberNoticeList);
    }

    @Transactional(readOnly = true)
    public Page<MemberNoticeDto> get(Long commonMemberId, Pageable pageable) throws Exception {
        return memberNoticeRepository.search(pageable, commonMemberId);
    }

    @Transactional()
    public MemberNoticeDto getById(Long memberNoticeId) throws Exception {

        Optional<MemberNotice> memberNoticeOptional = memberNoticeRepository.findById(memberNoticeId);

        if (memberNoticeOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        MemberNotice memberNotice = memberNoticeOptional.get();
        memberNotice.read();
        memberNoticeRepository.save(memberNotice);

        return MemberNoticeDto.create(memberNotice);
    }
}
