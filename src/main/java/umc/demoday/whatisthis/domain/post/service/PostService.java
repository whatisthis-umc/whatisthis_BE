package umc.demoday.whatisthis.domain.post.service;

import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;

public interface PostService {
    public PostResponseDTO.GgulPostResponseDTO getGgulPost(Integer postId);
    public PostResponseDTO.GgulPostsByCategoryResponseDTO getGgulPostsByCategory(Category category, SortBy sort, Integer page, Integer size);
}
