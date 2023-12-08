package org.monkey.learn.netty.captor1.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 *
 * @author : monkey
 * @date   : 2023/12/5 19:29
 */
public class HttpServer {

	private static final Integer PORT = 8800;

	public static void main(String[] args) throws InterruptedException {
		NioEventLoopGroup group = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(group)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) {
						ch.pipeline()
								.addLast(new HttpServerCodec())
								.addLast(new HttpRequestHandler());
					}
				});
		ChannelFuture future = bootstrap.bind(PORT);
		future.addListener((ChannelFutureListener) result -> {
			if (result.isSuccess()) {
				System.out.println("start success");
			}
		});
		future.channel().closeFuture().sync();
	}

}
