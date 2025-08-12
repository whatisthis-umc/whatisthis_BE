package umc.demoday.whatisthis.domain.admin.redis.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {
    @Bean(name = "searchIndexerTaskExecutor")
    public Executor searchIndexerTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);   // 기본 스레드 수
        executor.setMaxPoolSize(10);   // 최대 스레드 수
        executor.setQueueCapacity(50); // 대기 큐 크기
        executor.setThreadNamePrefix("SearchIndexer-");
        executor.initialize();
        return executor;
    }
}
