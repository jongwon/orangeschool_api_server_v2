package com.orangeschool.orangeschoolapiserver.domain.notice;

import com.orangeschool.orangeschoolapiserver.common.dto.request.IdListDto;
import com.orangeschool.orangeschoolapiserver.common.dto.request.KeywordSearchDto;
import com.orangeschool.orangeschoolapiserver.common.response.CustomException;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.domain.manager.entity.Manager;
import com.orangeschool.orangeschoolapiserver.domain.manager.repository.ManagerRepository;
import com.orangeschool.orangeschoolapiserver.domain.memberNotice.MemberNoticeService;
import com.orangeschool.orangeschoolapiserver.domain.memberNotice.repository.MemberNoticeRepository;
import com.orangeschool.orangeschoolapiserver.domain.notice.dto.CreateNoticeDto;
import com.orangeschool.orangeschoolapiserver.domain.notice.dto.NoticeDto;
import com.orangeschool.orangeschoolapiserver.domain.notice.entity.Notice;
import com.orangeschool.orangeschoolapiserver.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final MemberNoticeService memberNoticeService;
    private final ManagerRepository managerRepository;
    private final MemberNoticeRepository memberNoticeRepository;

    @Transactional
    public void create(Long managerId, CreateNoticeDto createNoticeDto) throws Exception {

        Optional<Manager> managerOptional = managerRepository.findById(managerId);

        if (managerOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Notice notice = Notice.builder()
                .title(createNoticeDto.getTitle())
                .content(createNoticeDto.getContent())
                .writerEmail(managerOptional.get().getEmail())
                .build();

        noticeRepository.save(notice);
        memberNoticeService.create(notice);
    }

    @Transactional(readOnly = true)
    public Page<NoticeDto> get(Pageable pageable, KeywordSearchDto keywordSearchDto) throws Exception {
        return noticeRepository.search(pageable, keywordSearchDto);
    }

    @Transactional(readOnly = true)
    public NoticeDto getById(Long noticeId) throws Exception {

        Optional<Notice> noticeOptional = noticeRepository.findById(noticeId);

        if (noticeOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        return NoticeDto.create(noticeOptional.get());
    }

    @Transactional
    public void delete(Long noticeId) throws Exception {
        memberNoticeRepository.deleteAllByIdInQuery(List.of(noticeId));
        noticeRepository.deleteById(noticeId);
    }

    @Transactional
    public void deleteAll(IdListDto idListDto) throws Exception {
        memberNoticeRepository.deleteAllByIdInQuery(idListDto.getIdList());
        noticeRepository.deleteAllById(idListDto.getIdList());
    }
}
