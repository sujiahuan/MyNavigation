package org.jiahuan.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class ExecutorConfig {

    @Bean("taskExecutor")
    public Executor taskExecutro(){
//        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//        taskExecutor.setCorePoolSize(1);
//        taskExecutor.setMaxPoolSize(3);
//        taskExecutor.setQueueCapacity(900000000);
//        taskExecutor.setKeepAliveSeconds(60);
//        taskExecutor.setThreadNamePrefix("taskExecutor--");
//        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
//        taskExecutor.setAwaitTerminationSeconds(60);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        return executorService;
    }
}
