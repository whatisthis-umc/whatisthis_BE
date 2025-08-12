package umc.demoday.whatisthis.domain.admin.redis;

import com.redis.om.spring.repository.RedisDocumentRepository;

public interface PostSearchRepository extends RedisDocumentRepository<PostDocument, String> {
}
