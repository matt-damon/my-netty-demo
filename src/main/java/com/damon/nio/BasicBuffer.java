package com.damon.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        IntBuffer intBuffer = IntBuffer.allocate(5);
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }
        intBuffer.flip();//读写切换

        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
