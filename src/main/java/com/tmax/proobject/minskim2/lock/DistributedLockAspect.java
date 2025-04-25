package com.tmax.proobject.minskim2.lock;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 분산락을 적용하는 Aspect 인터페이스
 *
 * @since 2025.04.25
 * @version 1.0
 * @author 김민석G (minskim2)
 */
public interface DistributedLockAspect {

    Object lock(final ProceedingJoinPoint joinPoint) throws Throwable;
}
