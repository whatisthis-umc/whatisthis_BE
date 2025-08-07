package umc.demoday.whatisthis.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.valid.annotation.EnumValid;

import java.util.List;

public class PostRequestDTO {

    @Getter @Setter
    public static class NewPostRequestDTO {

        @NotNull(message = "카테고리 입력은 필수입니다.")
        @EnumValid(anyOf = {Category.TIP, Category.ITEM, Category.SHOULD_I_BUY, Category.CURIOUS},
                message = "커뮤니티 글 작성은 TIP, ITEM, SHOULD_I_BUY, CURIOUS 중 하나만 선택 가능합니다.")
        Category category;

        @NotBlank(message = "제목 입력은 필수입니다.")
        @Size(max = 20, message = "제목은 20자 이내로 작성해야합니다.")
        String title;

        @NotBlank(message = "본문 입력은 필수입니다.")
        @Size(max = 3000, message = "본문은 3000자 이내로 작성해야합니다.")
        String content;

        //List<String> imageUrls; // 추후 삭제해도 될듯

        List<String> hashtags;
    }

    @Getter @Setter
    public static class ModifyPostRequestDTO {

        @NotNull(message = "카테고리 입력은 필수입니다.")
        @EnumValid(anyOf = {Category.TIP, Category.ITEM, Category.SHOULD_I_BUY, Category.CURIOUS},
                message = "커뮤니티 글 작성은 TIP, ITEM, SHOULD_I_BUY, CURIOUS 중 하나만 선택 가능합니다.")
        Category category;

        @NotBlank(message = "제목 입력은 필수입니다.")
        @Size(max = 20, message = "제목은 20자 이내로 작성해야합니다.")
        String title;

        @NotBlank(message = "본문 입력은 필수입니다.")
        @Size(max = 3000, message = "본문은 3000자 이내로 작성해야합니다.")
        String content;

        //List<String> imageUrls; // 추후 삭제해도 될듯

        List<String> hashtags;
    }

}
