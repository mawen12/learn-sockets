package com.mawen.learn.basic.sockets.chatper4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * <pre>{@code
 *  java TCPEchoServerExecutor 8081
 * }</pre>
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/28
 */
public class TCPEchoServerExecutor {

	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			throw new IllegalArgumentException("Parameter(s) <Port>");
		}

		int serverPort = Integer.parseInt(args[0]);

		// Create a server socket to accept client connection requests
		ServerSocket serverSocket = new ServerSocket(serverPort);

		Logger logger = Logger.getLogger("practical");

		// Dispatch svc
		ExecutorService service = Executors.newCachedThreadPool();

		// Run forever, accepting and spawning a thread for each connection
		while (true) {
			Socket clientSocket = serverSocket.accept();
//			service.execute(new EchoProtocol(clientSocket,logger));
			service.execute(new CompressProtocol(clientSocket,logger));
		}
	}
}
