package umc.demoday.whatisthis.domain.post.service;

import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.enums.SortBy;

import java.util.List;

public interface PostService {

    List<Post> findAllPosts(Integer page, Integer size, SortBy sort);
    List<Post> findAllPostsByCategory(Integer page, Integer size, String sort,SortBy Category);


}
