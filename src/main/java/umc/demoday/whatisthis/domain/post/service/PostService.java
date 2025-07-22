package umc.demoday.whatisthis.domain.post.service;

import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;

public interface PostService {
    public PostResponseDTO.GgulPostResponseDTO getGgulPost(Integer postId);

    public void scrapPost (Integer postId);
    public void deleteScrap (Integer postId, Integer scrapId);
}
