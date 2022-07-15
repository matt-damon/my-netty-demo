package com.damon.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {


    /**
     * 不用flip进行翻转
     * 0~readerIndex已读区域，readerIndex~writerIndex可读区域
     * writerIndex~capacity 可写的区域
     */

    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.buffer(10);//byte[10]

        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.getByte(i)); //不会造成readerIndex变化
        }

        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.readByte());//会造成readerIndex变化
        }
    }
}
