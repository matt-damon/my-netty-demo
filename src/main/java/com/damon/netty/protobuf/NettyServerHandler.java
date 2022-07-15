package com.damon.netty.protobuf;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

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
        //读取从客户端发来的StudentPOJO.Student
        StudentPOJO.Student student = (StudentPOJO.Student)msg;
        System.out.println("客户端的数据,id:" + student.getId() + ",name:" + student.getName());
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
