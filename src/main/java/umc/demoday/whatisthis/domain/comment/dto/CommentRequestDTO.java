package umc.demoday.whatisthis.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class CommentRequestDTO {

    @Getter @Setter
    public static class NewCommentRequestDTO {

        @NotBlank
        @Size(max = 50)
        String content;

        @NotNull
        Integer parentCommentId;
    }

    @Getter @Setter
    public static class ModifyCommentRequestDTO {

        @NotBlank
        @Size(max = 50)
        String content;

    }



}
