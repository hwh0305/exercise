package org.hao.test;

public class RetryImpl implements Retry {

    @Override
    public boolean process(int times, Task task, Object... params) {
        boolean result = false;
        for (int i = 1; i < times + 1; ++i) {
            result = task.execute(i, params);
            if (result) break;
        }
        return result;
    }

    public static void main(String[] args) {
        String name = "Express DHL";
        String shortName = "DHL";

        System.out.println("Express ".length());

        System.out.println(name.substring(8));

    }
}
