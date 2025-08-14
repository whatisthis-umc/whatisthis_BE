package umc.demoday.whatisthis.domain.admin.redis;

import com.redis.om.spring.annotations.Query;
import com.redis.om.spring.repository.RedisDocumentRepository;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import umc.demoday.whatisthis.domain.post.Post;

import java.util.List;

public interface PostSearchRepository extends RedisDocumentRepository<PostDocument, String> {

    // "Category가 리스트에 포함되고, Title에 특정 문자열이 포함된 게시물을 찾아줘"
    Page<PostDocument> findByCategoryInAndTitleContaining(List<String> categories, String title, Pageable pageable);

    // 카테고리만으로 검색
    Page<PostDocument> findByCategoryIn(List<String> categories, Pageable pageable);

    // 제목만으로 검색
    Page<PostDocument> findByTitleContaining(String title, Pageable pageable);
//
//    @Query("@category:{$categories} (@title:($keyword) | @content:($keyword))")
//    Page<PostDocument> searchByCategoriesAndTitleOrContent(@Param("keyword") String keyword, @Param("categories") List<String> categories, Pageable pageable);
//
}
