package umc.demoday.whatisthis.domain.admin.redis.async;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.admin.redis.PostDocument;
import umc.demoday.whatisthis.domain.admin.redis.PostMapper;
import umc.demoday.whatisthis.domain.admin.redis.PostSearchRepository;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitialDataIndexer {

    //  한 번에 조회하고 처리할 데이터 개수 (
    private static final int BATCH_SIZE = 1000;

    private final PostRepository postRepository; // JPA Repository (DB 데이터 조회용)
    private final PostSearchRepository postSearchRepository; // Redis OM Repository (검색엔진 저장용)
    private final PostMapper postMapper; // Entity -> Document 변환기

    @Transactional(readOnly = true) // CUD 작업이 없으므로 읽기 전용으로 성능 최적화
    public void reindexAllPosts() {
        log.info("기존 게시물 전체 리인덱싱 작업을 시작합니다");

        // 1. 기존 검색 인덱스 데이터 전체 삭제 (깨끗한 상태에서 시작)
        log.info("기존 검색 인덱스를 초기화합니다.");
        postSearchRepository.deleteAll();

        Pageable pageable = PageRequest.of(0, BATCH_SIZE);
        Page<Post> postPage;
        int pageCount = 0;

        do {
            // 2. DB에서 배치 사이즈만큼 데이터 조회 (Paging)
            postPage = postRepository.findAll(pageable);
            List<Post> postsInBatch = postPage.getContent();

            if (postsInBatch.isEmpty()) {
                log.info("더 이상 색인할 데이터가 없습니다.");
                break;
            }

            log.info("[페이지 {}] 처리 중... ({}개 게시물 색인)", pageCount++, postsInBatch.size());

            // 3. Post Entity 리스트를 PostDocument 리스트로 변환
            List<PostDocument> documents = postsInBatch.stream()
                    .map(postMapper::toDocument)
                    .toList();

            // 4. Redis에 배치 단위로 저장 (saveAll로 네트워크 비용 절감)
            postSearchRepository.saveAll(documents);

            // 5. 다음 페이지 요청 정보로 갱신
            pageable = postPage.nextPageable();

        } while (postPage.hasNext());

        log.info("전체 리인덱싱 작업을 성공적으로 완료했습니다.");
    }
}