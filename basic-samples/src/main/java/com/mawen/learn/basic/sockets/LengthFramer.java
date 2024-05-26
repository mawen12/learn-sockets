package com.mawen.learn.basic.sockets;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Length-based framing for messages up to 65535 bytes in length.
 * The sender determines the length of the given message and
 * writes it to the output stream as a two-byte, big-endian integer, followed by the complete message.
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/26
 */
public class LengthFramer implements Framer {

	public static final int MAX_MESSAGE_LENGTH = 65535;
	public static final int BYTE_MASK = 0xff;
	public static final int SHORT_MASK = 0xffff;
	public static final int BYTE_SHIFT = 8;

	/**
	 * wrapper for data I/O
	 */
	private DataInputStream in;

	public LengthFramer(InputStream in) {
		this.in = new DataInputStream(in);
	}

	@Override
	public void frameMsg(byte[] message, OutputStream out) throws IOException {
		if (message.length > MAX_MESSAGE_LENGTH) {
			throw new IOException("message too long");
		}
		// write length prefix
		out.write((message.length >> BYTE_SHIFT) & BYTE_MASK);
		out.write(message.length & BYTE_MASK);
		// write message
		out.write(message);
		out.flush();
	}

	@Override
	public byte[] nextMsg() throws IOException {
		int length;
		try {
			// read 2 bytes
			length = in.readUnsignedShort();
		}
		catch (IOException e) {
			// no (or 1 byte) message
			return null;
		}

		// 0 <= length <= 65535
		byte[] msg = new byte[length];
		// if exception, it's a framing error.
		in.readFully(msg);
		return msg;
	}
}
