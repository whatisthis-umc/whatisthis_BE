package umc.demoday.whatisthis.domain.recommendation;


import lombok.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class GeminiConfig {

    String baseUrl = System.getenv("BASE_URL");
    String apiKey = System.getenv("GEMINI_API_KEY");

    @Bean
    public RestClient geminiRestClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("x-goog-api-key", apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Bean
    public GeminiInterface geminiInterface(@Qualifier("geminiRestClient") RestClient client) {
        RestClientAdapter adapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(GeminiInterface.class);
    }

}

//    curl "https://generativelanguage.googleapis.com/v1beta/models/gemini-embedding-001:embedContent" \
//            -H "x-goog-api-key: $GEMINI_API_KEY" \
//            -H 'Content-Type: application/json' \
//            -d '{"model": "models/gemini-embedding-001",
//            "content": {"parts":[{"text": "What is the meaning of life?"}]}
//}'
//}