package com.mawen.learn.basic.sockets.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/26
 */
public class VoteService {

	// Map of candidates to number of votes
	private Map<Integer, Long> results = new HashMap<>();

	public VoteMsg handleRequest(VoteMsg msg) {
		// if response, just send it back
		if (msg.isResponse()) {
			return msg;
		}

		// make message a response
		msg.setResponse(true);

		// get candidate ID and vote count
		int candidate = msg.getCandidateID();
		Long count = results.get(candidate);
		if (count == null) {
			// candidate does not exist
			count = 0L;
		}

		if (!msg.isInquiry()) {
			results.put(candidate, ++count);
		}

		msg.setVoteCount(count);
		return msg;
	}
}
