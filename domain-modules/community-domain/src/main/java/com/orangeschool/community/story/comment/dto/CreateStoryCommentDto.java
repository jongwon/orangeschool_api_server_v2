package com.orangeschool.community.story.comment.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateStoryCommentDto {

    @Schema(description = "내용", example = "재판의 전심절차로서 행정심판을 할 수 있다. 행정심판의 절차는 법률로 정하되, 사법절차가 준용되어야 한다. 명령·규칙 또는 처분이 헌법이나 법률에 위반되는 여부가 재판의 전제가 된 경우에는 대법원은 이를 최종적으로 심사할 권한을 가진다.\n" +
            "제안된 헌법개정안은 대통령이 20일 이상의 기간 이를 공고하여야 한다. 공무원의 직무상 불법행위로 손해를 받은 국민은 법률이 정하는 바에 의하여 국가 또는 공공단체에 정당한 배상을 청구할 수 있다. 이 경우 공무원 자신의 책임은 면제되지 아니한다.")
    private String content = "";
}
