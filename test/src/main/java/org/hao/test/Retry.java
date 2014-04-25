package org.hao.test;

public interface Retry<T> {

    T process(int counts, Task<T> task, Object... params);

    interface Task<E> {

        E execute(int times, Object... params);

        boolean isStop();

    }

}
