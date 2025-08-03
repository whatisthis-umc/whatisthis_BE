package umc.demoday.whatisthis.domain.pinecone;


import io.pinecone.clients.Index;
import io.pinecone.clients.Pinecone;
import io.pinecone.proto.QueryRequest;
import io.pinecone.proto.QueryResponse;
import io.pinecone.proto.UpsertRequest;
import io.pinecone.proto.Vector;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.openapitools.db_data.client.model.Hit;
import org.openapitools.db_data.client.model.SearchRecordsResponse;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import umc.demoday.whatisthis.domain.post.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final EmbeddingService embeddingService;

    private String indexName = "whatisthis";
    String apiKey = System.getenv("PINECONE_API_KEY");
    Pinecone pinecone = new Pinecone.Builder(apiKey).build();
    Index index = pinecone.getIndexConnection(indexName);

    // 게시물 추가 및 임베딩 저장
    public void addPost(Post post, String text) throws Exception {
        // 백터로 변경
        List<Float> embedding = embeddingService.createEmbedding(text);
        // Pinecone Index에 Upsert (Update + Insert)
        index.upsert(post.getId().toString(), embedding);
    }

    // 특정 게시물과 유사한 게시물 추천받기
    public List<String> getRecommendations(Post post, int topK) throws Exception {
        // 기준이 되는 게시물 기준으로 유사도 검색
        List<String> fields = new ArrayList<>();
        fields.add("category");
        fields.add("chunk_text");
        SearchRecordsResponse response = index.searchRecordsByText(post.getTitle(), indexName, fields, topK, null, null);
        // 결과에서 ID만 추출 (자기 자신 제외)
        return response.getResult().getHits().stream()
                .map(Hit::getId)
                .limit(topK)
                .collect(Collectors.toList());
    }
}