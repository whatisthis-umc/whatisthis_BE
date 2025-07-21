package umc.demoday.whatisthis.domain.post.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import umc.demoday.whatisthis.domain.hashtag.Hashtag;
import umc.demoday.whatisthis.domain.hashtag.repository.HashtagRepository;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;
import umc.demoday.whatisthis.domain.post_image.PostImage;
import umc.demoday.whatisthis.domain.post_image.repository.PostImageRepository;
import umc.demoday.whatisthis.domain.post_scrap.repository.PostScrapRepository;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class) // Mockito 사용을 위한 어노테이션
class PostServiceImplTest {

    @InjectMocks
    private PostServiceImpl postService; // 테스트 대상 클래스. @Mock으로 선언된 객체들이 주입됩니다.

    @Mock
    private PostRepository postRepository; // 가짜 객체로 만들 의존성 (Repository)
    @Mock
    private PostImageRepository postImageRepository;
    @Mock
    private PostScrapRepository postScrapRepository;
    @Mock
    private HashtagRepository hashtagRepository;

    @Test
    @DisplayName("생활꿀팁 게시글 단건 조회 성공")
    void getGgulPost_Success() {
        // given (준비)
        Integer postId = 1;

        // 1. 가짜 데이터(Entity) 생성
        Post fakePost = Post.builder()
                .id(postId)
                .title("전자레인지 청소 꿀팁")
                .content("내용...")
                .category("청소/분리수거") // '생활꿀팁'으로 변환될 카테고리
                .viewCount(23)
                .likeCount(324)
                .build();

        List<PostImage> fakeImages = List.of(
                new PostImage(1, "url1", fakePost),
                new PostImage(2, "url2", fakePost)
        );

        List<Hashtag> fakeHashtags = List.of(
                new Hashtag(1, "#청소", LocalDateTime.of(2025,7,18,2,22), fakePost),
                new Hashtag(2, "#정보", LocalDateTime.of(2025,7,18,3,33), fakePost)
        );

        int fakeScrapCount = 15;

        // 2. Mock Repository들이 반환할 행동 정의
        given(postRepository.findById(postId)).willReturn(Optional.of(fakePost));
        given(postImageRepository.findAllByPostId(postId)).willReturn(fakeImages);
        given(hashtagRepository.findAllByPostId(postId)).willReturn(fakeHashtags);
        given(postScrapRepository.countByPostId(postId)).willReturn(fakeScrapCount);


        // when (실행)
        PostResponseDTO.GgulPostResponseDTO result = postService.getGgulPost(postId);


        // then (검증)
        assertNotNull(result);
        assertEquals(postId, result.getPostId());
        assertEquals("전자레인지 청소 꿀팁", result.getTitle());
        assertEquals("생활꿀팁", result.getCategory()); // 카테고리 변환 로직 검증
        assertEquals(2, result.getImages().size()); // 이미지 URL 개수 검증
        assertEquals(2, result.getHashtags().size()); // 해시태그 개수 검증
        assertEquals(15, result.getScrapCount()); // 스크랩 수 검증
        assertTrue(result.getHashtags().contains("#정보"));

        // Mock 객체들의 특정 메소드가 호출되었는지 검증
        verify(postRepository).findById(postId);
        verify(postImageRepository).findAllByPostId(postId);
        verify(hashtagRepository).findAllByPostId(postId);
        verify(postScrapRepository).countByPostId(postId);
    }

    @Test
    @DisplayName("게시글 조회 실패 - 존재하지 않는 ID")
    void getGgulPost_Fail_NotFound() {
        // given (준비)
        Integer nonExistentPostId = 999;

        // 존재하지 않는 ID로 조회 시, 빈 Optional을 반환하도록 설정
        given(postRepository.findById(nonExistentPostId)).willReturn(Optional.empty());

        // when & then (실행 및 검증)
        // GeneralException이 발생하는지 검증
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            postService.getGgulPost(nonExistentPostId);
        });

        // 발생한 예외의 에러 코드가 예상과 일치하는지 검증
        assertEquals(GeneralErrorCode.NOT_FOUND_404, exception.getCode());

        // postRepository.findById() 이후의 로직은 호출되지 않았는지 검증
        verify(postImageRepository, never()).findAllByPostId(any());
        verify(hashtagRepository, never()).findAllByPostId(any());
        verify(postScrapRepository, never()).countByPostId(any());
    }
}
