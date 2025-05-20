package com.tmax.proobject.minskim2.cache.core;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class LocalLock {

    private final ReentrantLock lock;
    private final long timeout;
    private final TimeUnit timeUnit;


    public LocalLock() {
        this.lock = new ReentrantLock();
        this.timeout = 60000L;
        this.timeUnit = TimeUnit.MILLISECONDS;
    }

    public LocalLock(long timeout, TimeUnit timeUnit) {
        this.lock = new ReentrantLock();
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }


    public Object invoke(Method method, Object obj, Object ... args) {
        boolean locked = lock.tryLock();
        if (!locked) {
            return null;
        }
        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public <T> T call(Callable<T> callable) {
        boolean locked = lock.tryLock();
        if (!locked) {
            return null;
        }
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void run(Runnable runnable) {
        boolean locked = lock.tryLock();
        if (!locked) {
            return;
        }
        try {
            runnable.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
