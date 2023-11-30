package org.monkey.learn.netty.captor1.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 *
 * 一个简单的使用netty进行客户端和服务端通信的例子,
 * 这个类是服务端的启动类,
 *
 * @author : monkey
 * @date   : 2023/11/27 23:13
 */
public class EchoServer {

	public static void main(String[] args) throws InterruptedException {
		// 创建服务端建立连接之后的处理类,所有的与服务端建立的连接都通过这个类进行处理,祖类是channelHandler
		EchoServerHandler handler = new EchoServerHandler();
		// 创建一个服务端的bootstrap,作为一切的开始
		ServerBootstrap bootstrap = new ServerBootstrap();
		// 给bootstrap绑定一个NioEventLoopGroup
		bootstrap.group(new NioEventLoopGroup())
				// 给bootstrap指定建立的channel类型
				.channel(NioServerSocketChannel.class)
				// 给bootstrap指定端口
				.localAddress(new InetSocketAddress(9966))
				// 给bootstrap指定处理新建立的连接的类
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(handler);
					}
				});

		// 绑定端口启动
		ChannelFuture future = bootstrap.bind().sync();
		// 处理完成之后关闭
		future.channel().closeFuture().sync();
	}

}
