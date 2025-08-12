package umc.demoday.whatisthis.domain.admin.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.search.FTCreateParams;
import redis.clients.jedis.search.IndexDataType;
import redis.clients.jedis.search.IndexDefinition;
import redis.clients.jedis.search.Schema;
import redis.clients.jedis.search.schemafields.NumericField;
import redis.clients.jedis.search.schemafields.SchemaField;
import redis.clients.jedis.search.schemafields.TagField;
import redis.clients.jedis.search.schemafields.TextField;

import java.util.Arrays;
import java.util.List;

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
                List<SchemaField> schemaFields = Arrays.asList(
                        new TextField("title").weight(5.0),
                        new TextField("content").weight(1.0),
                        new TagField("category"),
                        new NumericField("createdAt")
                );
                // 4. 인덱스 생성 규칙 정의
                FTCreateParams createParams = new FTCreateParams()
                        .on(IndexDataType.HASH) // new IndexDefinition(Type.HASH)와 동일
                        .addPrefix("post:");    // .setPrefixes("post:")와 동일

                // 5. 인덱스 생성 명령어 실행
                jedis.ftCreate(INDEX_NAME, createParams, schemaFields);

                log.info("RediSearch index '{}' created successfully.", INDEX_NAME);
            }
        }
    }
}