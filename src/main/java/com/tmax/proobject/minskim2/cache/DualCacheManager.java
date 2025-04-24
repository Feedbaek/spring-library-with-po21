package com.tmax.proobject.minskim2.cache;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 로컬 캐시와 글로벌 캐시를 사용할 수 있는 캐시 매니저
 * 로컬 캐시의 TTL 설정을 짧게 설정하는 것을 추천한다. 글로벌 캐시를 로컬 캐시에 즉각 반영하지 않기 때문이다.
 * 첫 조회 또는 로컬 캐시 만료 시 글로벌 캐시로 업데이트가 수행된다.
 * */
@RequiredArgsConstructor
public class DualCacheManager implements CacheManager {

    private final LocalCacheManager localManager;
    private final GlobalCacheManager globalManager;

    @Override
    public Cache getCache(@NonNull String name) {
        Cache localCache = localManager.getCache(name);
        Cache globalCache = globalManager.getCache(name);

        if (localCache == null && globalCache == null) {
            return null;
        }

        return DualCache.builder()
                .name(name)
                .localCache(localCache)
                .globalCache(globalCache)
                .build();
    }

    @NonNull
    @Override
    public Collection<String> getCacheNames() {
        Set<String> names = new LinkedHashSet<>(localManager.getCacheNames());
        names.addAll(globalManager.getCacheNames());
        return names;
    }
}
