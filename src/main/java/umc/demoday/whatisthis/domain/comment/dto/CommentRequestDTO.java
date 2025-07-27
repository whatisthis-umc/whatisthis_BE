package umc.demoday.whatisthis.domain.comment.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class CommentRequestDTO {

    @Getter @Setter
    public static class NewCommentRequestDTO {

        @NotBlank(message = "댓글 내용란이 비어있습니다.")
        @Size(max = 50, message = "댓글은 50자 이내로 작성가능합니다.")
        String content;

        @Nullable
        Integer parentCommentId;
    }

    @Getter @Setter
    public static class ModifyCommentRequestDTO {

        @NotBlank(message = "댓글 수정 내용란이 비어있습니다.")
        @Size(max = 50, message = "댓글은 50자 이내로 작성가능합니다.")
        String content;

    }



}
