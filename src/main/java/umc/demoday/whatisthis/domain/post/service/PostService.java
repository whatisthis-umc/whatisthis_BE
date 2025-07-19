package umc.demoday.whatisthis.domain.post.service;

import org.springframework.data.domain.Page;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;

import java.util.List;

public interface PostService {

    Page<Post> getAllPosts(Integer page, Integer size, SortBy sort);
    Page<Post> getBestPosts(Integer page, Integer size);
    Page<Post> getAllPostsByCategory(Integer page, Integer size, SortBy sort, Category category);


}
