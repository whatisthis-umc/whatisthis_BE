package umc.demoday.whatisthis.domain.post_like.service;

import org.springframework.data.domain.Pageable;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;

public interface PostLikeQueryService {
    PostResponseDTO.CommunityPostPreviewListDTO getMyPostLikeList(Member member, Pageable pageable);
}
