package org.monkey.learn.netty.captor1.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 *
 * @author : monkey
 * @date   : 2023/11/24 17:59
 */
public class NioClients {

	private static Integer count = 1;

	public static void main(String[] args) throws IOException {
		try (SocketChannel client = SocketChannel.open()){
			client.configureBlocking(false);
			client.connect(new InetSocketAddress("127.0.0.1",9966));
			if (client.finishConnect()) {
				Selector selector = Selector.open();
				client.register(selector, SelectionKey.OP_READ);
				while (true) {
					System.out.printf("第%s次循环%n",count++);
					handleWrite(client);
					if (selector.select() > 0) {
						Set<SelectionKey> keys = selector.selectedKeys();
						keys.forEach(i -> {
						if (i.isReadable()){
							handleRead(i);
						}
						});
						keys.clear();
					}
				}
			}else {
				System.out.println("not finish connect");
			}
		}
	}

	private static void handleRead(SelectionKey key){
		try {
			SocketChannel channel = (SocketChannel) key.channel();
			ByteBuffer buffer = ByteBuffer.allocate(128);
			int read = channel.read(buffer);
			System.out.println("读取数据的长度"+read);
			buffer.flip();
			// 因为如果客户端断开连接,为了让服务端知晓,客户端回发送一个空字符串过来,所以这里判断一下如果是-1,则关闭客户端,
			// 否则服务端这里回一直出发读事件,如果客户端发送一个空过来是不会出发读事件的,所以这里不需要担心误关闭
			while (read > 0 ) {
				int remaining = buffer.remaining();
				byte[] array = new byte[remaining];
				buffer.get(array);
				System.out.println(new String(array));
				buffer.clear();
				read = channel.read(buffer);
			}
			if (read < 0) {
				System.out.println("关闭客户端");
				channel.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void handleWrite(SocketChannel  channel){
		try {
			ByteBuffer buffer = ByteBuffer.wrap("monkey".getBytes());
			channel.write(buffer);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}



}

