package umc.demoday.whatisthis.domain.post_like.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.converter.PostConverter;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;
import umc.demoday.whatisthis.domain.post_like.PostLike;
import umc.demoday.whatisthis.domain.post_like.repository.PostLikeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostLikeQueryServiceImpl implements PostLikeQueryService {

    private final PostLikeRepository postLikeRepository;

    @Override
    public PostResponseDTO.CommunityPostPreviewListDTO getMyPostLikeList(Member member, Pageable pageable) {
        // 페이징으로 좋아요 목록 조회
        Page<PostLike> likePage = postLikeRepository.findByMember(member, pageable);

        // PostLike에서 Post만 추출해서 Page<Post>로 변환
        Page<Post> postPage = likePage.map(PostLike::getPost);

        // 컨버터 호출 (Page<Post> 받도록 수정 필요)
        return PostConverter.toCommunityPostPreviewListDTO(postPage);
    }
}