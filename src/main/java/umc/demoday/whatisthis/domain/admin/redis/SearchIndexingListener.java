package umc.demoday.whatisthis.domain.admin.redis;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchIndexingListener {

    private final PostRepository postRepository; // JPA Repository
    private final PostSearchRepository postSearchRepository; // Redis OM Repository
    private final PostMapper postMapper;

    @Async("searchIndexerTaskExecutor") // 이 메소드를 별도의 스레드에서 비동기적으로 실행
    @TransactionalEventListener // DB 트랜잭션이 '커밋'된 후에만 이벤트를 수신
    public void handlePostEvent(PostEvent event) {
        log.info("게시글 이벤트 수신: postId={}, action={}", event.postId(), event.action());

        try {
            if (event.action() == PostEvent.ActionType.DELETED) {
                postSearchRepository.deleteById(event.postId().toString());
            } else { // CREATED_OR_UPDATED
                // 이벤트 발행 시점과 처리 시점 사이에 데이터가 변경될 수 있으므로,
                // 항상 DB에서 최신 데이터를 다시 조회합니다.
                Post post = postRepository.findById(event.postId())
                        .orElse(null);

                if (post != null) {
                    PostDocument document = postMapper.toDocument(post);
                    postSearchRepository.save(document);
                }
            }
        } catch (Exception e) {
            log.error("검색 인덱싱 실패: postId={}", event.postId(), e);
            // 여기에 실패 시 재시도 또는 알림 로직을 추가할 수 있습니다.
        }
    }
}