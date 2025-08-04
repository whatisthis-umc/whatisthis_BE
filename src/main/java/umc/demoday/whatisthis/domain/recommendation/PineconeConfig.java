package umc.demoday.whatisthis.domain.recommendation;

import io.pinecone.clients.Pinecone;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class PineconeConfig {

    String apiKey = System.getenv("PINECONE_API_KEY");

    @Bean
    public Pinecone pineconeClient() {
        return new Pinecone.Builder(apiKey).build();
    }
}
