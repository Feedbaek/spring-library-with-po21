package com.tmax.proobject.minskim2.config;

import com.tmax.proobject.minskim2.cache.DualCacheManager;
import com.tmax.proobject.minskim2.cache.GlobalCacheManager;
import com.tmax.proobject.minskim2.cache.LocalCacheManager;
import com.tmax.proobject.minskim2.properties.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfiguration {

    @Bean
    @ConditionalOnBean({LocalCacheManager.class, GlobalCacheManager.class})
    public DualCacheManager dualCacheManager(LocalCacheManager localManager, GlobalCacheManager globalManager) {
        return new DualCacheManager(localManager, globalManager);
    }
}
