package com.mawen.learn.basic.sockets.protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

/**
 * Implement an UDP voting client that connects over an UDP socket to the voting server,
 * send an inquiry followed by a vote, and then receives the inquiry and vote responses.
 *
 * UDP doesn't need to use a framer, because UDP maintains message boundaries.
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/27
 */
public class VoteClientUDP {

	public static void main(String[] args) throws IOException {

		if (args.length != 3) {
			throw new IllegalArgumentException("Parameter(s): <Destination> <Port> <Candidate#>");
		}

		InetAddress destAddr = InetAddress.getByName(args[0]);
		int destPort = Integer.parseInt(args[1]);
		int candidate = Integer.parseInt(args[2]);

		DatagramSocket socket = new DatagramSocket();
		socket.connect(destAddr, destPort);

		// Create a voting message (2nd param false = vote)
		VoteMsg voteMsg = new VoteMsg(false, false, candidate, 0);

		// Change Text to Bin here for a different coding strategy
		VoteMsgTextCoder coder = new VoteMsgTextCoder();

		// Send request
		byte[] encodedVote = coder.toWire(voteMsg);
		System.out.println("Sending Text-Encoded Request (" + encodedVote.length + " bytes): ");
		System.out.println(voteMsg);

		DatagramPacket message = new DatagramPacket(encodedVote, encodedVote.length);
		socket.send(message);

		//Receive message
		message = new DatagramPacket(new byte[VoteMsgTextCoder.MAX_WIRE_LENGTH], VoteMsgTextCoder.MAX_WIRE_LENGTH);
		socket.receive(message);
		encodedVote = Arrays.copyOfRange(message.getData(), 0, message.getLength());

		System.out.println("Received Text-Encoded Response (" + encodedVote.length + " bytes): ");
		voteMsg = coder.fromWire(encodedVote);
		System.out.println(voteMsg);
	}
}
