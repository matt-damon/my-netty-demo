package com.damon.nio.groupchat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    public GroupChatServer() {
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        try {
            while (true) {
                int count = selector.select();
                if (count > 0) {//有事件处理
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);//设置为非阻塞
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress() + "上线......");
                        }
                        if (key.isReadable()) {//通道发送read事件
                            readData(key);
                        }
                    }
                    //selectedKeys只存放就绪的通道，因为底层代码是不知道key在什么时候被消费，
                    // 所以外部需要在迭代器内部进行迭代处理完成后remove()，否则就会出现一直有这个key存在
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //读取客户端消息
    private void readData(SelectionKey key) {
        //定义SocketChannel
        SocketChannel socketChannel = null;

        try {
            socketChannel = (SocketChannel)key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = socketChannel.read(buffer);
            if (count > 0) {//读取到数据
                String msg = new String(buffer.array());
                System.out.println("from client:" + msg);
                //向其他客户端转发消息(排除当前客户端)
                sendInfoToOtherClients(msg, socketChannel);
            }
        } catch (IOException e) {
            try {
                System.out.println(socketChannel.getRemoteAddress() + "离线了");
                key.cancel();//取消注册
                socketChannel.close();//关闭通道
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
    }

    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中...");
        //遍历所有注册到Selector上的SocketChannel
        for (SelectionKey key : selector.keys()) {
            Channel tgtChannel = key.channel();
            if (tgtChannel instanceof SocketChannel && tgtChannel != self) {//排除了服务端的ServerSocketChannel和当前客户端
                SocketChannel dest = (SocketChannel)tgtChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer chatServer = new GroupChatServer();
        chatServer.listen();
    }
}
