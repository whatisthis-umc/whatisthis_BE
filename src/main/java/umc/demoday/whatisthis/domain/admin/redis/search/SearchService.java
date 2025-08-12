package umc.demoday.whatisthis.domain.admin.redis.search;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.search.Query;
import redis.clients.jedis.search.SearchResult;
import umc.demoday.whatisthis.domain.admin.redis.PostDocument;
import umc.demoday.whatisthis.domain.admin.redis.PostMapper;
import umc.demoday.whatisthis.domain.admin.redis.PostSearchRepository;
import umc.demoday.whatisthis.domain.admin.redis.SearchPostResponseDTO;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.converter.CategoryConverter;
import umc.demoday.whatisthis.domain.post.enums.Category;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String POPULAR_SEARCH_KEY = "popular_searches:";//인기 키워드
    private final PostSearchRepository postSearchRepository;
    // 검색어가 입력될 때마다 호출되는 메소드
    public Page<PostDocument> executeSearch(String keyword, Category category, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(size, page, Sort.Direction.DESC, "likeCount");

        List<String> categories = CategoryConverter.getDynamicCategories(category.toString());
        // 실제 검색 로직 수행
        Page<PostDocument> posts = searchPosts(keyword, categories, pageable);

        // 검색어 카운트 증가
        if (keyword != null && !keyword.isBlank()) {
            redisTemplate.opsForZSet().incrementScore(POPULAR_SEARCH_KEY, keyword.trim(), 1);
        }
        return posts;
    }
    // 검색
    public Page<PostDocument> searchPosts(String title, List<String> categories, Pageable pageable) {

        if (!categories.isEmpty() && StringUtils.isNotBlank(title)) {
            return postSearchRepository.findByCategoryInAndTitleContaining(categories, title, pageable);
        } else if (!categories.isEmpty()) {
            return postSearchRepository.findByCategoryIn(categories, pageable);
        } else if (StringUtils.isNotBlank(title)) {
            return postSearchRepository.findByTitleContaining(title, pageable);
        } else {
            return Page.empty(); // 나중에 AI 유사도 검색으로 변형.
        }
    }
    // 상위 N개의 인기 검색어 조회
    public List<String> getTodayPopularKeywords(Integer topN) {
        Set<String> keywords = redisTemplate.opsForZSet().reverseRange(POPULAR_SEARCH_KEY + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE), 0, topN - 1);
        return keywords != null ? new ArrayList<>(keywords) : new ArrayList<>();
    }


}