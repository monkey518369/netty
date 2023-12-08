package org.monkey.learn.netty.captor1.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 *
 * @author : monkey
 * @date   : 2023/11/27 23:16
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] bytes = new byte[buf.writerIndex()];
		buf.readBytes(bytes);
		System.out.println(new String(bytes));
		buf.writeBytes("monkey reaad".getBytes());
		ctx.channel().write(buf);
		ctx.write(buf);
	}

	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

}
