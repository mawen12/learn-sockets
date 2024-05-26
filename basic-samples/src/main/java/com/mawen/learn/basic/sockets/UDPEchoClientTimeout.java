package com.mawen.learn.basic.sockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Send a datagram containing the string to be echoed and prints whatever it receives back from the server.
 *
 * <pre>{@code
 *  java UDPEchoClientTimeout 127.0.0.1 "Hello World" 8081
 * }</pre>
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/25
 */
public class UDPEchoClientTimeout {

	/**
	 * Resend timeout (missiseconds)
	 */
	private static final int TIMEOUT = 3000;
	/**
	 * Maximum retransmissions
	 */
	private static final int MAXTRIES = 5;

	public static void main(String[] args) throws IOException {

		if (args.length < 2 || args.length > 3) {
			throw new IllegalArgumentException("Parameter(s): <Server> <Word> [<Port>]");
		}

		// Server address
		InetAddress serverAddress = InetAddress.getByName(args[0]);
		// Convert the argument String to bytes using the default encoding
		byte[] content = args[1].getBytes();
		int servPort = args.length == 3 ? Integer.parseInt(args[2]) : 7;

		DatagramSocket socket = new DatagramSocket();
		// Maximum receive blocking time (milliseconds)
		socket.setSoTimeout(TIMEOUT);
		// Sending packet
		DatagramPacket sendPacket = new DatagramPacket(content, content.length, serverAddress, servPort);
		// Receiving packet
		DatagramPacket receivePacket = new DatagramPacket(new byte[content.length], content.length);

		// Packets may be lost, so wo have to keep trying
		int tries = 0;
		boolean receivedResponse = false;

		do {
			// Send the echo string
			socket.send(sendPacket);

			try {
				// Attempt echo reply reception
				socket.receive(receivePacket);
				// Check source
				if (!receivePacket.getAddress().equals(serverAddress)) {
					throw new IOException("Received packet from an unknown source");
				}

				receivedResponse = true;
			}
			catch (IOException e) {
				// Retry
				tries += 1;
				System.out.println("Timed out, " + (MAXTRIES - tries) + " more tries...");
			}
		}while (!receivedResponse && tries < MAXTRIES);

		if (receivedResponse) {
			System.out.println("Received: " + new String(receivePacket.getData()));
		}
		else {
			System.out.println("No response -- giving up.");
		}
		socket.close();
	}
}
