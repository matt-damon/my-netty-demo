package com.damon.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {



    /**
     * decode会根据接收的数据，被调用多次，直到确定没有新的元素被添加进list
     * 或是ByteBuf没有更多的可读字节为止
     * 如果List out不为空，会将list的内容传递给下个handler处理
     *
     * @param ctx
     * @param in  入站的ByteBuf
     * @param out  将解码后的数据传给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }

    }
}
