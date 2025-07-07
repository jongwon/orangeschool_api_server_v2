package com.orangeschool.community.pick.reply.dto;


import com.orangeschool.common.dto.response.CommonDto;
import com.orangeschool.common.util.Functions;
import com.orangeschool.member.commonMember.entity.CommonMember;
import com.orangeschool.community.pick.reply.entity.PickReply;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PickReplyDto extends CommonDto {

    private Long memberId;
    private String originFileName;
    private String serverFileName;
    private String fileUrl;
    private String parentNickName;
    private String receiverNickname;
    private String content;
    private String dateTitle;

    public static PickReplyDto create(PickReply pickReply) {

        CommonMember sender = pickReply.getSender();
        CommonMember receiver = pickReply.getReceiver();

        PickReplyDto pickReplyDto = PickReplyDto.builder()
                .memberId(sender.getId())
                .originFileName(sender.getOriginFileName())
                .serverFileName(sender.getServerFileName())
                .fileUrl(sender.getFileUrl())
                .parentNickName(sender.getParentNickName())
                .receiverNickname(receiver.getNickName())
                .content(pickReply.getContent())
                .dateTitle(Functions.getInstance().getDateTitle(pickReply.getCreatedAt()))
                .build();

        pickReplyDto.setCreatedAt(pickReply.getCreatedAt());
        pickReplyDto.setUpdatedAt(pickReply.getUpdatedAt());
        pickReplyDto.setId(pickReply.getId());

        return pickReplyDto;
    }


}
