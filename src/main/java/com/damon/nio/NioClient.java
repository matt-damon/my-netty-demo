package com.damon.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient {
    public static void main(String[] args) throws Exception {
        //得到网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 666);
        //连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {//不会阻塞
            while (!socketChannel.finishConnect()) {
                System.out.println("连接未完成，客户端不阻塞，可以做其他工作");
            }
        }
        //连接成功开始发送数据
        String data = "hello,达达";
        ByteBuffer byteBuffer = ByteBuffer.wrap(data.getBytes());
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}
