package com.mawen.learn.basic.sockets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/26
 */
public class DataArrayAndByteArrayEncoding {

	private static byte byteVal = 101;
	private static short shortVal = 10001;
	private static int intVal = 100000001;
	private static long longVal = 1000000000001L;

	public static void main(String[] args) throws IOException {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(buf);

		out.writeByte(byteVal);
		out.writeShort(shortVal);
		out.writeInt(intVal);
		out.writeLong(longVal);
		out.flush();

		byte[] msg = buf.toByteArray();

		System.out.println(Arrays.toString(msg));

		out.close();
		buf.close();
	}
}
