package com.mawen.learn.basic.sockets.chatper4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/28
 */
public class EchoProtocol implements Runnable {

	/**
	 * Size (in bytes) of I/O buffer
	 */
	private static final int BUFFER_SIZE = 32;

	/**
	 * Socket connect to client
	 */
	private Socket clientSocket;
	/**
	 * Server logger
	 */
	private Logger logger;

	public EchoProtocol(Socket clientSocket, Logger logger) {
		this.clientSocket = clientSocket;
		this.logger = logger;
	}

	public static void handleEchoClient(Socket clientSocket, Logger logger) {
		// Get the input and output I/O streams from socket
		try {
			InputStream in = clientSocket.getInputStream();
			OutputStream out = clientSocket.getOutputStream();

			int receiveMessageSize;
			int totalBytesEchoed = 0;
			byte[] echoBuffer = new byte[BUFFER_SIZE];
			while ((receiveMessageSize = in.read(echoBuffer)) != -1) {
				out.write(echoBuffer, 0, receiveMessageSize);
				totalBytesEchoed += receiveMessageSize;
			}

			logger.info("Client " + clientSocket.getRemoteSocketAddress() + " echoed " + totalBytesEchoed + " bytes.");
		}
		catch (IOException e) {
			logger.log(Level.WARNING, "Exception in echo protocol", e);
		}
		finally {
			try {
				clientSocket.close();
			}
			catch (IOException e) {
			}
		}
	}

	@Override
	public void run() {
		handleEchoClient(clientSocket,logger);
	}
}
