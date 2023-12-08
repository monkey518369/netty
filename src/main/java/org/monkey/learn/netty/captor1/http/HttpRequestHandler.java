package org.monkey.learn.netty.captor1.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

/**
 *
 * @author : monkey
 * @date   : 2023/12/5 21:20
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<HttpObject> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
		System.out.println("什么鬼:" + msg);
		ByteBuf buf = Unpooled.wrappedBuffer("im netty http server".getBytes());
		DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
		response.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=utf-8");
		response.headers().add(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
		ctx.writeAndFlush(response);
	}
}
