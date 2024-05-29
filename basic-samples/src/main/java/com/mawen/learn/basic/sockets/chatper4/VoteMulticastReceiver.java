package com.mawen.learn.basic.sockets.chatper4;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

import com.mawen.learn.basic.sockets.protocol.VoteMsg;
import com.mawen.learn.basic.sockets.protocol.VoteMsgTextCoder;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/29
 */
public class VoteMulticastReceiver {

	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			throw new IllegalArgumentException("Parameter(s): <Multicast Addr> <Port>");
		}

		InetAddress address = InetAddress.getByName(args[0]);
		if (!address.isMulticastAddress()) {
			throw new IllegalArgumentException("Not a multicast address");
		}

		int port = Integer.parseInt(args[1]); // multicast port
		MulticastSocket socket = new MulticastSocket(port); // for receiving
		socket.joinGroup(address); // Join the multicast group

		VoteMsgTextCoder coder = new VoteMsgTextCoder();

		// Receive a datagram
		DatagramPacket packet = new DatagramPacket(new byte[VoteMsgTextCoder.MAX_WIRE_LENGTH], VoteMsgTextCoder.MAX_WIRE_LENGTH);
		socket.receive(packet);

		VoteMsg vote = coder.fromWire(Arrays.copyOfRange(packet.getData(), 0, packet.getLength()));
		System.out.println("Received Text-Encoded Request (" + packet.getLength() + " bytes): ");
		System.out.println(vote);

		socket.close();
	}
}
