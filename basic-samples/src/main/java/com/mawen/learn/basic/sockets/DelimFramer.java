package com.mawen.learn.basic.sockets;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Delimiter-based framing using the "newline" character ("\n", byte value 10).
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/26
 */
public class DelimFramer implements Framer{

	private static final byte DELIMITER = '\n';

	private InputStream in;

	public DelimFramer(InputStream in) {
		this.in = in;
	}

	@Override
	public void frameMsg(byte[] message, OutputStream out) throws IOException {
		// ensure that the message does not contain the delimiter
		for (byte b : message) {
			if (b == DELIMITER) {
				throw new IOException("Message contains delimiter");
			}
		}

		out.write(message);
		out.write(DELIMITER);
		out.flush();
	}

	@Override
	public byte[] nextMsg() throws IOException {
		ByteArrayOutputStream messageBuffer = new ByteArrayOutputStream();
		int nextByte;

		while ((nextByte = in.read()) != DELIMITER) {
			if (nextByte == -1) {
				if (messageBuffer.size() == 0) {
					return null;
				}
				else {
					//
					throw new EOFException("Non-empty message without delimiter");
				}
			}
			messageBuffer.write(nextByte);
		}

		return messageBuffer.toByteArray();
	}
}
