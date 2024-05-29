package com.mawen.learn.basic.sockets.chatper4;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import com.mawen.learn.basic.sockets.protocol.VoteMsg;
import com.mawen.learn.basic.sockets.protocol.VoteMsgTextCoder;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/29
 */
public class VoteMulticastSender {

	public static final int CANDIDATE_ID = 475;

	public static void main(String[] args) throws IOException {

		if (args.length < 2 || args.length > 3) {
			throw new IllegalArgumentException("Parameter(s): <Multicast Addr> <Port> [<TTL>]");
		}

		InetAddress destAddress = InetAddress.getByName(args[0]);
		if (!destAddress.isMulticastAddress()) {
			throw new IllegalArgumentException("Not a multicast address");
		}

		int destPort = Integer.parseInt(args[1]);
		int ttl = (args.length == 3) ? Integer.parseInt(args[2]) : 1;

		MulticastSocket socket = new MulticastSocket();
		socket.setSoTimeout(ttl);

		VoteMsgTextCoder coder = new VoteMsgTextCoder();
		VoteMsg vote = new VoteMsg(true, true, CANDIDATE_ID, 1000001L);

		// Create and send a datagram
		byte[] msg = coder.toWire(vote);
		DatagramPacket message = new DatagramPacket(msg, msg.length, destAddress, destPort);

		System.out.println("Sending Text-Encoded Request (" + msg.length + " bytes): ");
		System.out.println(vote);
		socket.send(message);

		socket.close();
	}
}
