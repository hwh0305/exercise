package org.hao.test;

public interface Retry {

    boolean process(int counts, Task task, Object... params);

    interface Task {

        boolean execute(int times, Object... params);

    }

}
