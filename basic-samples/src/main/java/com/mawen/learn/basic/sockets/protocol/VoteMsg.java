package com.mawen.learn.basic.sockets.protocol;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/26
 */
public class VoteMsg {

	public static final int MAX_CANDIDATE_ID = 1000;

	private boolean isInquiry;
	private boolean isResponse;
	private int candidateID;
	private long voteCount;

	public VoteMsg (boolean isInquiry, boolean isResponse, int candidateID, long voteCount) {
		if (voteCount != 0 && !isResponse) {
			throw new IllegalArgumentException("Request vote count must be zero");
		}
		if (candidateID < 0 || candidateID > MAX_CANDIDATE_ID) {
			throw new IllegalArgumentException("Bad Candidate ID: " + candidateID);
		}
		if (voteCount < 0) {
			throw new IllegalArgumentException("Total must be >= zero");
		}

		this.isInquiry = isInquiry;
		this.isResponse = isResponse;
		this.candidateID = candidateID;
		this.voteCount = voteCount;
	}

	public void setCandidateID(int candidateID) {
		if (candidateID < 0 || candidateID > MAX_CANDIDATE_ID) {
			throw new IllegalArgumentException("Bad Candidate ID: " + candidateID);
		}
		this.candidateID = candidateID;
	}

	public void setVoteCount(long count) {
		if ((count != 0 && !isResponse) || count < 0) {
			throw new IllegalArgumentException("Bad vote count: " + count);
		}
		this.voteCount = count;
	}

	public void setInquiry(boolean inquiry) {
		isInquiry = inquiry;
	}

	public void setResponse(boolean response) {
		isResponse = response;
	}

	public boolean isInquiry() {
		return isInquiry;
	}

	public boolean isResponse() {
		return isResponse;
	}

	public int getCandidateID() {
		return candidateID;
	}

	public long getVoteCount() {
		return voteCount;
	}

	public String toString() {
		String res = (isInquiry ? "inquiry" : "vote") + " for candidate " + candidateID;
		if (isResponse) {
			res = "response to " + res + " who now has " + voteCount + " vote(s)";
		}
		return res;
	}
}
