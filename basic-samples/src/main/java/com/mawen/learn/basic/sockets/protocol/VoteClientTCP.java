package com.mawen.learn.basic.sockets.protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import com.mawen.learn.basic.sockets.LengthFramer;

/**
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


	}
}
