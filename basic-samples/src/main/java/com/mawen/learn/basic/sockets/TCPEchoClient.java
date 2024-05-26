package com.mawen.learn.basic.sockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * A client that communicate with an echo server using TCP.
 * An echo server simply repeats whatever it receives back to the client.
 *
 * <pre>{@code
 *  java TCPEchoClient 127.0.0.1 "Echo this!" 8081
 * }</pre>
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @see TCPEchoServer
 * @since 2024/5/22
 */
public class TCPEchoClient {

	public static void main(String[] args) throws IOException {

		if (args.length < 2 || args.length > 3) { // Test for correct
			throw new IllegalArgumentException("Parameter(s): <Server> <Word> [<Port>]");
		}

		// Server name or IP address
		String server = args[0];
		// Convert argument String to bytes using the default character encoding
		byte[] data = args[1].getBytes();

		int servPort = (args.length == 3) ? Integer.parseInt(args[2]) : 7;

		// Create the socket that is connected to server on specified port
		Socket socket = new Socket(server, servPort);
		System.out.println("Connected to server...sending echo string");

		InputStream in = socket.getInputStream();
		OutputStream out = socket.getOutputStream();

		// Send the encoded string to the server
		out.write(data);

		// Receive the name string back from the server
		int totalBytesRcvd = 0;
		int bytesRead;
		while (totalBytesRcvd < data.length) {
			if ((bytesRead = in.read(data, totalBytesRcvd, data.length - totalBytesRcvd)) == -1) {
				throw new SocketException("Connection closed prematurely");
			}

			totalBytesRcvd += bytesRead;
		}

		System.out.println("Received: " + new String(data));

		// Close the socket and its streams
		socket.close();
	}
}
