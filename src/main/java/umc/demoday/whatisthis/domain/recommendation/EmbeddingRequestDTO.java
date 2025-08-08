package umc.demoday.whatisthis.domain.recommendation;

import java.util.List;

public record EmbeddingRequestDTO(
        String model,
        Content content
) {
    public record Content(List<Part> parts) {}
    public record Part(String text) {}
}

