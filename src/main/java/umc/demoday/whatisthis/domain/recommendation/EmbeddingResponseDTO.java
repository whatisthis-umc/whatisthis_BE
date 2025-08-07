package umc.demoday.whatisthis.domain.recommendation;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EmbeddingResponseDTO(
        Embedding embedding,      // 차단되면 이 필드는 null 입니다.
        PromptFeedback promptFeedback  // 차단되면 여기에 정보가 담깁니다.
) {
    public record Embedding(List<Float> values) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PromptFeedback(String blockReason) {}
}