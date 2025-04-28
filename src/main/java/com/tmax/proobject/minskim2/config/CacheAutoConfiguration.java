package com.tmax.proobject.minskim2.config;

import com.tmax.proobject.minskim2.cache.DualCacheManager;
import com.tmax.proobject.minskim2.properties.CacheProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;

/**
 * 캐시 설정 자동 구성을 위한 클래스
 *
 * @since 2025.04.25
 * @version 1.0
 * @author 김민석G (minskim2)
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(CacheProperties.class)
public class CacheAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DualCacheManager.class)
    @ConditionalOnBean(name = {"localCacheManager", "globalCacheManager"})
    public DualCacheManager dualCacheManager(@Qualifier("localCacheManager") CacheManager localManager,
                                             @Qualifier("globalCacheManager") CacheManager globalManager) {
        log.info("Creating dual cache manager");
        return new DualCacheManager(localManager, globalManager);
    }
}
