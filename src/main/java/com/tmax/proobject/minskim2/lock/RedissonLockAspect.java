package com.tmax.proobject.minskim2.lock;

import com.tmax.proobject.minskim2.annotate.DistributedLock;
import com.tmax.proobject.minskim2.exception.DistributedLockException;
import com.tmax.proobject.minskim2.util.DistributedLockKeyGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Redisson 을 사용한 분산락을 적용하는 Aspect 구현체
 *
 * @since 2025.04.25
 * @version 1.0
 * @author 김민석G (minskim2)
 */
@Slf4j
@Aspect
@Order(1)
@RequiredArgsConstructor
public class RedissonLockAspect implements DistributedLockAspect {

    private final DistributedLockKeyGenerator keyGenerator = new DistributedLockKeyGenerator();

    private final RedissonClient redissonClient;

    @Around("@annotation(com.tmax.proobject.minskim2.annotate.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key = keyGenerator.generateKey("REDISSON",
                        Arrays.toString(signature.getParameterNames()) +
                        Arrays.toString(joinPoint.getArgs()) +
                        distributedLock.key());

        RLock rLock = redissonClient.getLock(key);
        try {
            boolean available = rLock.tryLock(
                            distributedLock.waitTime(),
                            distributedLock.leaseTime(),
                            distributedLock.timeUnit());
            if (!available) {
                throw new DistributedLockException.NotAvailable();
            }

            return joinPoint.proceed();

        } catch (InterruptedException e) { // 락을 얻으려고 시도하다가 인터럽트를 받았을 때 발생
            throw new DistributedLockException.Interrupted();
        } finally {
            try {
                rLock.unlock();
            } catch (IllegalMonitorStateException ex) { // 락이 이미 종료되었을때 발생
                log.warn("락이 이미 종료되었습니다.", ex);
            } catch (Exception ex) {
                // 락을 해제하는 도중에 예외가 발생했을 때
                log.error("락 해제 중 예외 발생", ex);
            }
        }
    }
}
