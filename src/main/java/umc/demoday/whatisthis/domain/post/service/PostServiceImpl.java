package umc.demoday.whatisthis.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.comment.repository.CommentRepository;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public Page<Post> getAllPosts(Integer page, Integer size, SortBy sort) {
        Pageable pageable;

        if (sort == SortBy.BEST) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount")); // 예: 인기순 정렬 기준
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")); // 기본은 최신순
        }

        return postRepository.findAll(pageable);
    }

    @Override
    public Page<Post> getBestPosts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount"));
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);

        return postRepository.findByCreatedAtAfter(oneWeekAgo, pageable);
    }

    @Override
    public Page<Post> getAllPostsByCategory(Integer page, Integer size, SortBy sort, Category category) {
        Pageable pageable;

        if (sort == SortBy.BEST) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount")); // 예: 인기순 정렬 기준
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")); // 기본은 최신순
        }

        return postRepository.findAllByCategory(category, pageable);
    }

}