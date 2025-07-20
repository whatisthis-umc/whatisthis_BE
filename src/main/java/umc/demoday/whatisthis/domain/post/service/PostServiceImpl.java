package umc.demoday.whatisthis.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.comment.Comment;
import umc.demoday.whatisthis.domain.comment.repository.CommentRepository;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.dto.PostRequestDTO;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;
import umc.demoday.whatisthis.domain.post_like.PostLike;
import umc.demoday.whatisthis.domain.post_like.repository.PostLikeRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

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

    @Override
    public Post insertNewPost(Post post) {

        return postRepository.save(post);
    }

    @Transactional
    @Override
    public Post getPost(Integer id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Comment> getCommentListByPost(Integer page, Integer size, SortBy sort, Post post) {
        Pageable pageable;

        if (sort == SortBy.BEST) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount"));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        }
        return commentRepository.findAllByPost(post,pageable);
    }

    @Transactional
    @Override
    public void plusOneViewCount(Post post) {
        postRepository.increaseViewCount(post.getId());
    }

    @Transactional
    @Override
    public void likePost(Post post, Member member) {
        if (postLikeRepository.existsByMemberAndPost(member, post)) {
            System.out.println("예외 처리 예정");}

        postRepository.increaseLikeCount(post.getId());
        postLikeRepository.save(PostLike.builder().post(post).member(member)
                .build());

        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
    }

    @Transactional
    @Override
    public void unLikePost(Post post, Member member) {
        if (!postLikeRepository.existsByMemberAndPost(member, post)) {
            System.out.println("예외 처리 예정");}

        postRepository.decreaseLikeCount(post.getId());
        postLikeRepository.deletePostLikeByPostAndMember(post, member);

       Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
    }

}