package com.damon.nio;


import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

//从文件读数据
//文件->FileInputStream(包装FileChannel)->ByteBuffer
public class NioFileChannel02 {

    public static void main(String[] args) throws Exception {
        File file = new File("file01.txt");
        FileInputStream fis = new FileInputStream(file);
        FileChannel fileChannel = fis.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate((int)file.length());
        //将通道的数据读入到buffer
        fileChannel.read(byteBuffer);

        //将字节转成字符串
        System.out.println(new java.lang.String(byteBuffer.array()));
        fis.close();
    }
}
