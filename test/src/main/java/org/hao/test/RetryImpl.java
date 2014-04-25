package org.hao.test;

public class RetryImpl<T> implements Retry<T> {

    @Override
    public T process(int times, Task<T> task, Object... params) {
        T result = null;
        for (int i = 1; i < times + 1; ++i) {
            result = task.execute(i, params);
            if (task.isStop()) break;
        }
        return result;
    }

}
