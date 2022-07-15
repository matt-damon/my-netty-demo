package com.damon.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

/*
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     *  ChannelHandlerContext ctx:上下文对象，含有管道pipeline,通道channel,地址
     *  Object msg: 客户端发送的数据
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器读取线程：" + Thread.currentThread().getName());
        System.out.println("server 上下文：" + ctx);

        //若此处任务非常耗时，改成异步执行->提交到该channel对应的NioEventLoop的taskQueue中

        //方案1 用户自定义的普通任务
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 5);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端，1", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //用户自定义定时任务 提交到scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 5);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端，1", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 5, TimeUnit.SECONDS);

        System.out.println("...server go on...");

        Channel channel = ctx.channel();
        ChannelPipeline pipeline = ctx.pipeline();//底层是双向链表


        //将msg转成ByteBuf
        ByteBuf  buf = (ByteBuf)msg;
        System.out.println("client origin msg:" + msg);
        System.out.println("client msg:" + buf.toString(CharsetUtil.UTF_8));
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端1!", CharsetUtil.UTF_8));
    }

    //处理异常，一般是关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
