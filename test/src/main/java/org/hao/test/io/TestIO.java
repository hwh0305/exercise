package org.hao.test.io;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class TestIO {

    public static void main(String[] args) throws Exception {
        File file = new File("/Users/hao/testio");
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        MappedByteBuffer bb = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, 1024 * 1024);
        // bb.putLong(1234451);
        // bb.put((byte) -1);
        // bb.force();
        // System.out.println("position: " + bb.position() + ", limit: " + bb.limit() + ", capactiy: " +
        // bb.capacity());
        // while (true) {
        // bb.mark();
        // byte b = bb.get();
        // System.out.println(b);
        // if (b == 4) {
        // bb.reset();
        // break;
        // }
        // }
        // System.out.println(bb.hasRemaining());
        // System.out.println(bb.position());
        raf.close();

    }
}
