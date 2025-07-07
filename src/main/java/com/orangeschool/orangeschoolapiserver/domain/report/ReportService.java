package com.orangeschool.orangeschoolapiserver.domain.report;

import com.orangeschool.orangeschoolapiserver.common.dto.request.KeywordSearchDto;
import com.orangeschool.orangeschoolapiserver.common.response.CustomException;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.repository.CommonMemberRepository;
import com.orangeschool.orangeschoolapiserver.domain.pick.comment.entity.PickComment;
import com.orangeschool.orangeschoolapiserver.domain.pick.comment.repository.PickCommentRepository;
import com.orangeschool.orangeschoolapiserver.domain.pick.reply.entity.PickReply;
import com.orangeschool.orangeschoolapiserver.domain.pick.reply.repository.PickReplyRepository;
import com.orangeschool.orangeschoolapiserver.domain.report.dto.CreateReportDto;
import com.orangeschool.orangeschoolapiserver.domain.report.dto.ReportDto;
import com.orangeschool.orangeschoolapiserver.domain.report.entity.Report;
import com.orangeschool.orangeschoolapiserver.domain.report.repository.ReportRepository;
import com.orangeschool.orangeschoolapiserver.domain.story.comment.entity.StoryComment;
import com.orangeschool.orangeschoolapiserver.domain.story.comment.repository.StoryCommentRepository;
import com.orangeschool.orangeschoolapiserver.domain.story.reply.entity.StoryReply;
import com.orangeschool.orangeschoolapiserver.domain.story.reply.repository.StoryReplyRepository;
import com.orangeschool.orangeschoolapiserver.domain.story.story.entity.Story;
import com.orangeschool.orangeschoolapiserver.domain.story.story.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final CommonMemberRepository commonMemberRepository;
    private final StoryRepository storyRepository;
    private final StoryCommentRepository storyCommentRepository;
    private final PickCommentRepository pickCommentRepository;
    private final StoryReplyRepository storyReplyRepository;
    private final PickReplyRepository pickReplyRepository;

    @Transactional(readOnly = true)
    public Page<ReportDto> get(Pageable pageable, KeywordSearchDto keywordSearchDto) throws Exception {

        return reportRepository.search(pageable, keywordSearchDto);
    }


    @Transactional(readOnly = true)
    public ReportDto getById(Long reportId) throws Exception {

        Optional<Report> reportOptional = reportRepository.findById(reportId);
        if (!reportOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }
        return ReportDto.create(reportOptional.get());
    }

    @Transactional
    public void delete(Long reportId) throws Exception {
        Optional<Report> reportOptional = reportRepository.findById(reportId);
        if (!reportOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }
        reportRepository.delete(reportOptional.get());
    }


    @Transactional
    public void create(Long reporterId, Long reportedMemberId, CreateReportDto createReportDto) throws Exception {
        Optional<CommonMember> reporterOptional = commonMemberRepository.findById(reporterId);
        if (!reporterOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }
        CommonMember reporter = reporterOptional.get();

        Optional<CommonMember> reportedMemberOptional = commonMemberRepository.findById(reportedMemberId);
        if (!reportedMemberOptional.isPresent()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }
        CommonMember reportedMember = reportedMemberOptional.get();

        String boardTitle = "";
        String boardContent = "";
        String comment = "";

        if (createReportDto.getIsStory()) {
            // 스토리 신고
            if (createReportDto.getStoryId() != 0L) {
                Optional<Story> storyOptional = storyRepository.findById(createReportDto.getStoryId());

                if (storyOptional.isPresent()) {
                    boardTitle = storyOptional.get().getTitle();
                    boardContent = storyOptional.get().getContent();
                }
            }

            if (createReportDto.getCommentId() != 0L) {
                Optional<StoryComment> storyCommentOptional = storyCommentRepository.findById(createReportDto.getCommentId());

                if (storyCommentOptional.isPresent()) {
                    comment = storyCommentOptional.get().getContent();
                }
            }

            if (createReportDto.getReplyId() != 0L) {
                Optional<StoryReply> storyReplyOptional = storyReplyRepository.findById(createReportDto.getReplyId());

                if (storyReplyOptional.isPresent()) {
                    comment = storyReplyOptional.get().getContent();
                }
            }
        } else {
            // 매거진신고
            if (createReportDto.getCommentId() != 0L) {
                Optional<PickComment> pickCommentOptional = pickCommentRepository.findById(createReportDto.getCommentId());

                if (pickCommentOptional.isPresent()) {
                    comment = pickCommentOptional.get().getContent();
                }
            }

            if (createReportDto.getReplyId() != 0L) {
                Optional<PickReply> pickReplyOptional = pickReplyRepository.findById(createReportDto.getReplyId());

                if (pickReplyOptional.isPresent()) {
                    comment = pickReplyOptional.get().getContent();
                }
            }
        }

        Report report = Report.builder()
                .reporter(reporter)
                .reportedMember(reportedMember)
                .reportReason(createReportDto.getReportReason())
                .reportReasonDetail(createReportDto.getReportReasonDetail())
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .comment(comment)
                .isStory(createReportDto.getIsStory())
                .build();

        reportRepository.save(report);
    }


}
