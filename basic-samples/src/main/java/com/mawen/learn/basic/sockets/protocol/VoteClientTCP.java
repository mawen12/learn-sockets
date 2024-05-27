package com.mawen.learn.basic.sockets.protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import com.mawen.learn.basic.sockets.LengthFramer;

/**
 * Implement a TCP voting client that connects over a TCP socket to the voting server,
 * send an inquiry followed by a vote, and then receives the inquiry and vote responses.
 *
 * <pre>{@code
 *  java VoteClientTCP 127.0.0.1 8081
 * }</pre>
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/26
 */
public class VoteClientTCP {

	public static final int CANDIDATE_ID = 888;

	public static void main(String[] args) throws IOException {

		if (args.length != 2) {
			throw new IllegalArgumentException("Parameter(s): <Server> <Port>");
		}

		String destAddr = args[0];
		int destPort = Integer.parseInt(args[1]);

		Socket socket = new Socket(destAddr, destPort);
		OutputStream out = socket.getOutputStream();

		// change bin to text for a different encoding strategy
		VoteMsgBinCoder coder = new VoteMsgBinCoder();
		// change length to delim for a different encoding strategy
		LengthFramer framer = new LengthFramer(socket.getInputStream());

		// create an inquiry request (2nd arg = true)
		VoteMsg msg = new VoteMsg(false, true, CANDIDATE_ID, 0);
		byte[] encodedMsg = coder.toWire(msg);

		// Send request
		System.out.println("Sending Inquiry (" + encodedMsg.length + " bytes): ");
		System.out.println(msg);
		framer.frameMsg(encodedMsg, out);

		// Now send a vote
		msg.setInquiry(false);
		encodedMsg = coder.toWire(msg);
		System.out.println("Sending Vote (" + encodedMsg.length + " bytes): ");
		framer.frameMsg(encodedMsg, out);

		// Receive inquiry response
		encodedMsg = framer.nextMsg();
		msg = coder.fromWire(encodedMsg);
		System.out.println("Received Response (" + encodedMsg.length + " bytes): ");
		System.out.println(msg);

		// Receive vote response
		msg = coder.fromWire(framer.nextMsg());
		System.out.println("Received Response (" + encodedMsg.length + " bytes): ");
		System.out.println(msg);

		socket.close();
	}
}
