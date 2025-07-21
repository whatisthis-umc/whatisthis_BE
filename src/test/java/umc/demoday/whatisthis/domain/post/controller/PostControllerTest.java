package umc.demoday.whatisthis.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.service.PostService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc; // HTTP 요청을 시뮬레이션하는 역할

    @Autowired
    private PostService postService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PostService postService() {
            return Mockito.mock(PostService.class);
        }
    }

    @Test
    @DisplayName("생활꿀팁 게시글 단건 조회 성공")
    void getGgulPost_Success() throws Exception {
        // given (준비)
        Integer postId = 1;

        // postServiceImpl.getGgulPost()가 반환할 가짜 결과 데이터 생성
        PostResponseDTO.GgulPostResponseDTO fakeResult = new PostResponseDTO.GgulPostResponseDTO(
                1,
                "생활꿀팁",
                Category.CLEAN_TIP,
                "전자레인지 청소 꿀팁",
                "내용...",
                List.of("#청소", "#정보"),
                List.of("sdfasdf", "sdfsdf"),
                1,
                2,
                3,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // postServiceImpl.getGgulPost()에 어떤 값이 들어오든 fakeResult를 반환하도록 설정
        given(postService.getGgulPost(any(Integer.class))).willReturn(fakeResult);

        // when & then (실행 및 검증)
        mockMvc.perform(
                        get("/posts/{post-id}", postId) // PostController의 @RequestMapping 경로를 포함
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("COMMON2000"))
                .andExpect(jsonPath("$.message").value("성공적으로 처리했습니다."))
                .andExpect(jsonPath("$.result.postId").value(fakeResult.getPostId()))
                .andExpect(jsonPath("$.result.title").value(fakeResult.getTitle()));
    }


}