package org.monkey.learn.netty.captor1.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author : monkey
 * @date   : 2023/11/21 21:24
 */
public class SocketService {

	public static final Integer PORT = 9966;

	private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 10,
			10000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

	public static void main(String[] args) throws IOException {
		try (ServerSocket server = new ServerSocket(PORT)){
			while (true) {
				Socket socket = server.accept();
				executor.execute(()-> {
					try {
						System.out.println("Thread:"+Thread.currentThread().getName());
						handler(socket);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				});;
			}
		}
	}

	private static void handler(Socket socket) throws IOException {
		try (InputStream inputStream = socket.getInputStream()){
			while (true) {
				byte[] bytes = new byte[16];
				if ((inputStream.read(bytes)) != -1) {
					System.out.println("read: "+ new String(bytes));
				}else {
					System.out.println("nothing to read");
					Thread.sleep(200);
				}
			}

		}catch (Exception e) {
			System.out.println(e);
		}finally {
			System.out.println("Thead:"+Thread.currentThread().getName()+" close");
			socket.close();;
		}

	}
}
