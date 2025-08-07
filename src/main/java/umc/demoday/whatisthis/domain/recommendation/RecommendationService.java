package umc.demoday.whatisthis.domain.recommendation;


import io.pinecone.clients.Index;
import io.pinecone.clients.Pinecone;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.openapitools.db_data.client.ApiException;
import org.openapitools.db_data.client.model.Hit;
import org.openapitools.db_data.client.model.SearchRecordsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.member_profile.MemberProfile;
import umc.demoday.whatisthis.domain.member_profile.MemberProfileRepository;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendationService {
    //log용
    private static final Logger log = LoggerFactory.getLogger(RecommendationService.class);

    private final GeminiInterface geminiInterface;
    private final Index index;
    private final MemberProfileRepository memberProfileRepository;
    private final PostRepository postRepository;

    public void savePostToVectorDB(Post post) {
        try{
            // 입력값 유효성 검사
            // 내용이 비어있거나, 공백만 있거나, 너무 짧으면 API를 호출하지 않고 건너뜁니다.
            if (post.getContent() == null || post.getContent().isBlank() || post.getContent().length() < 2) { // 최소 길이는 2~3글자 정도로 조절
                log.warn("Post ID {}의 내용이 비어있거나 너무 짧아서 임베딩을 건너뜁니다. 내용: '{}'", post.getId(), post.getContent());
                return;
            }
            // 요청 DTO 생성
            String modelName = "models/gemini-embedding-001";
            EmbeddingRequestDTO.Content content = new EmbeddingRequestDTO.Content(List.of(new EmbeddingRequestDTO.Part(post.getContent())));
            EmbeddingRequestDTO request = new EmbeddingRequestDTO(modelName, content);

            EmbeddingResponseDTO response = geminiInterface.embedContent(request);

            if (response == null) {
                log.error("Post ID {} 처리 중 Gemini로부터 null 응답을 받았습니다.", post.getId());
                return;
            }
            if (response.embedding() == null || response.embedding().values().isEmpty()) {
                // 피드백 정보가 있는지 확인하고 정확한 원인을 로그에 남깁니다.
                if (response.promptFeedback() != null) {
                    log.error("Post ID {}의 임베딩이 생성되지 않았습니다. 차단 이유: {}", post.getId(), response.promptFeedback().blockReason());
                } else {
                    log.error("Post ID {}의 임베딩이 생성되지 않았습니다. (알 수 없는 이유, 응답 본문 확인 필요)", post.getId());
                }
                return;
            }
            String namespace = post.getCategory().name().endsWith("_TIP") ? "LIFE_TIP" : "LIFE_ITEM" ;
            index.upsert(post.getId().toString(), response.embedding().values(),namespace);
            log.info("Post ID {} 벡터 DB 저장 성공", post.getId());
            } catch (Exception e) {
            log.error("Post ID {} 처리 중 예외 발생", post.getId(), e);
        }
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    public List<Integer> findRecommendationsForMember(Integer memberId, Integer topK, Category category) {
        return memberProfileRepository.findByMember_Id(memberId)
                .map(MemberProfile::getLastSeenPostId)
                // 2. 람다 표현식을 사용하여 postId와 topK를 모두 전달합니다.
                .map(postId -> findSimilarPostsInVectorDB(postId, topK, category))
                // 3. orElseGet에도 람다를 사용해 topK를 전달합니다.
                .orElseGet(() -> getDefaultRecommendations(topK, category));
    }
    private List<Integer> findSimilarPostsInVectorDB(Integer postId, Integer topK, Category category) {
        try {
            // 이 메소드 안에서 Pinecone 벡터 검색 로직을 수행합니다.
            //  벡터와 유사한 벡터들을 Pinecone에서 검색하여 Post 목록을 반환합니다.
            String namespace = category.toString();
            List<String> fields = new ArrayList<>();
            fields.add("category");
            fields.add("chunk_text");
            SearchRecordsResponse response = index.searchRecordsById(postId.toString(), namespace, fields, topK, null, null);
            List<Hit> hits = response.getResult().getHits();

            // 문자열 ID에서 숫자 부분만 추출하여 Long 타입의 ID 리스트를 생성합니다.
            List<Integer> postIds = hits.stream()
                    .map(hit -> Integer.parseInt(hit.getId()))
                    .toList();
            return postIds;
        }
        catch (ApiException e) {
            log.error("Post ID {} 기반 벡터 검색 중 API 에러 발생", postId, e);
            return Collections.emptyList();
        }
    }

    private List<Integer> getDefaultRecommendations(Integer topK, Category category) {
        try {
            List<String> fields = new ArrayList<>();
            fields.add("category");
            fields.add("chunk_text");
            String namespace = category.toString();
            SearchRecordsResponse response = index.searchRecordsByText("추천하는 생활팁", namespace, fields, topK, null, null);
            List<Hit> hits = response.getResult().getHits();

            // 문자열 ID에서 숫자 부분만 추출하여 Long 타입의 ID 리스트를 생성합니다.
            return hits.stream()
                    .map(hit -> Integer.parseInt(hit.getId()))
                    .toList();

        }
        catch (ApiException e) {
            log.error("Default 벡터 검색 중 API 에러 발생", e);
            return Collections.emptyList();
        }
    }
}