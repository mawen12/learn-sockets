package com.mawen.learn.basic.sockets.chatper4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/28
 */
public class TCPEchoServerThread {

	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			throw new IllegalArgumentException("Parameter(s): <Port>");
		}

		int echoServerPort = Integer.parseInt(args[0]);

		// Create a server socket to accept client connection requests
		ServerSocket serverSocket = new ServerSocket(echoServerPort);

		Logger logger = Logger.getLogger("practical");

		// Run forever, accepting and spawning a thread for each connection
		while (true) {
			// Block waiting for connection
			Socket clientSocket = serverSocket.accept();
			// Spawn thread to handle new connection
			Thread thread = new Thread(new EchoProtocol(clientSocket, logger));
			thread.start();
			logger.info("Created and started Thread " + thread.getName());
		}
	}
}
