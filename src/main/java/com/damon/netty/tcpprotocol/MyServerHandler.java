package com.damon.netty.tcpprotocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        //接收数据并处理
        int len = msg.getLen();
        byte[] content = msg.getContent();
        System.out.println("服务器接收到信息如下，长度：" + len + ",内容：" + new String(content, Charset.forName("utf-8")));
        System.out.println("服务器接收到的消息包数量:" + count);

        //回复消息
        String respContent = UUID.randomUUID().toString();
        int respLen = respContent.getBytes("utf-8").length;
        //构建协议包
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(respLen);
        messageProtocol.setContent(respContent.getBytes("utf-8"));
        ctx.writeAndFlush(messageProtocol);
    }
}
