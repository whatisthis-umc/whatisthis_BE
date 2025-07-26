package umc.demoday.whatisthis.domain.admin.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import umc.demoday.whatisthis.domain.admin.dto.AdminPostReqDTO;
import umc.demoday.whatisthis.domain.admin.dto.AdminPostResDTO;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;
import umc.demoday.whatisthis.domain.post_image.PostImage;
import umc.demoday.whatisthis.domain.post_image.repository.PostImageRepository;
import umc.demoday.whatisthis.domain.post_scrap.repository.PostScrapRepository;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class) // Mockito 확장 기능을 사용합니다.
class AdminPostServiceTest {

    @InjectMocks // 테스트 대상인 AdminPostService에 Mock 객체들을 주입합니다.
    private AdminPostService adminPostService;

    @Mock // PostRepository의 Mock 객체를 생성합니다.
    private PostRepository postRepository;

    @Mock // PostScrapRepository의 Mock 객체를 생성합니다.
    private PostScrapRepository postScrapRepository;

    @Test
    @DisplayName("게시글 상세 조회 성공")
    void 게시글_상세_조회_성공() {
        // given (테스트 데이터 준비)
        Integer postId = 1;
        int scrapCount = 10;

        // 테스트에 사용할 Member, Post, PostImage 객체 생성
        Member member = Member.builder()
                .nickname("테스트유저")
                .build();

        Post post = Post.builder()
                .id(postId)
                .title("테스트 제목")
                .content("테스트 내용")
                .category(Category.LIFE_TIP)
                .member(member)
                .viewCount(100)
                .build();
        // Post 객체 내부의 생성 시간 필드를 테스트를 위해 수동으로 설정
        post.setCreatedAt(LocalDateTime.now());

        PostImage image1 =
                PostImage.builder()
                        .imageUrl("http://image.url/1")
                        .build();

        PostImage image2 =
                PostImage.builder()
                        .imageUrl("http://image.url/2")
                        .build();

        post.setPostImageList(List.of(image1, image2));


        // Mock 객체의 동작 정의
        // postRepository.findById(1)이 호출되면 위에서 만든 post 객체를 Optional로 감싸서 반환
        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        // postScrapRepository.countByPostId(1)이 호출되면 10을 반환
        given(postScrapRepository.countByPostId(postId)).willReturn(scrapCount);

        // when (테스트할 메서드 호출)
        AdminPostResDTO result = adminPostService.getPost(postId);

        // then (결과 검증)
        assertThat(result).isNotNull();
        assertThat(result.getPostId()).isEqualTo(postId);
        assertThat(result.getTitle()).isEqualTo("테스트 제목");
        assertThat(result.getNickname()).isEqualTo("테스트유저");
        assertThat(result.getScrapCount()).isEqualTo(scrapCount);
        assertThat(result.getViewCount()).isEqualTo(100);
        assertThat(result.getImageUrls()).hasSize(2);
        assertThat(result.getImageUrls().get(0)).isEqualTo("http://image.url/1");
        assertThat(result.getCategory()).isEqualTo(Category.LIFE_TIP); // "_TIP"으로 끝나므로 LIFE_TIP

        // Mock 객체가 예상대로 호출되었는지 검증
        verify(postRepository).findById(postId);
        verify(postScrapRepository).countByPostId(postId);
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회 시 예외 발생")
    void 존재하지_않는_게시글_조회_실패() {
        // given (테스트 데이터 준비)
        Integer nonExistentPostId = 999;

        // postRepository.findById(999)가 호출되면 빈 Optional을 반환하도록 설정
        given(postRepository.findById(nonExistentPostId)).willReturn(Optional.empty());

        // when & then (예외 발생 검증)
        // adminPostService.getPost(999)를 호출했을 때 GeneralException이 발생하는지 확인
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            adminPostService.getPost(nonExistentPostId);
        });

        // 발생한 예외의 에러 코드가 NOT_FOUND_404인지 확인
        assertThat(exception.getCode()).isEqualTo(GeneralErrorCode.NOT_FOUND_404);

        // postScrapRepository.countByPostId는 호출되지 않았는지 검증
        // verify(postScrapRepository, never()).countByPostId(anyInt());
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    void 게시글_삭제_성공() {
        // given (테스트 데이터 준비)
        Integer postId = 1;
        Post existingPost = Post.builder().id(postId).build();

        // postRepository.findById(1)가 호출되면 existingPost 객체를 Optional로 감싸서 반환
        given(postRepository.findById(postId)).willReturn(Optional.of(existingPost));

        // when (테스트할 메서드 호출 및 결과 검증)
        // 예외가 발생하지 않아야 함
        Integer deletedPostId = assertDoesNotThrow(() -> adminPostService.deletePost(postId));

        // then (결과 검증)
        assertThat(deletedPostId).isEqualTo(postId);

        // Mock 객체가 예상대로 호출되었는지 검증
        // postRepository의 findById가 1번 호출되었는지 확인
        verify(postRepository).findById(postId);
        // postRepository의 delete가 1번 호출되었는지 확인
        verify(postRepository).delete(existingPost);
    }

