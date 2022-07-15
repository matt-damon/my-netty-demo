package com.damon.netty.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //加入一个出站的handler对数据进行编码
        pipeline.addLast(new MyLongToByteEncoder());

        //加入一个入站的解码器(入站handler)
        pipeline.addLast(new MyByteToLongDecoder());

        //加入一个自定义的handler，处理业务
        pipeline.addLast(new MyClientHandler());
    }
}
