package umc.demoday.whatisthis.domain.post_scrap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.converter.PostConverter;
import umc.demoday.whatisthis.domain.post.dto.MainPageResponseDTO;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;
import umc.demoday.whatisthis.domain.post_like.PostLike;
import umc.demoday.whatisthis.domain.post_scrap.PostScrap;
import umc.demoday.whatisthis.domain.post_scrap.converter.MyScrapPageConverter;
import umc.demoday.whatisthis.domain.post_scrap.dto.resDTO.MyScrapPageResDTO;
import umc.demoday.whatisthis.domain.post_scrap.repository.PostScrapRepository;
import umc.demoday.whatisthis.domain.post.converter.PageConverter;

@Service
@RequiredArgsConstructor
public class PostScrapQueryServiceImpl implements PostScrapQueryService {

    private final PostScrapRepository postScrapRepository;
    private final MyScrapPageConverter myScrapPageConverter;

    @Override
    public MyScrapPageResDTO getMyPostScrapList(Member loginUser, Pageable pageable) {
        Page<PostScrap> scrapPage = postScrapRepository.findByMemberIdWithPost(loginUser.getId(), pageable);
        return myScrapPageConverter.toMyScrapPageResDTO(scrapPage);
    }

}
