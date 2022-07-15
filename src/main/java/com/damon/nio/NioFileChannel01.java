package com.damon.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

//本地文件写数据
// 数据->bytebuffer->fileoutputstream(包装filechannel)->文件

public class NioFileChannel01 {
    public static void main(String[] args) throws Exception {
        String str = "你好，damon";
        //创建输出流->channel
        FileOutputStream fos = new FileOutputStream("file01.txt");
        //通过输出流获取对应的FileChannel
        FileChannel fileChannel = fos.getChannel();

        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //str放入ByteBuffer
        byteBuffer.put(str.getBytes());
        //对bytebuffer反转，对bytebuffer是读，所以要flip
        byteBuffer.flip();
        //将bytebuffer数据写入到filechannel
        fileChannel.write(byteBuffer);
        fos.close();;

    }
}
