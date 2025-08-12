package umc.demoday.whatisthis.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.admin.redis.async.PostEvent;
import umc.demoday.whatisthis.domain.comment.Comment;
import umc.demoday.whatisthis.domain.comment.repository.CommentRepository;
import umc.demoday.whatisthis.domain.hashtag.Hashtag;
import umc.demoday.whatisthis.domain.hashtag.repository.HashtagRepository;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.code.PostErrorCode;
import umc.demoday.whatisthis.domain.post.dto.MyPagePostResponseDTO;
import umc.demoday.whatisthis.domain.post.dto.PostRequestDTO;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;
import umc.demoday.whatisthis.domain.post_image.PostImage;
import umc.demoday.whatisthis.domain.post_image.repository.PostImageRepository;
import umc.demoday.whatisthis.domain.post_like.PostLike;
import umc.demoday.whatisthis.domain.post_like.repository.PostLikeRepository;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityPostServiceImpl implements CommunityPostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final HashtagRepository hashtagRepository;
    private final PostImageRepository postImageRepository;

    // 이벤트 컨트롤을 위한 도입
    private final ApplicationEventPublisher eventPublisher;

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
    public Post insertNewPost(PostRequestDTO.NewPostRequestDTO request, List<String> imageUrls, Member member) {

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .member(member)
                .likeCount(0)
                .viewCount(0)
                .build();

        // PostImage 리스트 생성 후 Post에 추가
        List<PostImage> postImages = imageUrls.stream()
                .map(url -> PostImage.builder()
                        .imageUrl(url)
                        .post(post)  // savedPost가 아니라 post로 세팅! (아직 저장 전이라 post 객체를 그대로 사용)
                        .build())
                .collect(Collectors.toList());

        // 해시태그 리스트 생성 후 Post에 추가
        if (request.getHashtags() != null) {
            List<Hashtag> hashtags = request.getHashtags().stream()
                    .map(content -> Hashtag.builder()
                            .content(content)
                            .post(post)
                            .build())
                    .collect(Collectors.toList());
            post.getHashtagList().addAll(hashtags);
        }


        post.getPostImageList().addAll(postImages);

        //redis에  게시글 저장
        eventPublisher.publishEvent(new PostEvent(post.getId(), PostEvent.ActionType.CREATED_OR_UPDATED));

        // Post 저장 시 cascade 옵션으로 PostImage도 함께 저장됨
        return postRepository.save(post);
    }

    @Override
    public Post updatePost(Integer postId,PostRequestDTO.ModifyPostRequestDTO request, List<String> imageUrls, Member member) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new GeneralException(PostErrorCode.POST_NOT_FOUND));

        if (!post.getMember().getId().equals(member.getId())) {
            throw new GeneralException(GeneralErrorCode.FORBIDDEN_403);
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCategory(request.getCategory());

        post.getPostImageList().clear();
        List<PostImage> postImages = imageUrls.stream()
                .map(url -> PostImage.builder()
                        .imageUrl(url)
                        .post(post)
                        .build())
                .toList();
        post.getPostImageList().addAll(postImages);

        post.getHashtagList().clear();
        if (request.getHashtags() != null) {
            List<Hashtag> hashtags = request.getHashtags().stream()
                    .map(content -> Hashtag.builder()
                            .content(content)
                            .post(post)
                            .build())
                    .toList();
            post.getHashtagList().addAll(hashtags);
        }

        //redis 에 저장
        eventPublisher.publishEvent(new PostEvent(post.getId(), PostEvent.ActionType.CREATED_OR_UPDATED));

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