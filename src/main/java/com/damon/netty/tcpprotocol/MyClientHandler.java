package com.damon.netty.tcpprotocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            String mes = "今天天气真的好呀~";
            byte[] content = mes.getBytes(Charset.forName("utf-8"));
            int length = content.length;

            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(length);
            messageProtocol.setContent(content);

            ctx.writeAndFlush(messageProtocol);
        }
    }

    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int len = msg.getLen();
        byte[] content = msg.getContent();
        System.out.println("客户端接收到消息如下，长度:" + len + "，内容：" + new String(content, Charset.forName("utf-8")));
        System.out.println("客户端接收消息数量：" + ++count);
    }


}
