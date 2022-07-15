package com.damon.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;


//客户端和服务器端相互通讯的数据被封装成HttpObject
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    //读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {

            //浏览器和channel一一对应，channel和pipeline一一对应，pipeline和具体的handler一一对应
            System.out.println("pipeline hashcode:" + ctx.pipeline().hashCode() + ",HttpServerHandler hashcode:" + this.hashCode());

            System.out.println("msg 类型：" + msg.getClass());
            System.out.println("客户端地址：" + ctx.channel().remoteAddress());

            //服务器过滤资源
            HttpRequest httpRequest = (HttpRequest)msg;
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了favicon.ico，不做响应");
                return;
            }

            //回复信息给浏览器[http协议]
            ByteBuf content = Unpooled.copiedBuffer("hello,我是服务器", CharsetUtil.UTF_8);
            //构造http的响应，即httpresponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            ctx.writeAndFlush(response);
        }
    }
}
