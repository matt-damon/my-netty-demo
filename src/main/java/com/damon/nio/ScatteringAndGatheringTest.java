package com.damon.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 *  分散scattering:将数据写入到buffer，可以采用buffer数组，依次写入
 *  收集gathering:从buffer读取数据时，可采用buffer数组，依次读取
 *
 *  从客户端读取数据，再写回给客户端
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);
        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        SocketChannel socketChannel = serverSocketChannel.accept();
        int msgLen = 8;//假定从客户端接收8个字节
        while (true) {

            int byteRead = 0;
            while (byteRead < msgLen) {
                long l = socketChannel.read(byteBuffers);//将数据从channel写到buffer(从客户端读）
                byteRead += l;
                System.out.println(byteRead);
                Arrays.asList(byteBuffers).stream().map(buffer -> "pos:" + buffer.position() + ",limit:" + buffer.limit())
                        .forEach(str -> System.out.println(str));
            }

            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());//翻转，切到读
            long byteWrite = 0;
            while (byteWrite < msgLen) {
                long l = socketChannel.write(byteBuffers);//将数据从buffer写到channel(写回客户端)
                byteWrite += l;
            }
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());
            System.out.println("byteRead:" + byteRead);
        }
    }
}
