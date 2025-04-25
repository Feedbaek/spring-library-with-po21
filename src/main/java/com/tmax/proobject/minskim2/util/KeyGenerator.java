package com.tmax.proobject.minskim2.util;

/**
 * 키를 생성하는 인터페이스
 *
 * @since 2025.04.25
 * @version 1.0
 * @author 김민석G (minskim2)
 */
public interface KeyGenerator {

    String generateKey(String prefix, String... params);
}
