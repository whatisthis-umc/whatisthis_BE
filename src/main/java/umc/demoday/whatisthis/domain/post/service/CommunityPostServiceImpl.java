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
import umc.demoday.whatisthis.domain.hashtag.Hashtag;
import umc.demoday.whatisthis.domain.hashtag.repository.HashtagRepository;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.code.PostErrorCode;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;
import umc.demoday.whatisthis.domain.post_image.PostImage;
import umc.demoday.whatisthis.domain.post_image.repository.PostImageRepository;
import umc.demoday.whatisthis.domain.post_like.PostLike;
import umc.demoday.whatisthis.domain.post_like.repository.PostLikeRepository;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityPostServiceImpl implements CommunityPostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final HashtagRepository hashtagRepository;
    private final PostImageRepository postImageRepository;

    private void checkPageAndSize(Integer page, Integer size) {
        if (page < 0 || size <= 0) {
            throw new GeneralException(PostErrorCode.INVALID_PAGE_REQUEST);
        }
    }

    @Override
    public Page<Post> getAllPosts(Integer page, Integer size, SortBy sort) {

        checkPageAndSize(page, size);

        Pageable pageable;

        if (sort == SortBy.BEST) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount")); // 예: 인기순 정렬 기준
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")); // 기본은 최신순
        }

        List<Category> allowedCategories = List.of(
                Category.TIP,
                Category.ITEM,
                Category.SHOULD_I_BUY,
                Category.CURIOUS
        );

        return postRepository.findByCategoryIn(allowedCategories, pageable);
    }

    @Override
    public Page<Post> getBestPosts(Integer page, Integer size) {

        checkPageAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount"));
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);

        List<Category> allowedCategories = List.of(
                Category.TIP,
                Category.ITEM,
                Category.SHOULD_I_BUY,
                Category.CURIOUS
        );

        return postRepository.findByCreatedAtAfterAndCategoryIn(oneWeekAgo, allowedCategories, pageable);
    }

    @Override
    public Page<Post> getAllPostsByCategory(Integer page, Integer size, SortBy sort, Category category) {
        Pageable pageable;

        checkPageAndSize(page, size);

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
        return postRepository.findById(id).orElseThrow(() -> new GeneralException(PostErrorCode.POST_NOT_FOUND));
    }

    @Override
    public Page<Comment> getCommentListByPost(Integer page, Integer size, SortBy sort, Post post) {

        checkPageAndSize(page, size);

        Pageable pageable;

        if (sort == SortBy.BEST) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount"));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        }
        return commentRepository.findAllByPost(post,pageable);
    }

    @Override
    public List<Hashtag> getHashtagListByPost(Post post) {
        return hashtagRepository.findAllByPostId(post.getId());
    }

    @Override
    public List<PostImage> getPostImageListByPost(Post post) {
        return postImageRepository.findAllByPostId(post.getId());
    }

    @Override
    public void plusOneViewCount(Post post) {
        postRepository.increaseViewCount(post.getId());
    }


    @Override
    public void likePost(Post post, Member member) {

        if (postLikeRepository.existsByMemberAndPost(member, post)) {
            throw new GeneralException(PostErrorCode.ALREADY_LIKED_POST);
        }

        if (post.getMember().getId().equals(member.getId())) {
            throw new GeneralException(PostErrorCode.CANNOT_LIKE_OWN_POST);
        }

        postRepository.increaseLikeCount(post.getId());
        postLikeRepository.save(PostLike.builder().post(post).member(member)
                .build());

        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
    }

    @Transactional
    @Override
    public void unLikePost(Post post, Member member) {

        if (!postLikeRepository.existsByMemberAndPost(member, post)) {
            throw new GeneralException(PostErrorCode.ALREADY_UNLIKED_POST);
        }

        postRepository.decreaseLikeCount(post.getId());
        postLikeRepository.deletePostLikeByPostAndMember(post, member);
    }

}