package org.hao.test.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class EchoServer {

    private static final Logger log         = LoggerFactory.getLogger(EchoServer.class);

    private Selector            selector;

    private final static int    KB          = 1024;

    private final static int    FOUR_KB     = 4 * KB;

    private final static int    EOF         = -1;

    private final static int    ENTER       = 10;

    private final static int    SERVER_PORT = 1000;

    public EchoServer(){
        try {
            ServerSocketChannel listenChannel = ServerSocketChannel.open();
            selector = Selector.open();
            listenChannel.configureBlocking(false);
            listenChannel.socket().bind(new InetSocketAddress(SERVER_PORT));
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() throws IOException {
        while (true) {
            if (selector.select(TimeUnit.MINUTES.toMillis(1)) == 0) {
                continue;
            }
            Iterator<SelectionKey> itr = selector.selectedKeys().iterator();

            while (itr.hasNext()) {
                SelectionKey key = itr.next();
                itr.remove();
                if (key.isAcceptable()) doAccept(key);
                if (key.isReadable()) doRead(key);
                if (key.isValid() && key.isWritable()) doWrite(key);
            }
        }
    }

    private void doAccept(SelectionKey key) throws IOException {
        ServerSocketChannel listenChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = listenChannel.accept();
        log.info("Hello, " + channel.getRemoteAddress());
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocateDirect(FOUR_KB));
    }

    private void doRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buf = (ByteBuffer) key.attachment();
        long num = channel.read(buf);
        log.info("doRead. num: " + num);
        if (num == EOF) {
            log.info("bye, " + channel.getRemoteAddress());
            channel.close();
        } else {
            int position = buf.position();
            byte b = buf.get(position - 1);
            if (position == buf.capacity() || b == ENTER) {
                key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            }
        }
    }

    private void doWrite(SelectionKey key) throws IOException {
        log.info("doWrite");
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buf = (ByteBuffer) key.attachment();
        buf.flip();
        channel.write(buf);
        if (!buf.hasRemaining()) {
            key.interestOps(SelectionKey.OP_READ);
        }
        buf.compact();
    }

    public static void main(String[] args) throws Exception {
        EchoServer server = new EchoServer();
        server.init();
    }
}
