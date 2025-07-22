package umc.demoday.whatisthis.domain.post.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import umc.demoday.whatisthis.domain.hashtag.Hashtag;
import umc.demoday.whatisthis.domain.hashtag.repository.HashtagRepository;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;
import umc.demoday.whatisthis.domain.post.Post;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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
                .category(Category.LIFE_TIP) // '생활꿀팁'으로 변환될 카테고리
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

        @Mock
        private MemberRepository memberRepository;
        // SecurityContextHolder를 모킹하기 위한 Mock 객체들
        @Mock
        private SecurityContext securityContext;
        @Mock
        private Authentication authentication;

        @Test
        @DisplayName("게시물 스크랩 성공 테스트")
        void 게시물_스크랩_성공_테스트() {
            // given (준비)
            Integer postId = 1;
            Integer memberId = 100;

            Post mockPost = new Post(); // 실제 엔티티 또는 가짜 엔티티
            Member mockMember = new Member();
            CustomUserDetails mockUserDetails = new CustomUserDetails(memberId, "tester", "password", new ArrayList<>());

            // SecurityContextHolder가 모킹된 Authentication 객체를 반환하도록 설정
            when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            // Authentication 객체가 CustomUserDetails를 반환하도록 설정
            when(authentication.getPrincipal()).thenReturn(mockUserDetails);

            // Repository가 Optional<Entity>를 반환하도록 설정
            when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));
            when(memberRepository.findById(memberId)).thenReturn(Optional.of(mockMember));

            // when (실행)
            postService.scrapPost(postId);

            // then (검증)
            // postScrapRepository.save()가 한 번 호출되었는지 확인
            verify(postScrapRepository, times(1)).save(any(PostScrap.class));

            // [심화] save 메서드에 전달된 PostScrap 객체의 내용 검증
            ArgumentCaptor<PostScrap> captor = ArgumentCaptor.forClass(PostScrap.class);
            verify(postScrapRepository).save(captor.capture());
            PostScrap savedScrap = captor.getValue();

            Assertions.assertThat(savedScrap.getPost()).isEqualTo(mockPost);
            Assertions.assertThat(savedScrap.getMember()).isEqualTo(mockMember);
        }

        @Test
        @DisplayName("실패 테스트: 스크랩할 게시물이 존재하지 않는 경우")
        void 스크랩_실패_테스트_게시물없음() {
            // given (준비)
            Integer postId = 999; // 존재하지 않는 ID

            // findById가 빈 Optional을 반환하도록 설정
            when(postRepository.findById(postId)).thenReturn(Optional.empty());

            // when & then (실행 및 검증)
            // GeneralException이 발생하는지 확인
            GeneralException exception = assertThrows(GeneralException.class, () -> {
                postService.scrapPost(postId);
            });

            // postScrapRepository.save()가 호출되지 않았는지 확인
            verify(postScrapRepository, never()).save(any());
        }

        @Test
        @DisplayName("실패 테스트: 인증된 사용자 정보가 없는 경우")
        void 스크랩_실패_테스트_인증된_사용자정보_없음() {
            // given (준비)
            Integer postId = 1;
            Post mockPost = new Post();

            // 게시물은 존재하지만
            when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));

            // SecurityContextHolder가 null을 반환하도록 설정 (인증 정보 없음)
            when(securityContext.getAuthentication()).thenReturn(null);
            SecurityContextHolder.setContext(securityContext);

            // when (실행)
            postService.scrapPost(postId);

            // then (검증)
            // 로직상 아무 일도 일어나지 않으므로, save 메서드가 호출되지 않았음을 검증
            verify(postScrapRepository, never()).save(any(PostScrap.class));
        }
}
