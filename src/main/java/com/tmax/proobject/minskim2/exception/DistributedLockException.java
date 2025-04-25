package com.tmax.proobject.minskim2.exception;

/**
 * 분산락 예외 클래스
 *
 * @since 2025.04.25
 * @version 1.0
 * @author 김민석G (minskim2)
 */
public class DistributedLockException extends RuntimeException {
    private DistributedLockException() {
        super();
    }
    private DistributedLockException(String message) {
        super(message);
    }
    private DistributedLockException(String message, Throwable cause) {
        super(message, cause);
    }


    public static class NotAvailable extends DistributedLockException {
        public NotAvailable() {
            super("Distributed lock is not available.");
        }
        public NotAvailable(String message) {
            super(message);
        }
        public NotAvailable(String message, Throwable cause) {
            super(message, cause);
        }
    }
    public static class Interrupted extends DistributedLockException {
        public Interrupted() {
            super("Thread was interrupted while waiting for distributed lock.");
        }
        public Interrupted(String message) {
            super(message);
        }
        public Interrupted(String message, Throwable cause) {
            super(message, cause);
        }
    }
    public static class AlreadyUnlocked extends DistributedLockException {
        public AlreadyUnlocked() {
            super("Distributed lock is already unlocked.");
        }
        public AlreadyUnlocked(String message) {
            super(message);
        }
        public AlreadyUnlocked(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
