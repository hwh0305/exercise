package org.hao.test;

import java.util.concurrent.TimeUnit;

import org.hao.test.Retry.Task;

public class RetryFramework {

    public static Retry getInstance() {
        return new RetryImpl();
    }

    public static void main(String[] args) {
        Retry retry = RetryFramework.getInstance();
        retry.process(10, new Task() {

            @Override
            public boolean execute(int times, Object... params) {
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
        });

    }
}
