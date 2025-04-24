package com.tmax.proobject.minskim2.config;

import com.tmax.proobject.minskim2.cache.DualCacheManager;
import com.tmax.proobject.minskim2.properties.CacheProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfiguration {

    @Bean
    @ConditionalOnMissingBean(DualCacheManager.class)
    @ConditionalOnBean(name = {"localManager", "globalManager"})
    public DualCacheManager dualCacheManager(@Qualifier("localManager") CacheManager localManager,
                                             @Qualifier("globalManager") CacheManager globalManager) {
        return new DualCacheManager(localManager, globalManager);
    }
}
