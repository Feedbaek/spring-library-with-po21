package com.tmax.proobject.minskim2.util;

/**
 * 분산락에서 사용할 키를 생성하는 유틸 클래스
 *
 * @since 2025.04.25
 * @version 1.0
 * @author 김민석G (minskim2)
 */
public class DistributedLockKeyGenerator implements KeyGenerator {

    private static final String LOCK_PREFIX = "DLOCK:";

    @Override
    public String generateKey(String prefix, String... params) {
        return LOCK_PREFIX + prefix + ":" + String.join(":", params);
    }
}
