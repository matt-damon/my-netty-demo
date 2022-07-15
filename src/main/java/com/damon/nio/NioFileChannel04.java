package com.damon.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class NioFileChannel04 {
    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream("a.png");
        FileOutputStream fos = new FileOutputStream("b.png");

        FileChannel srcCh = fis.getChannel();
        FileChannel destCh = fos.getChannel();

        destCh.transferFrom(srcCh, 0, srcCh.size());
        srcCh.close();
        destCh.close();
        fis.close();
        fos.close();
    }
}
