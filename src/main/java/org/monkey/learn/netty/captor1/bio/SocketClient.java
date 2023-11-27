package org.monkey.learn.netty.captor1.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author : monkey
 * @date   : 2023/11/21 21:24
 */
public class SocketClient {

	public static void main(String[] args) throws IOException {
		Socket socket = new Socket("127.0.0.1", 9966);
		OutputStream outputStream = socket.getOutputStream();
		outputStream.write("monkey got it".getBytes());
		outputStream.close();
		socket.close();
	}

}
