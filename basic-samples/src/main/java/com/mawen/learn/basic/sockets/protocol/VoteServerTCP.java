package com.mawen.learn.basic.sockets.protocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.mawen.learn.basic.sockets.Framer;
import com.mawen.learn.basic.sockets.LengthFramer;

/**
 * The server repeatedly accepts a new client connection and uses the {@link VoteService}
 * to generate responses to the client vote messages.
 *
 * <pre>{@code
 *  java VoteServerTCP 8081
 * }</pre>
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/27
 */
public class VoteServerTCP {

	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			throw new IllegalArgumentException("Parameter(s): <Port>");
		}

		int port = Integer.parseInt(args[0]);

		ServerSocket serverSocket = new ServerSocket(port);
		// Change Bin to Text on both client and server for different encoding
		VoteMsgBinCoder coder = new VoteMsgBinCoder();
		VoteService voteService = new VoteService();

		while (true) {
			Socket clientSocket = serverSocket.accept();
			System.out.println("Handling client at" + clientSocket.getRemoteSocketAddress());
			// Change Length to Delim for a different framing strategy
			Framer framer = new LengthFramer(clientSocket.getInputStream());

			try {
				byte[] req;
				while ((req = framer.nextMsg()) != null) {
					System.out.println("Received message (" + req.length + " bytes)");
					VoteMsg responseMsg = voteService.handleRequest(coder.fromWire(req));
					framer.frameMsg(coder.toWire(responseMsg), clientSocket.getOutputStream());
				}
			}
			catch (IOException e) {
				System.out.println("Error handling client: " + e.getMessage());
			}
			finally {
				System.out.println("Closing connection");
				clientSocket.close();
			}
		}
	}
}
