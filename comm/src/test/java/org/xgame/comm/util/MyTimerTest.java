package org.xgame.comm.util;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MyTimerTest {
    @Test
    public void test() {
        CountDownLatch cdL = new CountDownLatch(1);
        AtomicInteger counter = new AtomicInteger(3);

        MyTimer.getInstance().repeatUntilGetFalse(
            () -> {
                System.out.println("Test");
                return counter.decrementAndGet() > 1;
            },
            10, 10, TimeUnit.MILLISECONDS, (unused) -> {
                System.out.println("Finished");
                cdL.countDown();
                return null;
            }
        );

        try {
            cdL.await();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
