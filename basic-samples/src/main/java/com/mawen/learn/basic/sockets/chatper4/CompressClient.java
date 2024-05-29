package com.mawen.learn.basic.sockets.chatper4;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Implements the client side of the compression protocol.
 * The uncompressed bytes are read from the file specified on the command line,
 * and the compressed bytes are written to a new file.
 *
 * <p>It use {@link Socket#shutdownOutput()} after sending the uncompressed bytes.
 *
 * <pre>{@code
 *  java CompressClient 127.0.0.1 8081 README.md
 * }</pre>
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/29
 */
public class CompressClient {

	public static final int BUFFER_SIZE = 256;

	public static void main(String[] args) {

		if (args.length != 3) {
			throw new IllegalArgumentException("Parameter(s): <Server> <Port> <File>");
		}

		String server = args[0];
		int port = Integer.parseInt(args[1]);
		String fileName = args[2];

		// Open input and output file (named input.gz)
		try (FileInputStream fis = new FileInputStream(fileName);
		     FileOutputStream fos = new FileOutputStream(fileName + ".gz")) {

			// Create socket connected to server on specified port
			Socket socket = new Socket(server, port);

			// Send uncompressed byte stream to server
			sendBytes(socket, fis);

			// Receive compressed byte stream from server
			InputStream socketIn = socket.getInputStream();
			int bytesRead;
			byte[] buffer = new byte[BUFFER_SIZE];

			while ((bytesRead = socketIn.read(buffer)) != -1) {
				fos.write(buffer, 0, bytesRead);
				System.out.println("R"); // Reading progress indicator
			}
			System.out.println(); // End progress indicator line
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void sendBytes(Socket socket, InputStream fis) throws IOException {
		OutputStream socketOut = socket.getOutputStream();
		int bytesRead;
		byte[] buffer = new byte[BUFFER_SIZE];

		while ((bytesRead = fis.read(buffer)) != -1) {
			socketOut.write(buffer, 0, bytesRead);
			System.out.println("W"); // Writing progress indicator
		}
		socket.shutdownOutput(); // Finished sending
	}
}
