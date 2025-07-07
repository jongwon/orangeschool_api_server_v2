package com.orangeschool.orangeschoolapiserver.domain.story.reply.dto;


import com.orangeschool.orangeschoolapiserver.common.dto.response.CommonDto;
import com.orangeschool.orangeschoolapiserver.common.utils.Functions;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import com.orangeschool.orangeschoolapiserver.domain.story.reply.entity.StoryReply;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoryReplyDto extends CommonDto {

    private Long memberId;
    private String originFileName;
    private String serverFileName;
    private String fileUrl;
    private String parentNickName;
    private String receiverNickname;
    private String content;
    private String dateTitle;

    public static StoryReplyDto create(StoryReply storyReply) {

        CommonMember sender = storyReply.getSender();
        CommonMember receiver = storyReply.getReceiver();

        StoryReplyDto storyReplyDto = StoryReplyDto.builder()
                .memberId(sender.getId())
                .originFileName(sender.getOriginFileName())
                .serverFileName(sender.getServerFileName())
                .fileUrl(sender.getFileUrl())
                .parentNickName(sender.getParentNickName())
                .receiverNickname(receiver.getNickName())
                .content(storyReply.getContent())
                .dateTitle(Functions.getInstance().getDateTitle(storyReply.getCreatedAt()))
                .build();

        storyReplyDto.setCreatedAt(storyReply.getCreatedAt());
        storyReplyDto.setUpdatedAt(storyReply.getUpdatedAt());
        storyReplyDto.setId(storyReply.getId());

        return storyReplyDto;
    }


}
