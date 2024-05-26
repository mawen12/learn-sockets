package com.mawen.learn.basic.sockets.protocol;

import java.io.IOException;

/**
 * Support vote message serialization and deserialization.
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/26
 */
public interface VoteMsgCoder {

	byte[] toWire(VoteMsg msg) throws IOException;

	VoteMsg fromWire(byte[] data) throws IOException;
}
