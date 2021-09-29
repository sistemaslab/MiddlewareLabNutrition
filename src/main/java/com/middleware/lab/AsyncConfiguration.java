
package com.middleware.lab;



import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
 /**
 *
 * @author Natalia Manrique
 */
import java.util.concurrent.Executor;
 
@EnableAsync
@EnableScheduling
@Configuration
public class AsyncConfiguration extends AsyncConfigurerSupport {
 
    public AsyncConfiguration(){
        //For Spring
    }
 
    @Override
    public Executor getAsyncExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(15);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Middleware-");
        executor.initialize();
        return executor;
    }
}
