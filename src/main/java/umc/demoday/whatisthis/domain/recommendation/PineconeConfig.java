package umc.demoday.whatisthis.domain.recommendation;

import io.pinecone.clients.Index;
import io.pinecone.clients.Pinecone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PineconeConfig {

    // Pinecone 클라이언트를 Spring Bean으로 등록
    @Bean
    public Pinecone pinecone() {
        return new Pinecone.Builder(System.getenv("PINECONE_API_KEY")).build();
    }

    // Index 연결 객체를 Spring Bean으로 등록
    @Bean
    public Index pineconeIndex(Pinecone pinecone) {
        // 위에서 생성된 Pinecone Bean을 받아 Index 연결을 생성합니다.
        return pinecone.getIndexConnection("whatisthis");
    }
}