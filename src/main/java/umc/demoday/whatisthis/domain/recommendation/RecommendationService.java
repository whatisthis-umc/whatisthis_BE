package umc.demoday.whatisthis.domain.recommendation;


import io.pinecone.clients.Index;
import io.pinecone.clients.Pinecone;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.post.Post;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendationService {
    //log용
    private static final Logger log = LoggerFactory.getLogger(RecommendationService.class);

    private final GeminiInterface geminiInterface;
    private final Index index;

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

            index.upsert(post.getId().toString(), response.embedding().values());
            log.info("Post ID {} 벡터 DB 저장 성공", post.getId());
            } catch (Exception e) {
            log.error("Post ID {} 처리 중 예외 발생", post.getId(), e);
        }
    }

}