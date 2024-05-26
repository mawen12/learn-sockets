package com.mawen.learn.basic.sockets.protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Wire Format "VOTEPROTO" <"v"|"i"> [<RESPFLAG>] [<CANDIDATE>] [<VOTECNT>]
 * Charset is fixed by the wire format.
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/26
 */
public class VoteMsgTextCoder implements VoteMsgCoder {

	/**
	 * Manifest constants for encoding
	 */
	public static final String MAGIC = "Voting";
	public static final String VOTE_STR = "v";
	public static final String INQ_STR = "i";
	public static final String RESPONSE_STR = "R";

	public static final String CHARSET_NAME = "US-ASCII";
	public static final String DELIMITER = " ";
	public static final int MAX_WIRE_LENGTH = 2000;

	@Override
	public byte[] toWire(VoteMsg msg) throws IOException {
		String msgString = MAGIC + DELIMITER + (msg.isInquiry() ? INQ_STR : VOTE_STR)
		                   + DELIMITER + (msg.isResponse() ? RESPONSE_STR + DELIMITER : "")
		                   + Integer.toString(msg.getCandidateID())
		                   + DELIMITER + Long.toString(msg.getVoteCount());
		return msgString.getBytes(CHARSET_NAME);
	}

	@Override
	public VoteMsg fromWire(byte[] data) throws IOException {
		ByteArrayInputStream stream = new ByteArrayInputStream(data);
		Scanner s = new Scanner(new InputStreamReader(stream, CHARSET_NAME));
		boolean isInquiry;
		boolean isResponse;
		int candidateID;
		long voteCount;
		String token;

		try {
			token = s.next();
			if (!token.equals(MAGIC)) {
				throw new IOException("Bad magic string: " + token);
			}
			token = s.next();
			if (token.equals(VOTE_STR)) {
				isInquiry = false;
			}
			else if (!token.equals(INQ_STR)) {
				throw new IOException("Bad vote/inq indicator: " + token);
			}
			else {
				isInquiry = true;
			}

			token = s.next();
			if (token.equals(RESPONSE_STR)) {
				isResponse = true;
				token = s.next();
			}
			else {
				isResponse = false;
			}

			// Current token is candidateID
			candidateID = Integer.parseInt(token);
			if (isResponse) {
				token = s.next();
				voteCount = Long.parseLong(token);
			}
			else {
				voteCount = 0;
			}
		}
		catch (IOException e) {
			throw new RuntimeException("Parse error...");
		}

		return new VoteMsg(isInquiry, isResponse, candidateID, voteCount);
	}
}
