package com.capstone.ads.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("EventListener-");
        executor.initialize();
        return executor;
    }

    @Bean // Đặt tên bean mặc định (có thể không cần tên nếu là duy nhất)
    public DelegatingSecurityContextAsyncTaskExecutor delegatingSecurityContextAsyncTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
        return new DelegatingSecurityContextAsyncTaskExecutor(taskExecutor);
    }
}
