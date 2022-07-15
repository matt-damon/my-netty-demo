package com.damon.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    public static void main(String[] args) throws Exception {
        //创建ServerSocketChannel -> ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //得到Selector
        Selector selector = Selector.open();
        //绑定端口，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //把serverSocketChannel注册到selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            if (selector.select(1000) == 0) {//没有事件发生
                System.out.println("server等待了1s，无事发生");
                continue;
            }
            //返回关注事件的集合，通过selectionKeys反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey selectionKey = keyIterator.next();
                if (selectionKey.isAcceptable()) {//有新的客户端连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("获取客户端连接成功");
                    socketChannel.configureBlocking(false);//设置为非阻塞
                    //将socketChannel注册到selector，关注事件OP_READ，并关联一个bytebuffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if (selectionKey.isReadable()) {
                    //通过key反向获取到对应channel
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    //获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer)selectionKey.attachment();
                    channel.read(buffer);
                    System.out.println("from 客户端：" + new String(buffer.array()));
                }
                //手动移除selectionKey，防止重复操作
                keyIterator.remove();
            }
        }
    }
}
