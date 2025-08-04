package umc.demoday.whatisthis.domain.recommendation;


import io.pinecone.clients.Pinecone;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostSyncService {

    private static final String SYNC_ID = "POST_VECTOR_SYNC";
    private final PostRepository postRepository;
    private final SyncStatusRepository syncStatusRepository;
    private final RecommendationService recommendationService;
    private final Pinecone pineconeClient;

    @Scheduled(cron = "0 */10 * * * *")
    @Transactional
    public void syncPostsToVectorDB() {
        // 1. 마지막으로 동기화된 시점 조회
        LocalDateTime lastProcessedTime = syncStatusRepository.findBySyncId(SYNC_ID)
                .map(SyncStatus::getLastProcessedAt)
                .orElse(LocalDateTime.of(1970, 1, 1, 0, 0)); // 최초 실행 시 아주 오래된 시간으로 설정

        System.out.println("벡터 DB 동기화 시작. 기준 시간: " + lastProcessedTime);

        // 2. 기준 시간 이후에 업데이트된 게시물 조회 (100개씩)
        Page<Post> postsToProcessPage = postRepository.findByUpdatedAtAfterOrderByUpdatedAtAsc(lastProcessedTime, PageRequest.of(0, 100));

        if (postsToProcessPage.isEmpty()) {
            System.out.println("새롭게 동기화할 게시물이 없습니다.");
            return;
        }

        List<Post> postsToProcess = postsToProcessPage.getContent();
        for (Post post : postsToProcess) {
            try {
                recommendationService.savePostToVectorDB(post);
            } catch (Exception e) {
                System.err.println("Post ID " + post.getId() + " 처리 중 에러 발생: " + e.getMessage());
            }
        }

        // 3. 마지막으로 처리된 게시물의 updatedAt을 다음 기준 시간으로 저장
        Post lastProcessedPost = postsToProcess.get(postsToProcess.size() - 1);
        SyncStatus newStatus = new SyncStatus(SYNC_ID, lastProcessedPost.getUpdatedAt());
        syncStatusRepository.save(newStatus);

        System.out.println(postsToProcess.size() + "개의 게시물을 동기화 완료. 다음 기준 시간: " + newStatus.getLastProcessedAt());
    }
}