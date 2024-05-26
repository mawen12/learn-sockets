package com.mawen.learn.basic.sockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Receive and repeat data forever.
 *
 * <p>The {@link ServerSocket} instance is used to supply a new, connected {@link Socket} instance for each new incoming TCP connection.
 *
 * <p>When the server is ready to handle a client, it calls {@link ServerSocket#accept()}, which blocks util
 * an incoming connection is made to the {@link ServerSocket}'s port.
 *
 * <p>The {@link ServerSocket#accept()} method returns an instance of {@link Socket} that is already connected to the client's socket and
 * ready for reading and writing.
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/23
 */
public class TCPEchoServer {

	/**
	 * Size of receive buffer
	 */
	private static final int BUF_SIZE = 32;

	public static void main(String[] args) throws IOException {
		// Test for correct # of args
		if (args.length != 1) {
			throw new IllegalArgumentException("Parameter(s): <Port>");
		}

		int servPort = Integer.parseInt(args[0]);

		// Create a server socket to accept client connection requests
		ServerSocket servSock = new ServerSocket(servPort);

		// Size of received message
		int recvMsgSize;
		// Receive buffer
		byte[] receiveBuf = new byte[BUF_SIZE];

		// Run forever, accepting and servicing connections
		while (true) {
			// Get client connection
			Socket clntSock = servSock.accept();

			SocketAddress clientAddress = clntSock.getRemoteSocketAddress();
			System.out.println("Handling client at " + clientAddress);

			InputStream in = clntSock.getInputStream();
			OutputStream out = clntSock.getOutputStream();

			// Receive util client close connection, indicated by -1 return
			while ((recvMsgSize = in.read(receiveBuf)) != -1) {
				out.write(receiveBuf, 0, recvMsgSize);
			}

			// Close the socket
			clntSock.close();
		}
	}
}
