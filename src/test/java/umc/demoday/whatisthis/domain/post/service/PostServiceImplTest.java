package umc.demoday.whatisthis.domain.post.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import umc.demoday.whatisthis.domain.hashtag.Hashtag;
import umc.demoday.whatisthis.domain.hashtag.repository.HashtagRepository;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.converter.PageConverter;
import umc.demoday.whatisthis.domain.post.dto.MainPageResponseDTO;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;
import umc.demoday.whatisthis.domain.post_image.PostImage;
import umc.demoday.whatisthis.domain.post_image.repository.PostImageRepository;
import umc.demoday.whatisthis.domain.post_scrap.repository.PostScrapRepository;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import java.time.LocalDateTime;
import java.util.Arrays;
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
    private PageConverter pageConverter;

    @Test
    @DisplayName("꿀팁 게시글 조회 - 인기순(BEST) 정렬 테스트")
    void 카테고리_게시글_조회_인기순_정렬_테스트() {
        // given (테스트 준비)
        Category category = Category.LIFE_TIP;
        SortBy sortBy = SortBy.BEST;
        int page = 0;
        int size = 10;
        long totalElements = 2L;

        // 1. Repository가 반환할 Page<Post> Mock 객체 생성
        List<Post> postList = List.of(new Post(), new Post()); // Mock 데이터
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount"));
        Page<Post> mockPostPage = new PageImpl<>(postList, pageable, totalElements);
        when(postRepository.findByCategory(eq(category), any(Pageable.class)))
                .thenReturn(mockPostPage);

        // 2. PageConverter가 반환할 DTO 생성
        List<PostResponseDTO.GgulPostSummaryDTO> summaryDTOs = List.of(
                new PostResponseDTO.GgulPostSummaryDTO(1, "url1", "인기글1", "요약1", List.of(), 100, 50, 10, LocalDateTime.now()),
                new PostResponseDTO.GgulPostSummaryDTO(2, "url2", "인기글2", "요약2", List.of(), 200, 45, 5, LocalDateTime.now())
        );
        PostResponseDTO.GgulPostsByCategoryResponseDTO expectedResponse = new PostResponseDTO.GgulPostsByCategoryResponseDTO(
                category, sortBy, page, size, totalElements, 1, summaryDTOs
        );
        // toGgulPostsByCategoryResponseDTO 메서드가 호출되면 위에서 만든 DTO를 반환하도록 설정
        when(pageConverter.toGgulPostsByCategoryResponseDTO(mockPostPage, category, sortBy))
                .thenReturn(expectedResponse);


        // when (테스트 실행)
        PostResponseDTO.GgulPostsByCategoryResponseDTO actualResponse = postService.getGgulPostsByCategory(category, sortBy, page, size);


        // then (결과 검증)
        Assertions.assertThat(actualResponse).isNotNull();
        // 반환된 DTO의 내용 자체를 검증
        Assertions.assertThat(actualResponse.getCategory()).isEqualTo(category);
        Assertions.assertThat(actualResponse.getSortBy()).isEqualTo(sortBy);
        Assertions.assertThat(actualResponse.getPosts()).hasSize(2);
        Assertions.assertThat(actualResponse.getPosts().get(0).getTitle()).isEqualTo("인기글1");

        // Repository에 전달된 인자(Pageable)의 정렬 기준을 검증
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(postRepository).findByCategory(eq(category), pageableCaptor.capture());
        Pageable capturedPageable = pageableCaptor.getValue();
        Assertions.assertThat(capturedPageable.getSort()).isEqualTo(Sort.by(Sort.Direction.DESC, "likeCount"));

        // Converter가 정확한 인자들로 호출되었는지 검증
        verify(pageConverter).toGgulPostsByCategoryResponseDTO(mockPostPage, category, sortBy);
    }

    @Test
    @DisplayName("꿀팁 게시글 조회 - 최신순(LATEST) 정렬 테스트")
    void 카테고리_게시글_조회_최신순_정렬_테스트() {
        // given (테스트 준비)
        Category category = Category.LIFE_TIP;
        SortBy sortBy = SortBy.LATEST;
        int page = 1; // 페이지 번호를 다르게 설정
        int size = 5;
        long totalElements = 1L;

        // 1. Repository가 반환할 Page<Post> Mock 객체 생성
        List<Post> postList = List.of(new Post());
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> mockPostPage = new PageImpl<>(postList, pageable, totalElements);

        when(postRepository.findByCategory(eq(category), any(Pageable.class)))
                .thenReturn(mockPostPage);

        // 2. PageConverter가 반환할 DTO 객체 생성
        List<PostResponseDTO.GgulPostSummaryDTO> summaryDTOs = List.of(
                new PostResponseDTO.GgulPostSummaryDTO(10, "url10", "최신글1", "요약10", List.of(), 10, 5, 2, LocalDateTime.now())
        );
        PostResponseDTO.GgulPostsByCategoryResponseDTO expectedResponse = new PostResponseDTO.GgulPostsByCategoryResponseDTO(
                category, sortBy, page, size, totalElements, 1, summaryDTOs
        );
        when(pageConverter.toGgulPostsByCategoryResponseDTO(mockPostPage, category, sortBy))
                .thenReturn(expectedResponse);


        // when (테스트 실행)
        PostResponseDTO.GgulPostsByCategoryResponseDTO actualResponse = postService.getGgulPostsByCategory(category, sortBy, page, size);


        // then (결과 검증)
        // 반환된 DTO 내용 검증
        Assertions.assertThat(actualResponse).isNotNull();
        Assertions.assertThat(actualResponse.getSortBy()).isEqualTo(sortBy);
        Assertions.assertThat(actualResponse.getPosts()).hasSize(1);
        Assertions.assertThat(actualResponse.getPosts().get(0).getPostId()).isEqualTo(10);

        // Repository에 전달된 Pageable 객체의 정렬 기준 검증
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(postRepository).findByCategory(eq(category), pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        Assertions.assertThat(capturedPageable.getSort()).isEqualTo(Sort.by(Sort.Direction.DESC, "createdAt"));
        Assertions.assertThat(capturedPageable.getPageNumber()).isEqualTo(page);
        Assertions.assertThat(capturedPageable.getPageSize()).isEqualTo(size);

        // Converter가 정확한 인자들로 호출되었는지 검증
        verify(pageConverter).toGgulPostsByCategoryResponseDTO(mockPostPage, category, sortBy);
    }

    @Test
    @DisplayName("메인 페이지 조회 - 생활꿀팁(_TIP) 카테고리 그룹 테스트")
    void 생활꿀팁_메인페이지_조회_테스트() {
        // given (테스트 준비)
        Category parentCategory = Category.LIFE_TIP;
        int page = 0;
        int size = 6;

        // 1. 서비스 로직 내에서 생성될 것으로 예상되는 카테고리 리스트
        List<Category> expectedTipCategories = Arrays.stream(Category.values())
                .filter(c -> c.name().endsWith("_TIP"))
                .toList();

        // 2. Repository가 반환할 Mock Page 객체들 (BEST, LATEST)
        Page<Post> mockBestPage = new PageImpl<>(List.of(new Post()));
        Page<Post> mockLatestPage = new PageImpl<>(List.of(new Post()));

        // 3. Repository Mocking 설정
        // 인기순(likeCount) 정렬로 호출될 때
        when(postRepository.findByCategoryIn(
                eq(expectedTipCategories),
                argThat(p -> p.getSort().equals(Sort.by(Sort.Direction.DESC, "likeCount")))
        )).thenReturn(mockBestPage);

        // 최신순(createdAt) 정렬로 호출될 때
        when(postRepository.findByCategoryIn(
                eq(expectedTipCategories),
                argThat(p -> p.getSort().equals(Sort.by(Sort.Direction.DESC, "createdAt")))
        )).thenReturn(mockLatestPage);

        // 4. Converter Mocking 설정
        MainPageResponseDTO expectedResponse = new MainPageResponseDTO(); // Mock DTO
        when(pageConverter.toMainPageResponseDTO(mockBestPage, mockLatestPage, expectedTipCategories))
                .thenReturn(expectedResponse);

        // when (테스트 실행)
        MainPageResponseDTO actualResponse = postService.getAllGgulPosts(parentCategory, page, size);

        // then (결과 검증)
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);

        // postRepository.findByCategoryIn이 총 2번 호출되었는지 확인
        verify(postRepository, times(2)).findByCategoryIn(any(), any(Pageable.class));

        // converter가 정확한 인자들로 호출되었는지 확인
        verify(pageConverter).toMainPageResponseDTO(mockBestPage, mockLatestPage, expectedTipCategories);
    }

    @Test
    @DisplayName("메인 페이지 조회 - 생활꿀템(_ITEM) 카테고리 그룹 테스트")
    void 생활꿀템_메인페이지_조회_테스트() {
        // given
        Category parentCategory = Category.LIFE_ITEM;
        int page = 0;
        int size = 6;

        // 서비스 로직 내에서 생성될 것으로 예상되는 카테고리 리스트
        List<Category> expectedItemCategories = Arrays.stream(Category.values())
                .filter(c -> c.name().endsWith("_ITEM"))
                .toList();

        // Repository가 반환할 Mock Page 객체들
        Page<Post> mockBestPage = new PageImpl<>(List.of(new Post()));
        Page<Post> mockLatestPage = new PageImpl<>(List.of(new Post()));

        // Repository Mocking 설정
        when(postRepository.findByCategoryIn(eq(expectedItemCategories), any(Pageable.class)))
                .thenReturn(mockBestPage) // 첫 번째 호출은 Best
                .thenReturn(mockLatestPage); // 두 번째 호출은 Latest

        // Converter Mocking 설정
        when(pageConverter.toMainPageResponseDTO(any(), any(), any())).thenReturn(new MainPageResponseDTO());


        // when
        postService.getAllGgulPosts(parentCategory, page, size);


        // then
        // ArgumentCaptor로 repository에 전달된 카테고리 리스트를 캡처하여 검증
        ArgumentCaptor<List<Category>> categoryListCaptor = ArgumentCaptor.forClass(List.class);
        verify(postRepository, times(2)).findByCategoryIn(categoryListCaptor.capture(), any(Pageable.class));

        // 캡처된 두 개의 리스트 모두 _ITEM 카테고리 리스트와 일치하는지 확인
        Assertions.assertThat(categoryListCaptor.getAllValues().get(0)).isEqualTo(expectedItemCategories);
        Assertions.assertThat(categoryListCaptor.getAllValues().get(1)).isEqualTo(expectedItemCategories);
    }
}
