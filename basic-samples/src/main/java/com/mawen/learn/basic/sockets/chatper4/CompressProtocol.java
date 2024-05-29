package com.mawen.learn.basic.sockets.chatper4;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/29
 */
public class CompressProtocol implements Runnable {

	public static final int BUFFER_SIZE = 1024;

	private Socket clientSocket;
	private Logger logger;

	public CompressProtocol(Socket clientSocket, Logger logger) {
		this.clientSocket = clientSocket;
		this.logger = logger;
	}

	@Override
	public void run() {
		try {
			// Get the input and output streams from socket
			InputStream in = clientSocket.getInputStream();
			GZIPOutputStream out = new GZIPOutputStream(clientSocket.getOutputStream());

			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead;

			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			out.finish();

			logger.info("Client " + clientSocket.getRemoteSocketAddress() + " finished");
		}
		catch (IOException e) {
			logger.log(Level.WARNING, "Exception in compress protocol", e);
		}

		try {
			// Close socket
			clientSocket.close();
		}
		catch (IOException e) {
			logger.info("Exception = " + e.getMessage());
		}
	}
}
