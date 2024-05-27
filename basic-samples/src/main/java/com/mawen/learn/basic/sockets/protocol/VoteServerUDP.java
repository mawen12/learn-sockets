package com.mawen.learn.basic.sockets.protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/27
 */
public class VoteServerUDP {

	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			throw new IllegalArgumentException("Parameter(s): <Port>");
		}

		int port = Integer.parseInt(args[0]);

		DatagramSocket socket = new DatagramSocket(port);

		byte[] inBuffer = new byte[VoteMsgTextCoder.MAX_WIRE_LENGTH];
		// Change Bin to Text for a different coding approach
		VoteMsgTextCoder coder = new VoteMsgTextCoder();
		VoteService voteService = new VoteService();

		while (true) {
			// Reset the data area to the input buffer on each iteration
			DatagramPacket packet = new DatagramPacket(inBuffer, inBuffer.length);
			// UDP does the framing for us
			socket.receive(packet);
			byte[] encodedMsg = Arrays.copyOfRange(packet.getData(), 0, packet.getLength());

			System.out.println("Handling request from " + packet.getSocketAddress() + " (" + encodedMsg.length + " bytes)");

			try {
				VoteMsg voteMsg = coder.fromWire(encodedMsg);
				voteMsg = voteService.handleRequest(voteMsg);
				packet.setData(coder.toWire(voteMsg));
				System.out.println("Sending response (" + packet.getLength() + " bytes): ");
				System.out.println(voteMsg);
				socket.send(packet);
			}
			catch (IOException e) {
				System.err.println("Parse error in message: " + e.getMessage());
			}
		}
	}
}
