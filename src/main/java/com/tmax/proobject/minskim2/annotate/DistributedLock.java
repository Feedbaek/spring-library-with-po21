package com.tmax.proobject.minskim2.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 분산락을 적용하기 위한 어노테이션.
 * 메소드에 이 어노테이션을 붙이면, 해당 메소드에 분산락을 적용합니다.
 *
 * @since 2025.04.25
 * @version 1.0
 * @author 김민석G (minskim2)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * 락의 이름
     */
    String key();

    /**
     * 락의 시간 단위
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 락 획득을 위해 기다리는 시간
     */
    long waitTime() default 5L;

    /**
     * 락 임대 시간
     */
    long leaseTime() default 3L;
}
