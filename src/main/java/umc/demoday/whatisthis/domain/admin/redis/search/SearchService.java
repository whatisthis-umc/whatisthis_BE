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

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "likeCount");

        List<String> categories = CategoryConverter.getDynamicCategories(category.toString());
        // 검색 로직 수행
        Page<PostDocument> posts = searchPosts(keyword, categories, pageable);

        // 검색어 카운트 증가
        if (keyword != null && !keyword.isBlank()) {
            redisTemplate.opsForZSet().incrementScore(POPULAR_SEARCH_KEY, keyword.trim(), 1);
        }

        return posts;
    }
    // 검색
    public Page<PostDocument> searchPosts(String text, List<String> categories, Pageable pageable) {

        if(!categories.isEmpty() && StringUtils.isNotBlank(text)) {
            return postSearchRepository.findByCategoryInAndTitleContaining(categories, text, pageable); //카테고리와 제목  으로
        } else if (!categories.isEmpty()) {
            return postSearchRepository.findByCategoryIn(categories, pageable); // 카테고리로
        } else if (StringUtils.isNotBlank(text)) {
            return postSearchRepository.findByTitleContaining(text, pageable); // 제목으로
        } else {
            return postSearchRepository.findAll(pageable);
        }
    }
    // 상위 N개의 인기 검색어 조회
    public List<String> getPopularKeywords(Integer topN) {
        Set<String> keywords = redisTemplate.opsForZSet().reverseRange(POPULAR_SEARCH_KEY, 0, topN - 1);
        return keywords != null ? new ArrayList<>(keywords) : new ArrayList<>();
    }


}