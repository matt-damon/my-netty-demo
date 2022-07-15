package com.damon.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;


public class ServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //netty提供的处理http的编解码器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        //增加自己的处理器
        pipeline.addLast("MyHttpServerHandler", new HttpServerHandler());

    }
}
