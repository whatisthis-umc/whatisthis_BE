package umc.demoday.whatisthis.domain.recommendation;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface GeminiInterface {

    @PostExchange
    EmbeddingResponseDTO embedContent(@RequestBody EmbeddingRequestDTO request);
}
