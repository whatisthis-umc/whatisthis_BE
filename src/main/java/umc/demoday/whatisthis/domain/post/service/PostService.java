package umc.demoday.whatisthis.domain.post.service;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.post.dto.MainPageResponseDTO;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;
import umc.demoday.whatisthis.global.CustomUserDetails;

import java.util.List;

public interface PostService {
    public PostResponseDTO.GgulPostResponseDTO getGgulPost(Integer postId, Member memberDetails);

    public void scrapPost (Integer postId, Member memberDetails);
  
    public void deleteScrap (Integer scrapId, Member memberDetails);

    public PostResponseDTO.GgulPostsByCategoryResponseDTO getGgulPostsByCategory(Category category, SortBy sort, Integer page, Integer size);

    PostResponseDTO.GgulPostsByAiResponseDTO  getPostsByAiRecommendation(Member memberDetails, Integer page, Integer size, Category category);

    public MainPageResponseDTO getAllGgulPosts(Category category, Integer page, Integer size, Member memberDetails);

}
