package com.mawen.learn.basic.sockets.chatper4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/28
 */
public class TCPEchoServerPool {

	public static void main(String[] args) throws IOException {

		if (args.length != 2) {
			throw new IllegalArgumentException("Parameter(s): <Port> <Threads>");
		}

		int serverPort = Integer.parseInt(args[0]);
		int threadPoolSize = Integer.parseInt(args[1]);

		// Create a server socket to accept client connection requests
		ServerSocket serverSocket = new ServerSocket(serverPort);

		Logger logger = Logger.getLogger("practical");

		// Spawn a fixed number of threads to service clients
		for (int i = 0; i < threadPoolSize; i++) {
			Thread thread = new Thread() {
				@Override
				public void run() {
					while (true) {
						try {
							Socket clientSocket = serverSocket.accept();
							EchoProtocol.handleEchoClient(clientSocket,logger);
						}
						catch (IOException e) {
							logger.log(Level.WARNING, "Client accept failed", e);
						}
					}
				}
			};
			thread.start();
			logger.info("Created and started Thread = " + thread.getName());
		}
	}
}
