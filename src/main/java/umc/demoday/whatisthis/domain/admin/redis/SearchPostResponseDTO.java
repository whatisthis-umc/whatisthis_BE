package umc.demoday.whatisthis.domain.admin.redis;

import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.Searchable;
import com.redis.om.spring.annotations.TagIndexed;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import umc.demoday.whatisthis.domain.post.enums.Category;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Builder
public class SearchPostResponseDTO {
    private Integer id;
    private String title;
    private String content;
    private String authorName;
    private Category category;
    private Integer viewCount;
    private Integer likeCount;
    private Set<String> hashtags;
    private List<String> images;
    private LocalDateTime createdAt;
}
