package com.tmax.proobject.minskim2.config;

import com.tmax.proobject.minskim2.lock.RedissonLockAspect;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * 분산락 설정을 자동 구성하기 위한 클래스
 * RedissonClient 이 존재할 경우에만 RedissonLockAspect 를 빈으로 등록한다.
 *
 * @since 2025.04.25
 * @version 1.0
 * @author 김민석G (minskim2)
 */
@Slf4j
@AutoConfiguration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class DistributedLockAutoConfiguration {

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    public RedissonLockAspect distributedLockAspect(RedissonClient redissonClient) {
        log.info("Creating distributed lock aspect");
        return new RedissonLockAspect(redissonClient);
    }
}
