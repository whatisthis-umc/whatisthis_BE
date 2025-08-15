package umc.demoday.whatisthis.domain.recommendation;


import io.pinecone.clients.Index;
import io.pinecone.clients.Pinecone;
import io.pinecone.unsigned_indices_model.QueryResponseWithUnsignedIndices;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.openapitools.db_data.client.ApiException;
import org.openapitools.db_data.client.model.Hit;
import org.openapitools.db_data.client.model.SearchRecordsResponse;
import org.openapitools.db_data.client.model.SearchRecordsVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Role;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.member_profile.MemberProfile;
import umc.demoday.whatisthis.domain.member_profile.MemberProfileRepository;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;
import umc.demoday.whatisthis.global.CustomUserDetails;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

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

    private final String defaultSeedTipPostId = "205";
    private final String defaultSeedItemPostId = "216";
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
            EmbeddingResponseDTO response = textEmbedding(post.getContent());

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
            String namespace = getNamespaceForCategory(post.getCategory());
            index.upsert(post.getId().toString(), response.embedding().values(),namespace);
            log.info("Post ID {} 벡터 DB 저장 성공", post.getId());
            } catch (Exception e) {
            log.error("Post ID {} 처리 중 예외 발생", post.getId(), e);
        }
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    public List<Integer> findRecommendationsForMember(CustomUserDetails customUserDetails, Integer topK, Category category) {
        String seedPostId;
        String namespace = getNamespaceForCategory(category);
        // 1. 사용자의 역할을 먼저 확인합니다.
        if (customUserDetails == null || customUserDetails.getRole().equals("ROLE_ADMIN")) {
            // ADMIN 역할일 경우, 항상 기본 추천 ID를 사용합니다.
            seedPostId = namespace.equals(Category.LIFE_TIP.name()) ? defaultSeedTipPostId : defaultSeedItemPostId;
            log.info("기본 추천(Seed: {})을 제공합니다.", seedPostId);
        } else {
            // 그 외 사용자(USER 등)는 기존 로직을 따릅니다.
            // 마지막으로 본 게시글이 있으면 그것을, 없으면 기본 추천 ID를 사용합니다.
            seedPostId = memberProfileRepository.findByMember_Id(customUserDetails.getId())
                    .map(profile -> profile.getLastSeenPostId().toString())
                    .orElse(namespace.equals(Category.LIFE_TIP.name()) ? defaultSeedTipPostId : defaultSeedItemPostId);

            Post seedPost = postRepository.findById(Integer.parseInt(seedPostId)).orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND_404));
            if(!seedPost.getCategory().equals(category)) {
                seedPostId = seedPost.getCategory().equals(Category.LIFE_TIP) ? defaultSeedItemPostId : defaultSeedTipPostId;
            }
            log.info("Member(ID: {}) 추천 생성. 기준 Post ID: {}, 카테고리: {}", customUserDetails.getId(), seedPostId, category.name());
        }

        // 2. 결정된 seedPostId를 기반으로 유사한 게시물을 검색합니다.
        return findSimilarPostsInVectorDB(seedPostId, topK, category);
    }
    public List<Integer> findSimilarPostsInVectorDB(String seedPostId, Integer topK, Category category) {
        // 이 메소드 안에서 Pinecone 벡터 검색 로직을 수행합니다.
        //  벡터와 유사한 벡터들을 Pinecone에서 검색하여 Post 목록을 반환합니다.
        String namespace = getNamespaceForCategory(category);
        List<String> fields = new ArrayList<>();
        fields.add("category");
        fields.add("chunk_text");
        QueryResponseWithUnsignedIndices response = index.queryByVectorId(topK+1,seedPostId,namespace);
        log.info("관련 게시물 추천. 기준 Post ID: {}, 카테고리: {}", seedPostId, category.name());
        return response.getMatchesList().stream()
                .map(hit -> Integer.parseInt(hit.getId()))
                .toList();

    }

    private EmbeddingResponseDTO textEmbedding(String text)
    {
        String modelName = "models/gemini-embedding-001";
        EmbeddingRequestDTO.Content content = new EmbeddingRequestDTO.Content(List.of(new EmbeddingRequestDTO.Part(text)));
        EmbeddingRequestDTO request = new EmbeddingRequestDTO(modelName, content);
        return geminiInterface.embedContent(request);
    }

    private String getNamespaceForCategory(Category category) {
        if (category.name().endsWith("_TIP")) {
            return Category.LIFE_TIP.name();
        } else if (category.name().endsWith("_ITEM")) {
            return Category.LIFE_ITEM.name();
        }
        return "COMMUNITY";
    }
}