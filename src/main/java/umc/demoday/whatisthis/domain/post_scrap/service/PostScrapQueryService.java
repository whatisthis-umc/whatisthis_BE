package umc.demoday.whatisthis.domain.post_scrap.service;

import org.springframework.data.domain.Pageable;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.post.dto.MainPageResponseDTO;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post_scrap.dto.resDTO.MyScrapPageResDTO;

public interface PostScrapQueryService {
    MyScrapPageResDTO getMyPostScrapList(Member member, Pageable pageable);
}
