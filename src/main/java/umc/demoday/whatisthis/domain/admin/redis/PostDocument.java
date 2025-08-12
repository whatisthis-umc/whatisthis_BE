package umc.demoday.whatisthis.domain.admin.redis;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.Searchable;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post_image.PostImage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Document("post") // Redis에 저장될 때 "post:" 라는 prefix로 저장됩니다.
public class PostDocument {

    @Id
    private String id; // Post Entity의 Integer id를 String으로 변환하여 사용

    @Indexed
    private Integer postId;

    // '풀텍스트 검색' 대상 필드 (내용 기반 검색)
    @Searchable(nostem = true) // 한국어 검색을 위해 stemming 비활성화
    private String title;

    @Searchable(nostem = true)
    private String content;

    // '필터링' 및 '정확한 값 매칭' 대상 필드
    @Indexed
    private String authorName; // 비정규화: Member 객체 대신 이름만 저장

    // '태그' 기반 필터링 (다중 값 허용, 정확한 값 매칭)
    @Indexed
    private Set<String> hashtags; // 비정규화: Hashtag 객체 리스트 대신 키워드 Set으로 저장

    @Indexed
    private Category category; // Enum은 String으로 저장되어 필터링에 용이

    // '정렬' 및 '범위 검색' 대상 필드
    @Indexed(sortable = true)
    private Integer viewCount;

    @Indexed(sortable = true)
    private Integer likeCount;

    private List<String> images;

    @Indexed(sortable = true)
    private LocalDateTime createdAt;
}