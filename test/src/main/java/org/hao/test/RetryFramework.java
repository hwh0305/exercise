package org.hao.test;

import java.util.concurrent.TimeUnit;

import org.hao.test.Retry.Task;

public class RetryFramework {

    public static <T> Retry<T> getInstance() {
        return new RetryImpl<T>();
    }

    public static void main(String[] args) {
        Retry<Boolean> retry = RetryFramework.getInstance();
        retry.process(10, new Task<Boolean>() {

            private final boolean isStop = false;

            @Override
            public Boolean execute(int times, Object... params) {
                System.out.println("run");
                if (times == 5) return true;
                sleep(times);
                return false;
            }

            private void sleep(int ms) {
                try {
                    TimeUnit.MILLISECONDS.sleep(ms);
                } catch (InterruptedException e) {
                }
            }

            @Override
            public boolean isStop() {
                return isStop;
            }
        });

    }
}
