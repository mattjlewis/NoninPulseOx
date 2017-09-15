package com.diozero.util;

import java.io.IOException;
import java.io.InputStream;

public class IOUtil {
	public static short readUByte(InputStream is) throws IOException {
		int i = is.read();
		if (i == -1) {
			throw new IOException("InputStream closed");
		}
		
		return (short)(i & 0xff);
	}
	
	public static int readUShort(InputStream is) throws IOException {
		return toInt(readUByte(is), readUByte(is));
	}
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j=0; j<bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static int toInt(byte hb, byte lb) {
		return hb<<8 | (lb & 0xFF);
	}

	public static int toInt(short hb, short lb) {
		return hb<<8 | (lb & 0xFF);
	}
}
