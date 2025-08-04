package umc.demoday.whatisthis.domain.recommendation;


import io.pinecone.clients.Index;
import io.pinecone.clients.Pinecone;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.post.Post;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendationService {

    private final GeminiInterface geminiInterface;
    private final Pinecone pineconeClient;

    Index index = pineconeClient.getIndexConnection("whatisthis");

    public void savePostToVectorDB(Post post) {
        //게시글 내용으로
        EmbeddingRequestDTO request = new EmbeddingRequestDTO(post.getContent());
        EmbeddingResponseDTO response = geminiInterface.embedContent("gemini-embedding-001", request);
        index.upsert(post.getId().toString(), response.getEmbeddings());
    }

}