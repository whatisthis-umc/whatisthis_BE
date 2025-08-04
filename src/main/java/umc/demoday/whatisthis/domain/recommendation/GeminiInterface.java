package umc.demoday.whatisthis.domain.recommendation;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/models/")
public interface GeminiInterface {

    @PostExchange("{model}:embedContent")
    EmbeddingResponseDTO embedContent(
            @PathVariable String model,
            @RequestBody EmbeddingRequestDTO request
    );
}
