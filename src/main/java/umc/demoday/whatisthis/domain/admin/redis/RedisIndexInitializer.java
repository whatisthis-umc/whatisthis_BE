package umc.demoday.whatisthis.domain.admin.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.search.IndexDefinition;
import redis.clients.jedis.search.Schema;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisIndexInitializer implements ApplicationRunner {

    private final JedisPooled jedis;
    public static final String INDEX_NAME = "post_index";

    @Override
    public void run(ApplicationArguments args) {
        try {
            // 1. 인덱스 정보 확인
            jedis.ftInfo(INDEX_NAME);
            log.info("RediSearch index '{}' already exists.", INDEX_NAME);
        } catch (JedisDataException e) {
            // 2. 인덱스가 존재하지 않으면 예외 발생 -> 생성 로직 실행
            if (e.getMessage().contains("Unknown Index name")) {
                log.info("RediSearch index '{}' does not exist. Creating new index...", INDEX_NAME);

                // 3. 스키마 정의
                Schema schema = new Schema()
                        .addTextField("title", 5.0)    // 제목 (가중치 5.0)
                        .addTextField("content", 1.0)  // 내용
                        .addTagField("category")           // 작성자 (필터링용)
                        .addNumericField("createdAt");   // 생성일 (정렬용)

                // 4. 인덱스 생성 규칙 정의
                IndexDefinition rule = new IndexDefinition(IndexDefinition.Type.HASH)
                        .setPrefixes("post:"); // "post:"로 시작하는 Hash를 인덱싱

                // 5. 인덱스 생성 명령어 실행
                jedis.ftCreate(INDEX_NAME, rule, schema);
                log.info("RediSearch index '{}' created successfully.", INDEX_NAME);
            }
        }
    }
}