    @Test
    @DisplayName("존재하지 않는 게시글 삭제 시 예외 발생")
    void 존재하지_않는_게시글_삭제_실패() {
        // given (테스트 데이터 준비)
        Integer nonExistentPostId = 999;

        // postRepository.findById(999)가 호출되면 빈 Optional을 반환하도록 설정
        given(postRepository.findById(nonExistentPostId)).willReturn(Optional.empty());

        // when & then (예외 발생 검증)
        // adminPostService.deletePost(999)를 호출했을 때 GeneralException이 발생하는지 확인
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            adminPostService.deletePost(nonExistentPostId);
        });

        // 발생한 예외의 에러 코드가 NOT_FOUND_404인지 확인
        assertThat(exception.getCode()).isEqualTo(GeneralErrorCode.NOT_FOUND_404);

        // Mock 객체가 예상대로 호출되었는지 검증
        // postRepository의 findById가 1번 호출되었는지 확인
        verify(postRepository).findById(nonExistentPostId);
        // postRepository의 delete 메서드는 절대 호출되지 않았는지 확인
        verify(postRepository, never()).delete(any(Post.class));
    }


    @Mock
    private PostImageRepository postImageRepository;


    @Test
    @DisplayName("게시글 수정 성공 - 모든 필드 업데이트")
    void 게시글_수정_성공_전체() {
        // given (테스트 데이터 준비)
        Integer postId = 1;

        // 1. 기존 Post 데이터 생성
        Post existingPost = Post.builder()
                .id(postId)
                .title("오래된 제목")
                .content("오래된 내용")
                .category(Category.LIFE_ITEM)
                .build();

        // 연관관계 편의를 위해 기존 이미지 리스트 설정
        existingPost.setPostImageList(new ArrayList<>(List.of(PostImage.builder().imageUrl("http://old.image/1").post(existingPost).build())));
        existingPost.setUpdatedAt(LocalDateTime.now().minusDays(1));


        // 2. 업데이트 요청 DTO 생성
        List<String> newImageUrls = List.of("http://new.image/1", "http://new.image/2");

        AdminPostReqDTO.updatePostReqDTO request = new AdminPostReqDTO.updatePostReqDTO(
                "새로운 제목",
                "새로운 내용",
                Category.LIFE_TIP,
                newImageUrls
        );

        // 3. Mock 객체 동작 정의
        given(postRepository.findById(postId)).willReturn(Optional.of(existingPost));

        // when (테스트할 메서드 호출)
        AdminPostResDTO.updatePostResDTO result = adminPostService.updatePost(postId, request);

        // then (결과 검증)
        assertThat(result).isNotNull();
        assertThat(result.getPostId()).isEqualTo(postId);
        assertThat(result.getTitle()).isEqualTo("새로운 제목");
        assertThat(result.getContent()).isEqualTo("새로운 내용");
        assertThat(result.getCategory()).isEqualTo(Category.LIFE_TIP);
        assertThat(result.getImageUrls()).hasSize(2);
        assertThat(result.getImageUrls()).containsExactly("http://new.image/1", "http://new.image/2");


        // Mock 객체 호출 검증
        verify(postRepository).findById(postId);
        verify(postImageRepository).deleteAllByPost(existingPost); // 기존 이미지 삭제 메서드 호출 확인
        verify(postImageRepository).saveAll(any()); // 새 이미지 저장 메서드 호출 확인
    }

    @Test
    @DisplayName("게시글 수정 성공 - 일부 필드(제목, 내용)만 업데이트")
    void 게시글_수정_성공_부분() {
        // given
        Integer postId = 2;
        Post existingPost = Post.builder()
                .id(postId)
                .title("원본 제목")
                .content("원본 내용")
                .category(Category.LIFE_ITEM) // 카테고리는 그대로 유지되어야 함
                .build();
        existingPost.setPostImageList(new ArrayList<>(List.of(PostImage.builder().imageUrl("http://keep.this/1").post(existingPost).build())));

        // 제목과 내용만 업데이트하고, 이미지와 카테고리는 null로 보냄
        AdminPostReqDTO.updatePostReqDTO request = new AdminPostReqDTO.updatePostReqDTO(
                "수정된 제목",
                "수정된 내용",
                null, // 카테고리는 업데이트하지 않음
                null  // 이미지도 업데이트하지 않음
        );

        given(postRepository.findById(postId)).willReturn(Optional.of(existingPost));

        // when
        AdminPostResDTO.updatePostResDTO result = adminPostService.updatePost(postId, request);

        // then (결과 검증)
        assertThat(result).isNotNull();
        assertThat(result.getPostId()).isEqualTo(postId);
        assertThat(result.getTitle()).isEqualTo("수정된 제목");
        assertThat(result.getContent()).isEqualTo("수정된 내용");

        verify(postRepository).findById(postId);
        // request의 imageUrls가 null이므로 이미지 관련 repository 메서드는 호출되지 않아야 함
        verify(postImageRepository, never()).deleteAllByPost(any(Post.class));
        verify(postImageRepository, never()).saveAll(any());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정 시 예외 발생")
    void 존재하지_않는_게시글_수정_실패() {
        // given
        Integer nonExistentPostId = 999;
        AdminPostReqDTO.updatePostReqDTO request = new AdminPostReqDTO.updatePostReqDTO("아무거나", null, null, null);

        given(postRepository.findById(nonExistentPostId)).willReturn(Optional.empty());

        // when & then
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            adminPostService.updatePost(nonExistentPostId, request);
        });

        assertThat(exception.getCode()).isEqualTo(GeneralErrorCode.NOT_FOUND_404);

        // 게시글이 없으므로 이미지 관련 로직은 절대 실행되면 안 됨
        verify(postImageRepository, never()).deleteAllByPost(any());
        verify(postImageRepository, never()).saveAll(any());
    }
}
