package com.tmax.proobject.minskim2.cache;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

/**
 * 로컬 캐시와 글로벌 캐시를 내부에 갖고있는 듀얼 캐시. DualCacheManager 가 사용하는 캐시 구현체.
 *
 * @since 2025.04.25
 * @version 1.0
 * @author 김민석G (minskim2)
 * */
@Slf4j
@Getter
public class DualCache implements Cache {

    private final String name;
    private final Cache localCache;
    private final Cache globalCache;

    @Builder
    DualCache(@NonNull String name, Cache localCache, Cache globalCache) {
        if (localCache == null && globalCache == null) {
            throw new RuntimeException("localCache or globalCache cannot be null");
        }
        this.name = name;
        this.localCache = localCache;
        this.globalCache = globalCache;
    }

    @NonNull
    @Override
    public String getName() {
        return this.name;
    }

    @NonNull
    @Override
    public Object getNativeCache() {
        return globalCache.getNativeCache();
    }

    /**
     * 로컬 캐시에서 먼저 조회를 하고, 로컬에 없는 경우 글로벌 캐시를 조회한다.
     * 글로벌 캐시를 조회한 경우 로컬 캐시에 업데이트 한다.
     * */
    @Override
    public ValueWrapper get(@NonNull Object key) {
        ValueWrapper localVal = localCache.get(key);
        if (localVal != null) {
            return localVal;
        }
        log.debug("Local cache miss for key: {}", key);
        ValueWrapper globalVal = globalCache.get(key);
        if (globalVal != null) {
            localCache.put(key, globalVal);
        }
        return globalVal;
    }

    
    @Override
    public <T> T get(@NonNull Object key, Class<T> type) {
        T localVal = localCache.get(key, type);
        if (localVal != null) {
            return localVal;
        }
        log.debug("Local cache miss for key: {}", key);
        T globalVal = globalCache.get(key, type);
        if (globalVal != null) {
            localCache.put(key, globalVal);
        }
        return globalVal;
    }

    @Override
    public <T> T get(@NonNull Object key, @NonNull Callable<T> valueLoader) {
        T localVal = localCache.get(key, valueLoader);
        if (localVal != null) {
            return localVal;
        }
        log.debug("Local cache miss for key: {}", key);
        T globalVal = globalCache.get(key, valueLoader);
        if (globalVal != null) {
            localCache.put(key, globalVal);
        }
        return globalVal;
    }

    @Override
    public void put(@NonNull Object key, Object value) {
        localCache.put(key, value);
        globalCache.put(key, value);
    }

    @Override
    public void evict(@NonNull Object key) {
        localCache.evict(key);
        globalCache.evict(key);
    }

    @Override
    public void clear() {
        localCache.clear();
        globalCache.clear();
    }
}
