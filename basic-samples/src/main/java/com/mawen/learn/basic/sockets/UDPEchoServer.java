package com.mawen.learn.basic.sockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Loop forever, receive datagrams and then sending the same datagrams back to the client.
 *
 * <pre>{@code
 *  java UDPEchoServer 8081
 * }</pre>
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/25
 */
public class UDPEchoServer {

	/**
	 * Maximum size of echo datagram
	 */
	private static final int ECHOMAX = 25;

	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			throw new IllegalArgumentException("Parameter(s): <Port>");
		}

		int servPort = Integer.parseInt(args[0]);

		DatagramSocket socket = new DatagramSocket(servPort);
		DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);

		// Run forever, receiving and echoing datagrams
		while (true) {
			// Receive packet from client
			socket.receive(packet);
			System.out.println("Handling client at " + packet.getAddress().getHostAddress() + " on port " + packet.getPort());

			// Send the same packet back to client
			socket.send(packet);
			// Reset length to avoid shrinking buffer
			packet.setLength(ECHOMAX);
		}
	}
}
