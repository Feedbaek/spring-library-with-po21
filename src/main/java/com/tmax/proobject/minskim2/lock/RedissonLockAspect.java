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
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

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
    private final SpelExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    private final RedissonClient redissonClient;

    /**
     * RedissonClient 를 사용하여 분산락을 적용하는 메소드
     * 논블로킹 방식으로 락을 확인하고, 획득하지 못하는 경우 예외를 발생시킵니다.
     *
     * @param joinPoint ProceedingJoinPoint
     * @return Object
     * @throws Throwable 예외 발생 시
     */
    @Around("@annotation(com.tmax.proobject.minskim2.annotate.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);
        if (distributedLock == null) {
            return joinPoint.proceed();
        }

        // SpEL 평가
        EvaluationContext context = new StandardEvaluationContext();
        String[] paramNames = nameDiscoverer.getParameterNames(method);
        Object[] args = joinPoint.getArgs();
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }
        String lockKey = parser.parseExpression(distributedLock.key()).getValue(context, String.class);
        if (lockKey == null || lockKey.isBlank()) {
            throw new DistributedLockException.InvalidKey();
        }
        String key = keyGenerator.generateKey("REDISSON", lockKey);
        RLock rLock = redissonClient.getLock(key);
        boolean available = false;
        try {
            try {
                available = rLock.tryLock(
                        distributedLock.waitTime(), // 락을 얻기 위해 대기하는 시간
                        distributedLock.leaseTime(), // 락을 소유할 수 있는 시간
                        distributedLock.timeUnit());
            } catch (InterruptedException e) { // 락을 얻으려고 시도하다가 인터럽트를 받았을 때 발생
                throw new DistributedLockException.Interrupted();
            }

            if (!available) {
                throw new DistributedLockException.NotAvailable();
            }
            log.debug("락 획득 성공: {}", key);
            return joinPoint.proceed();

        } finally {
            if (available) {
                rLock.unlock();
            }
        }
    }
}
