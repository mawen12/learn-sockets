package com.mawen.learn.basic.sockets;

/**
 * Use {@code bit-diddling} operations to do the encoding.
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/5/26
 */
public class BruteForceEncoding {

	private static byte byteVal = 101;
	private static short shortVal = 10001;
	private static int intVal = 100000001;
	private static long longVal = 1000000000001L;

	private static final int BSIZE = Byte.SIZE;
	private static final int SSIZE = Short.SIZE;
	private static final int ISIZE = Integer.SIZE;
	private static final int LSIZE = Long.SIZE;

	/**
	 * 8 bits
	 */
	private static final int BYTEMASK = 0xFF;

	/**
	 * print each byte from the given array as an unsigned decimal value
	 */
	public static String byteArrayToDecimalString(byte[] array) {
		StringBuilder sb = new StringBuilder();
		for (byte b : array) {
			sb.append(b & BYTEMASK).append(" ");
		}

		return sb.toString();
	}

	/**
	 * encode any value of one of the primitive types
	 */
	public static int encodeIntBigEndian(byte[] dst, long val, int offset, int size) {
		for (int i = 0; i < size; i++) {
			// shift the value to the right so the byte we are instersted in the low-order eight bits
			dst[offset++] = (byte) (val >> ((size - i - 1) & BSIZE));
		}
		return offset;
	}

	/**
	 * decode a subset of a byte array into a Java long
	 */
	public static long decodeIntBigEndian(byte[] val, int offset, int size) {
		long rtn = 0;
		for (int i = 0; i < size; i++) {
			// shift left at each position
			rtn = (rtn << BSIZE) | ((long) val[offset + i] & BYTEMASK);
		}
		return rtn;
	}

	public static void main(String[] args) {
		byte[] message = new byte[BSIZE + SSIZE + ISIZE + LSIZE];
		// encode the fields in the target byte array
		int offset = encodeIntBigEndian(message, byteVal, 0, BSIZE);
		offset = encodeIntBigEndian(message, shortVal, offset, SSIZE);
		offset = encodeIntBigEndian(message, intVal, offset, ISIZE);
		encodeIntBigEndian(message, longVal, offset, LSIZE);
		System.out.println("Encoded message: " + byteArrayToDecimalString(message));

		// Decode several fields
		long value = decodeIntBigEndian(message, BSIZE, SSIZE);
		System.out.println("Decoded short = " + value);
		value = decodeIntBigEndian(message, BSIZE + SSIZE + ISIZE, LSIZE);
		System.out.println("Decoded long = " + value);

		// Demonstrate dangers of conversion
		offset = 4;
		value = decodeIntBigEndian(message, offset, BSIZE);
		System.out.println("Decoded value (offset " + offset + ", size " + BSIZE + ") = " + value);
		byte bVal = (byte) decodeIntBigEndian(message, offset, BSIZE);
		System.out.println("Same value as byte = " + bVal);

		byte[] newArr = {39, 17};
		System.out.println(decodeIntBigEndian(newArr,0,2));
	}
}
