package umc.demoday.whatisthis.domain.pinecone;


import io.pinecone.clients.Index;
import io.pinecone.clients.Pinecone;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.openapitools.db_control.client.model.CreateIndexForModelRequestEmbed;
import org.openapitools.inference.client.model.EmbedRequest;
import org.openapitools.inference.client.model.Embedding;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmbeddingService {


    private String embeddingModel = "llama-text-embed-v2";
    String apiKey = System.getenv("PINECONE_API_KEY");
    Pinecone pinecone = new Pinecone.Builder(apiKey).build();

    public List<Float> createEmbedding(String text) {
        // 1. Pinecone의 Embed API를 호출하기 위한 요청 객체 생성
            pinecone.
        // 2. Pinecone 클라이언트를 통해 임베딩 생성 API 호출

        // 3. 결과에서 임베딩 벡터(List<Float>)를 추출하여 반환
        // 텍스트를 하나만 보냈으므로 첫 번째 임베딩 결과를 가져옵니다.
        if (response.getEmbeddingsCount() > 0) {
            return response.getEmbeddings(0).getValuesList();
        } else {
            return Collections.emptyList();
        }
    }
}