package umc.demoday.whatisthis.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.enums.SortBy;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public List<Post> findAllPosts(Integer page, Integer size, SortBy sort) {


        return List.of();
    }

    @Override
    public List<Post> findAllPostsByCategory(Integer page, Integer size, String sort,SortBy Category) {
        return List.of();
    }
}
