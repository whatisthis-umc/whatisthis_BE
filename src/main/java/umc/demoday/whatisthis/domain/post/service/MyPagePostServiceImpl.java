package umc.demoday.whatisthis.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.comment.repository.CommentRepository;
import umc.demoday.whatisthis.domain.comment_like.repository.CommentLikeRepository;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.code.PostErrorCode;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import java.util.List;

import static umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPagePostServiceImpl implements MyPagePostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Override
    public Page<Post> getMyPosts(Integer page, Integer size, Member member) {

        if (page < 0 || size <= 0) {
            throw new GeneralException(PostErrorCode.INVALID_PAGE_REQUEST);
        }

        if (member == null) {
            throw new GeneralException(MEMBER_NOT_FOUND);
        }

        Pageable pageable = PageRequest.of(page, size);

        Member fetchedMember = memberRepository.findByIdWithProfileImage(member.getId())
                .orElseThrow(() -> new GeneralException(MEMBER_NOT_FOUND));

        return postRepository.findAllByMember(fetchedMember, pageable);
    }

    @Override
    public void deletePost(Integer postId, Member member) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND_404));

        if (!post.getMember().getId().equals(member.getId())) {
            throw new GeneralException(GeneralErrorCode.FORBIDDEN_403);
        }

        List<Integer> commentIds = commentRepository.findIdsByPostId(postId); // 커스텀 쿼리
        if (!commentIds.isEmpty()) {
            commentLikeRepository.deleteByCommentIds(commentIds);  // 좋아요 선삭제
            commentRepository.deleteByIds(commentIds);             // 댓글 하드 삭제
        }

        postRepository.delete(post);
    }

}
