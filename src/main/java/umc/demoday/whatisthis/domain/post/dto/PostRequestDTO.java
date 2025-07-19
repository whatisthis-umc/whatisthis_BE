package umc.demoday.whatisthis.domain.post.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import umc.demoday.whatisthis.domain.post.enums.Category;

import java.util.List;

public class PostRequestDTO {

    @Getter @Setter
    public static class NewPostRequestDTO {

        @NotNull
        Category category;

        @NotBlank
        @Size(max = 20)
        String title;

        @NotBlank
        @Size(max = 3000)
        String content;

        List<String> imageUrls;
    }
}
