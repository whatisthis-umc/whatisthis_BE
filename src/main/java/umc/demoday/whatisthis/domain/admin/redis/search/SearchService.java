package umc.demoday.whatisthis.domain.admin.redis.search;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.search.Query;
import redis.clients.jedis.search.SearchResult;
import umc.demoday.whatisthis.domain.admin.redis.PostDocument;
import umc.demoday.whatisthis.domain.admin.redis.PostMapper;
import umc.demoday.whatisthis.domain.admin.redis.PostSearchRepository;
import umc.demoday.whatisthis.domain.admin.redis.RedisIndexInitializer;
import umc.demoday.whatisthis.domain.admin.redis.dto.RedisResponseDTO;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.global.config.RedisConfig;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String POPULAR_SEARCH_KEY = "popular_searches:";//인기 키워드
    private final JedisPooled jedis;
    private final PostMapper postMapper;
    private final PostSearchRepository postSearchRepository;
    // 검색어가 입력될 때마다 호출되는 메소드
    public Page<PostDocument> executeSearch(String keyword, Category category, Pageable pageable) {

        List<Category> categories = List.of(category);
        categories.add(0, category);
        // 실제 검색 로직 수행
        Page<PostDocument> postDocuments = searchPosts(keyword, categories, pageable);
        // 검색어 카운트 증가
        if (keyword != null && !keyword.isBlank()) {
            redisTemplate.opsForZSet().incrementScore(POPULAR_SEARCH_KEY, keyword.trim(), 1);
        }
        return postDocuments;
    }
    public Page<PostDocument> searchPosts(String searchText, List<String> categories, Pageable pageable) {
        StringBuilder queryBuilder = new StringBuilder();

        // searchText 쿼리 (기존과 동일)
        if (StringUtils.isNotBlank(searchText)) {
            queryBuilder.append("(@title|content:(").append(searchText).append("))");
        }

        // ⭐ categories 리스트가 비어있지 않다면 TAG 필터링 쿼리 생성
        if (categories != null && !categories.isEmpty()) {
            // List<String>을 "|" 문자로 join하여 OR 조건 생성
            String categoryQuery = String.join("|", categories);
            // @category:{tech|dev|life} 와 같은 형식의 쿼리가 생성됨
            queryBuilder.append(" @category:{").append(categoryQuery).append("}");
        }

        String finalQuery = !queryBuilder.isEmpty() ? queryBuilder.toString().trim() : "*";

        Query query = new Query(finalQuery)
                .setSortBy("createdAt", false)
                .limit((int) pageable.getOffset(),pageable.getPageSize());

        // 검색 실행 및 결과 파싱 (기존과 동일)
        SearchResult result = jedis.ftSearch(RedisIndexInitializer.INDEX_NAME, query);

        List<PostDocument> postList = result.getDocuments().stream()
                .map(doc -> postSearchRepository.findById(doc.getId()).orElseThrow())
                .collect(Collectors.toList());

        return new PageImpl<>(postList, pageable, result.getTotalResults());
    }
    // 상위 N개의 인기 검색어 조회
    public List<String> getTodayPopularKeywords(Integer topN) {
        Set<String> keywords = redisTemplate.opsForZSet().reverseRange(POPULAR_SEARCH_KEY + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE), 0, topN - 1);
        return keywords != null ? new ArrayList<>(keywords) : new ArrayList<>();
    }
}