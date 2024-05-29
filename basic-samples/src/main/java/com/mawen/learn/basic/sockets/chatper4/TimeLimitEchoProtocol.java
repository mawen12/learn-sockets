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
public class TimeLimitEchoProtocol implements Runnable {

	private static final int BUFFER_SIZE = 32;
	private static final String TIME_LIMIT = "10000";
	private static final String TIME_LIMIT_PROP = "Timelimit";

	private static int timelimit;
	private Socket clientSocket;
	private Logger logger;

	public TimeLimitEchoProtocol(Socket clientSocket, Logger logger) {
		this.clientSocket = clientSocket;
		this.logger = logger;
		this.timelimit = Integer.parseInt(System.getProperty(TIME_LIMIT_PROP, TIME_LIMIT));
	}

	public static void handleEchoClient(Socket clientSocket, Logger logger) {
		try {
			InputStream in = clientSocket.getInputStream();
			OutputStream out = clientSocket.getOutputStream();
			int receiveMessageSize;
			int totalBytesEchoed = 0;
			byte[] echoBuffer = new byte[BUFFER_SIZE];
			long endTime = System.currentTimeMillis() + timelimit;
			int timeBoundMillis = timelimit;

			clientSocket.setSoTimeout(timeBoundMillis);
			// Receive util client close connection, indicated by -1
			while (timeBoundMillis > 0 && (receiveMessageSize = in.read(echoBuffer)) != -1) {
				out.write(echoBuffer, 0, receiveMessageSize);
				totalBytesEchoed += receiveMessageSize;
				timeBoundMillis = (int) (endTime - System.currentTimeMillis());
				clientSocket.setSoTimeout(timeBoundMillis);
			}

			logger.info("Client " + clientSocket.getRemoteSocketAddress() + ", echoed " + totalBytesEchoed + " bytes.");
		}
		catch (IOException e) {
			logger.log(Level.WARNING, "Exception in echo protocol", e);
		}
	}

	@Override
	public void run() {
		handleEchoClient(clientSocket,logger);
	}
}
