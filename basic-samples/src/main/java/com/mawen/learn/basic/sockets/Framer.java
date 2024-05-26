package com.mawen.learn.basic.sockets;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/26
 */
public interface Framer {

	/**
	 * add framing information, outputs a given message to a given stream
	 */
	void frameMsg(byte[] message, OutputStream out) throws IOException;

	/**
	 * scans a given stream, extracting the next message
	 */
	byte[] nextMsg() throws IOException;
}
