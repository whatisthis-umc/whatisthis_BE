package umc.demoday.whatisthis.domain.post.service;

import umc.demoday.whatisthis.domain.post.Post;

import java.util.List;

public interface PostService {

    List<Post> findAll(Integer page, Integer size, String sort);
    List<Post> findAllByCategory(String Category,Integer page, Integer size);
    List<Post> sortPosts(List<Post> postList,String sort);
}
