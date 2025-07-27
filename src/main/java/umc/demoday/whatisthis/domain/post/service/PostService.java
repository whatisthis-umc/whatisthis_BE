package umc.demoday.whatisthis.domain.post.service;

import umc.demoday.whatisthis.domain.post.dto.MainPageResponseDTO;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;

import java.util.List;

public interface PostService {
    public PostResponseDTO.GgulPostResponseDTO getGgulPost(Integer postId);

    public void scrapPost (Integer postId);
  
    public void deleteScrap (Integer scrapId);

    public PostResponseDTO.GgulPostsByCategoryResponseDTO getGgulPostsByCategory(Category category, SortBy sort, Integer page, Integer size);
    public MainPageResponseDTO getAllGgulPosts(Category category, Integer page, Integer size);

}
