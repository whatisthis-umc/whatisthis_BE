package umc.demoday.whatisthis.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.hashtag.Hashtag;
import umc.demoday.whatisthis.domain.hashtag.repository.HashtagRepository;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.converter.PostConverter;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;
import umc.demoday.whatisthis.domain.post_image.PostImage;
import umc.demoday.whatisthis.domain.post_image.repository.PostImageRepository;
import umc.demoday.whatisthis.domain.post_scrap.PostScrap;
import umc.demoday.whatisthis.domain.post_scrap.repository.PostScrapRepository;
import umc.demoday.whatisthis.global.CustomUserDetails;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostScrapRepository postScrapRepository;
    private final HashtagRepository hashtagRepository;
    private final MemberRepository memberRepository;

    @Override
    public PostResponseDTO.GgulPostResponseDTO getGgulPost(Integer postId) {
        // 1. 게시글 정보 조회
        Post post = postRepository.findById(postId).orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND_404));

        //Post.category 기준으로 DTO category(e. g. 생활 꿀팁) 설정
        String category = "생활꿀템";
        if(post.getCategory().name().endsWith("_TIP"))
            category = "생활꿀팁";

        // 2. 추가 정보들을 각 Repository에서 조회
        // 2-1. 이미지 목록 조회
        List<String> imageUrls = postImageRepository.findAllByPostId(postId).stream()
                .map(PostImage::getImageUrl)
                .toList();

        // 2-2. 해시태그 목록 조회
        List<String> hashtags = hashtagRepository.findAllByPostId(postId).stream()
                .map(Hashtag::getContent)
                .toList();

        // 2-3. 스크랩 수 조회
        int postScrapCount = postScrapRepository.countByPostId(postId);

        // 3. 모든 데이터를 조합하여 최종 DTO 생성 후 반환
        return PostConverter.toGgulPostResponseDTO(post,category,imageUrls,hashtags,postScrapCount);
    }

    public void scrapPost(Integer postId) {

        // Post 불러오기
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null) throw new GeneralException(GeneralErrorCode.NOT_FOUND_404);

        // 멤버 ID 불러오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            Integer memberId = userDetails.getId();
            // 멤버 엔티티 불러오기
            Member member = memberRepository.findById(memberId).orElseThrow();
            PostScrap postScrap = new PostScrap(member, post);
            postScrapRepository.save(postScrap);
        }
    }

    public void deleteScrap(Integer scrapId) {
        // 현재 멤버의 ID 찾기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer currentMemberId = userDetails.getId();

        // PostScrap 찾기
        PostScrap postScrap = postScrapRepository.findById(scrapId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND_404));

        // 스크랩 소유자가 현재 멤버인지 검증하기
        if (!postScrap.getMember().getId().equals(currentMemberId)) {
            throw new GeneralException(GeneralErrorCode.FORBIDDEN_403); // 권한 없음 예외
        }

        // 삭제하기
        postScrapRepository.delete(postScrap);
    }
}
