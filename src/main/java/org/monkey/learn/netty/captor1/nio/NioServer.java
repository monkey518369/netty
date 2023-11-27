package org.monkey.learn.netty.captor1.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author : monkey
 * @date   : 2023/11/23 0:20
 */
public class NioServer {

	private static Integer count = 0;

	private static final Map<String, SelectionKey> clients = new HashMap<>();

	public static void main(String[] args) {
		try (ServerSocketChannel server = ServerSocketChannel.open()){
			server.configureBlocking(false);
			server.bind(new InetSocketAddress("127.0.0.1",9966));
			Selector selector = Selector.open();
			server.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("star open connect");
			while (true){
				System.out.printf("第%s次循环%n",count++);
				selector.select(1000*10);
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = selectionKeys.iterator();
				while (iterator.hasNext()) {
					SelectionKey k = iterator.next();
					if (k.isReadable()) {
						handleRead(k);
					} else if (k.isWritable()) {
						handleWrite(k);
					} else if (k.isAcceptable()){
						handleAccept(k);
					}
					iterator.remove();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void handleAccept(SelectionKey key){
		try {
			ServerSocketChannel channel = (ServerSocketChannel)key.channel();
			SocketChannel socket = channel.accept();
			socket.configureBlocking(false);
			socket.register(key.selector(), SelectionKey.OP_READ);
		}catch ( Exception e) {
			e.printStackTrace();
		}
	}

	private static void handleRead(SelectionKey key) {
		try {
			SocketChannel channel = (SocketChannel) key.channel();
			ByteBuffer buffer = ByteBuffer.allocate(128);
			int read = channel.read(buffer);
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

	private static void handleWrite(SelectionKey key, String content) {
		try {
			SocketChannel channel = (SocketChannel) key.channel();
			channel.write(ByteBuffer.wrap(content.getBytes()));
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

}
