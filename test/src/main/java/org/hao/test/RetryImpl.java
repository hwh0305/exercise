package org.hao.test;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

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

    public static void main(String[] args) throws Exception {
        ByteBuffer bb = ByteBuffer.allocate(128);
        bb.putInt(2);
        bb.putInt(3);

        System.out.println("position: " + bb.position() + ", limit: " + bb.limit() + ", capactiy: " + bb.capacity());

        bb.flip();
        System.out.println("position: " + bb.position() + ", limit: " + bb.limit() + ", capactiy: " + bb.capacity());

        RandomAccessFile file = new RandomAccessFile("123", "rw");
        System.out.println(file.length());
        FileChannel fc = file.getChannel();
        file.seek(file.length());
        fc.write(bb);
        fc.close();
        file.close();

    }
}
