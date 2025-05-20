package cache.core;

import com.tmax.proobject.minskim2.cache.core.LocalLock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class LocalLockTest {

    private static final LocalLock localLock = new LocalLock();

    public String hello(String name) throws InterruptedException {
        Thread.sleep(1000);
        return "Hello, " + name + "!";
    }

    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("LocalLock 락 테스트 - 멀티 스레드 환경에서 락 획득 시도가 정상적인지 확인")
    public void testLocalLock() {
        // 락을 선점하고 스레드가 hello 메서드를 정상 호출. 출력 값은 "Hello, minskim2!" 가 나와야 한다.
        new Thread(() -> {
            try {
                String result = (String) localLock.invoke(this.getClass().getDeclaredMethod("hello", String.class), this, "minskim2");
                System.out.println(result);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        // 락을 획득하지 못해서 hello 메서드를 호출하지 못하는 스레드. 출력 값은 "null"이 나와야 한다.
        new Thread(() -> {
            try {
                String param = "minskim3";
                String result = localLock.call(() -> this.hello(param));
                System.out.println(result);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        // 락을 획득하지 못해서 람다를 호출하지 못하는 스레드. 출력 값은 "null"이 나와야 한다.
        new Thread(() -> {
            String param = "minskim4";
            AtomicBoolean isRun = new AtomicBoolean(false);
            try {
                localLock.run(() -> isRun.set(true));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (isRun.get()) {
                System.out.println("Hello, " + param + "!");
            } else {
                System.out.println("null");
            }
        }).start();

        sleep(1000);
    }
}
