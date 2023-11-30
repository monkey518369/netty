package org.monkey.learn.netty.captor1.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 *
 * @author : monkey
 * @date   : 2023/11/27 23:13
 */
public class EchoClient                                                                                                      {

	public static void main(String[] args) throws InterruptedException {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(new NioEventLoopGroup())
				.channel(NioSocketChannel.class)
				.remoteAddress(new InetSocketAddress("127.0.0.1",9966))
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(	SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new EchoClientHandler());
					}
				});
		ChannelFuture sync = bootstrap.connect().sync();
		sync.channel().closeFuture().sync();
	}

}
