package umc.demoday.whatisthis.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.hashtag.repository.HashtagRepository;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.converter.PostConverter;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;
import umc.demoday.whatisthis.domain.post_image.repository.PostImageRepository;
import umc.demoday.whatisthis.domain.post_scrap.repository.PostScrapRepository;
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

    public PostResponseDTO.GgulPostResponseDTO getGgulPost(Integer postId) {
        // 1. 게시글 정보 조회
        Post post = postRepository.findById(postId).orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND_404));

        //Post.category (e.g. 청소/분리수거 등) 기준으로 DTO category(e. g. 생활 꿀팁) 설정
        String category = "생활꿀팁";
        if(post.getCategory().substring(post.getCategory().length() - 1).equals("템"))  // 마지막 글자 '템'이면 생활꿀템
            category = "생활꿀템";
        // 2. 추가 정보들을 각 Repository에서 조회
        // 2-1. 이미지 목록 조회
        List<String> imageUrls = postImageRepository.findAllByPost(post).stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());

        // 2-2. 해시태그 목록 조회
        List<String> hashtags = hashtagRepository.findAllByPost(post).stream()
                .map(postHashtag -> postHashtag.getHashtag().getName())
                .collect(Collectors.toList());

        // 2-3. 스크랩 수 조회
        int postScrapCount = postScrapRepository.countByPost(post);

        // 3. 모든 데이터를 조합하여 최종 DTO 생성 후 반환
        return PostConverter.toGgulPostResponseDTO(post,category);
    }
}
