package com.damon.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HttpServer {
    public static void main(String[] args) throws Exception  {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);//设置线程数为1
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerInitializer());//也是一个handler

            //3个handler串联ServerInitializer -> HttpServerCodec -> HttpServerHandler,实际是串的是ChannelHandlerContext

            ChannelFuture channelFuture = bootstrap.bind(8080).sync();//同步等bind成功
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("监听端口8080成功");
                    } else {
                        System.out.println("监听端口8080失败");
                    }
                }
            });
            channelFuture.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}
