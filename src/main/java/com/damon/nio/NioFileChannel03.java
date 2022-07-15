package com.damon.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

//使用一个buffer拷贝文件
public class NioFileChannel03 {
    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream("file01.txt");
        FileChannel fileChannel01 = fis.getChannel();

        FileOutputStream fos = new FileOutputStream("file02.txt");
        FileChannel fileChannel02 = fos.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true) {
            byteBuffer.clear();//重要操作，重置pos=0,limit=capacity,mark=-1
            //从filechannel01读取数据写到buffer，如果没有clear，pos=limit，会读不了数据到buffer，read=0，一直循环
            int read = fileChannel01.read(byteBuffer);
            if (read == -1) {
                break;
            }
            byteBuffer.flip();//从写模式切换到读模式
            fileChannel02.write(byteBuffer);//写到filechannel02
        }
        fis.close();
        fos.close();
    }
}
