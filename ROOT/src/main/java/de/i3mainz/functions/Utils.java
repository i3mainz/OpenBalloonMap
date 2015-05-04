package de.i3mainz.functions;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

/**
 * CLASS Utils
 *
 * @author Martin Unold M.Sc.
 * @author Florian Thiery M.Sc.
 * @author i3mainz - Institute for Spatial Information and Surveying Technology
 * @version 04.05.2015
 */
public class Utils {

	private Utils() {
	}

	static final String REDIRECT_MAP = "/map";

	static final int RADIX = 36;
	static final int MAX_NR = RADIX * RADIX * RADIX * RADIX * RADIX;

	public static String toUTF8(String value) throws UnsupportedEncodingException {
		if (value == null) {
			return "";
		}
		byte[] b = value.getBytes("ISO-8859-1");
		return new String(b, "utf-8");
	}

	public static Timestamp stringToTimestamp(String value) {
		try {
			return Timestamp.valueOf(value);
		} catch (Exception e) {
			return new Timestamp(new java.util.Date().getTime());
		}
	}

	public static int stringToInt(String value) {
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			return 0;
		}
	}

	public static String intToCodeString(int value) {
		return Integer.toString(value, RADIX);
	}

	public static int codeStringToInt(String value) {
		try {
			return Integer.valueOf(value, RADIX);
		} catch (Exception e) {
			return 0;
		}
	}

	public static double stringToDouble(String value) {
		try {
			return Double.valueOf(value);
		} catch (Exception e) {
			return 0;
		}
	}

}
