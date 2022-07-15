package com.damon.netty.protobuf2;

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
        MyDataInfo.MyMessage myMessage = (MyDataInfo.MyMessage) msg;
        MyDataInfo.MyMessage.DataType dataType = myMessage.getDataType();
        if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {
            MyDataInfo.Student student = myMessage.getStudent();
            System.out.println("学生id:" + student.getId() + ",name:" + student.getName());
        } else if (dataType == MyDataInfo.MyMessage.DataType.WorkerType) {
            MyDataInfo.Worker worker = myMessage.getWoker();
            System.out.println("工人age:" + worker.getAge() + ",name:" + worker.getName());
        } else {
            System.out.println("传输类型不正确");
        }
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
