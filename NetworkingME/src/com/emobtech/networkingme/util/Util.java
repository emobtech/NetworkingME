/* Util.java
 * 
 * Networking ME
 * Copyright (c) 2013 eMob Tech (http://www.emobtech.com/)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.emobtech.networkingme.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.Vector;

/**
 * <p>
 * This class implements a series of utility methods.
 * </p>
 * @author Ernandes Jr. (ernandes@emobtech.com)
 * @version 1.0
 * @since 1.0
 */
public final class Util {
	/**
	 * <p>
	 * EN-US locale.
	 * </p>
	 */
	public static final String LOCALE_ENUS = "en-US";
	
	/**
	 * <p>
	 * UTF-8 encoding.
	 * </p>
	 */
	public static final String UTF8 = "UTF-8";

	/**
	 * <p>
	 * Default user agent.
	 * </p>
	 */
	public static final String USER_AGENT =
		"NetworkingME/1.0 (JavaME; Profile/MIDP-2.0 Configuration/CLDC-1.0)";
	
	/**
     * <p>
     * Splits a string based on a given delimiter.
     * </p>
     * @param str String.
     * @param delimiter Delimiter.
     * @return String tokens.
     */
    public static String[] splitString(String str, char delimiter) {
    	if (isEmptyString(str)) {
    		return new String[0];
    	}
    	//
        Vector v = new Vector();
        int start = 0;
        int iof;
        //
        while ((iof = str.indexOf(delimiter, start)) != -1) {
            v.addElement(str.substring(start, iof).trim());
            start = iof +1;
        }
        //
        v.addElement(str.substring(start, str.length()).trim());
        String[] codes = new String[v.size()];
        v.copyInto(codes);
        //
        return codes;
    }
	
	/**
	 * <p>
	 * Checks whether the given string is null or empty.
	 * </p>
	 * @param str The string.
	 * @return true null/empty.
	 */
	public static boolean isEmptyString(String str) {
		return str == null || str.trim().length() == 0;
	}
	
	/**
	 * <p>
	 * Reads all bytes from a given stream.
	 * </p>
	 * @param in Stream.
	 * @return Bytes.
	 * @throws IOException If any I/O error occurs.
	 */
	public static byte[] readBytes(InputStream in) throws IOException {
		if (in == null) {
			return new byte[0];
		}
		//
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];
		//
		for (int n; (n = in.read(buffer)) > 0;) {
			out.write(buffer, 0, n);
		}
		//
		return out.toByteArray();
	}
	
	/**
	 * <p>
	 * Writes a given bytes to a stream.
	 * </p>
	 * @param data Bytes.
	 * @param out Stream.
	 * @throws IOException If any I/O error occurs.
	 */
	public static void writeBytes(byte[] data, OutputStream out)
		throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		byte[] buffer = new byte[1024];
		//
		for (int n; (n = in.read(buffer)) > 0;) {
			out.write(buffer, 0, n);
		}
	}
	
	/**
	 * <p>
	 * Encodes a given string.
	 * </p>
	 * @param str String.
	 * @return Encoded string.
	 */
	public static String encodeStringURL(String str) {
		if (str.indexOf('%') == -1) {
			return encodeStringURL(str, UTF8);
		} else {
			return str;
		}
	}
	
	/**
	 * <p>
	 * Encodes a given query string.
	 * </p>
	 * @param queryString Query string.
	 * @return Encoded query string.
	 */
	public static String encodeQueryString(String queryString) {
		StringBuffer encodedQueryString = new StringBuffer();
		String[] params = Util.splitString(queryString, '&');
		//
		for (int i = 0; i < params.length; i++) {
			String[] paramValue = Util.splitString(params[i], '=');
			//
			encodedQueryString.append(encodeStringURL(paramValue[0]));
			encodedQueryString.append('=');
			encodedQueryString.append(encodeStringURL(paramValue[1]));
			//
			if (i +1 < params.length) {
				encodedQueryString.append('&');
			}
		}
		//
		return encodedQueryString.toString();
	}
	
	/**
	 * <p>
	 * Creates a query string from a table of parameters.
	 * </p>
	 * @param parameters Parameters.
	 * @return Query string.
	 */
	public static String toQueryString(Hashtable parameters) {
		if (parameters == null || parameters.size() == 0) {
			return "";
		}
		//
		String key;
		StringBuffer queryStr = new StringBuffer();
		Enumeration keys = parameters.keys();
		//
		while (keys.hasMoreElements()) {
			key = (String)keys.nextElement();
			//
			queryStr.append(encodeStringURL(key));
			queryStr.append('=');
			queryStr.append(encodeStringURL((String)parameters.get(key)));
			//
			if (keys.hasMoreElements()) {
				queryStr.append('&');
			}
		}
		//
		return queryStr.toString();
	}
	
	/**
	 * <p>
	 * Formats a given path.
	 * </p>
	 * @param path Path.
	 * @return Formatted path.
	 */
	public static String formatPath(String path) {
		if (isEmptyString(path)) {
			return "";
		}
		//
		if (path.startsWith("/")) {
			return path;
		} else {
			return '/' + path;
		}
	}
	
	/**
	 * <p>
	 * Formats a given query string.
	 * </p>
	 * @param queryString Query string.
	 * @return Formatted query string.
	 */
	public static String formatQueryString(String queryString) {
		if (isEmptyString(queryString)) {
			return "";
		}
		//
		if (queryString.startsWith("?")) {
			return queryString;
		} else {
			return '?' + queryString;
		}
	}
	
	/**
	 * <p>
	 * Formats a cookie.
	 * </p>
	 * @param name Name.
	 * @param value Value.
	 * @return Formatted cookie.
	 */
	public static String formatCookie(String name, String value) {
		return name + '=' + value;
	}
	
	/**
	 * <p>
	 * Formats a given cookie date.
	 * </p>
	 * @param date Date as string.
	 * @return Date.
	 */
	public static Date parseCookieDate(String date) {
		if (isEmptyString(date)) {
			return null;
		}
		//
		date = replaceAllStrings(date, "-", " ");
		//
		String[] dateParts = splitString(date, ' ');
		//
		if (dateParts.length < 6) {
			return null;
		}
		//
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		//
		cal.set(Calendar.YEAR, Integer.parseInt(dateParts[3]));
		cal.set(Calendar.MONTH, getMonth(dateParts[2]));
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateParts[1]));
		//
		String[] timeParts = splitString(dateParts[4], ':');
		//
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeParts[0]));
		cal.set(Calendar.MINUTE, Integer.parseInt(timeParts[1]));
		cal.set(Calendar.SECOND, Integer.parseInt(timeParts[2]));
		cal.set(Calendar.MILLISECOND, 0);
		//
		return cal.getTime();
	}
	
	/**
	 * <p>
	 * Returns the month number from a given month name.
	 * </p>
	 * @param name Name.
	 * @return Number.
	 * @throws IllegalArgumentException Name is null!
	 */
	public static int getMonth(String name) {
		if (isEmptyString(name)) {
			throw new IllegalArgumentException("Name is null!");
		}
		//
		name = name.trim().toLowerCase();
		//
		final String[] months = {
			"jan", "feb", "mar", 
			"apr", "may", "jun", 
			"jul", "aug", "sep", 
			"oct", "nov", "dec"
		};
		//
		for (int i = 0; i < months.length; i++) {
			if (name.startsWith(months[i])) {
				return i;
			}
		}
		//
		return -1;
	}
	
	/**
	 * <p>
	 * Encodes a given string with a encoding.
	 * </p>
	 * @param str String.
	 * @param encoding Encoding.
	 * @return Enconded string.
	 */
	public static String encodeStringURL(String str, String encoding) {
		if (isEmptyString(str)) {
			return str;
		}
		//
		if (isEmptyString(encoding)) {
			encoding = getSystemProperty("microedition.encoding", UTF8);
		}
		//
		ByteArrayInputStream bIn;
		//
		try {
			bIn = new ByteArrayInputStream(str.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			bIn = new ByteArrayInputStream(str.getBytes());
		}
		//
		int c = bIn.read();
		StringBuffer ret = new StringBuffer();
		//
		while (c >= 0) {
			if ((c >= 'a' && c <= 'z')
					|| (c >= 'A' && c <= 'Z')
					|| (c >= '0' && c <= '9')
					|| c == '.'
					|| c == '-'
					|| c == '*'
					|| c == '_') {
				ret.append((char) c);
			} else if (c == ' ') {
				ret.append("%20");
			} else {
				if (c < 128) {
					ret.append(getHexChar(c));
				} else if (c < 224) {
					ret.append(getHexChar(c));
					ret.append(getHexChar(bIn.read()));
				} else if (c < 240) {
					ret.append(getHexChar(c));
					ret.append(getHexChar(bIn.read()));
					ret.append(getHexChar(bIn.read()));
				}
			}
			//
			c = bIn.read();
		}
		//
		return ret.toString();
	}
	
	/**
	 * <p>
	 * Returns the hex value of a char.
	 * </p>
	 * @param c Char.
	 * @return Hex value.
	 */
	public static String getHexChar(int c) {
		return (c < 16 ? "%0" : "%") + Integer.toHexString(c).toUpperCase();
	}
	
	/**
	 * <p>
	 * Encodes a given string to Base64.
	 * </p>
	 * @param str String.
	 * @return Base64 string.
	 */
	public static String encodeStringBase64(String str) {
		if (isEmptyString(str)) {
			return str;
		}
		//
		final String c =
			"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		//
		byte[] code = c.getBytes();
		byte[] s = str.getBytes();
		int x;
		int y = str.length() - (str.length() % 3);
		byte[] coded = new byte[4];
		StringBuffer dest = new StringBuffer();
		//
		for (x = 0; x < y; x += 3) {
			coded[3] = code[s[x + 2] % 64];
			coded[0] = code[s[x] >> 2];
			coded[1] = new Integer((s[x] % 4) << 4).byteValue();
			coded[1] += s[x + 1] >> 4;
			coded[1] = code[coded[1]];
			coded[2] = new Integer((s[x + 1] % 16) << 2).byteValue();
			coded[2] += s[x + 2] / 64;
			coded[2] = code[coded[2]];
			//
			dest.append(new String(coded));
		}
		//
		x = y;
		//
		if (s.length % 3 == 0) {
			return dest.toString();
		}
		//
		if (s.length % 3 == 1) {
			coded[2] = '=';
			coded[3] = '=';
			coded[0] = code[s[x] >> 2];
			coded[1] = code[new Integer((s[x] % 4) << 4).byteValue()];
			//
			dest.append(new String(coded));
		}
		//
		if (s.length % 3 == 2) {
			coded[3] = '=';
			coded[0] = code[s[x] >> 2];
			coded[1] = new Integer((s[x] % 4) << 4).byteValue();
			coded[1] += s[x + 1] >> 4;
			coded[1] = code[coded[1]];
			coded[2] = code[new Integer((s[x + 1] % 16) << 2).byteValue()];
			//
			dest.append(new String(coded));
		}
		//
		return dest.toString();
	}
	
	/**
	 * <p>
	 * Returns the system property value or a default value if it is 
	 * <code>null</code>.
	 * </p>
	 * @param key Property key.
	 * @param defaultValue Default value.
	 * @return Value.
	 */
	public static String getSystemProperty(String key, String defaultValue) {
		String value = System.getProperty(key);
		//
		return isEmptyString(value) ? defaultValue : value;
	}
	
	/**
	 * <p>
	 * Returns the bytes of a given string.
	 * </p>
	 * @param str String.
	 * @return Bytes.
	 */
	public static byte[] toBytesString(String str) {
		if (isEmptyString(str)) {
			return new byte[0];
		}
		//
		try {
			return str.getBytes(UTF8);
		} catch (UnsupportedEncodingException e) {
			return str.getBytes();
		}
	}
	
	/**
	 * <p>
	 * Returns the string of a given bytes.
	 * </p>
	 * @param bytes Bytes.
	 * @return String.
	 */
	public static String toStringBytes(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return "";
		}
		//
		try {
			return new String(bytes, UTF8);
		} catch (UnsupportedEncodingException e) {
			return new String(bytes);
		}
	}
	
	/**
	 * <p>
	 * Finds the index of a given string.
	 * </p>
	 * @param str String.
	 * @param search String to find.
	 * @param fromIndex From index.
	 * @param sensitive Case sensitive (true).
	 * @return Index.
	 */
	public static int indexOfString(String str, String search, int fromIndex, 
		boolean sensitive) {
		if (!sensitive) {
			str = str.toLowerCase();
			search = search.toLowerCase();
		}
		//
		return str.indexOf(search, fromIndex);
	}
	
	/**
	 * <p>
	 * Finds the index of a given string or its last index.
	 * </p>
	 * @param str String.
	 * @param search String to find.
	 * @param fromIndex From index.
	 * @param sensitive Case sensitive (true).
	 * @return Index.
	 */
	public static int indexOrLastOfString(String str, String search, 
		int fromIndex, boolean sensitive) {
		int index = indexOfString(str, search, fromIndex, sensitive);
		//
		return index != -1 ? index : str.length();
	}
	
	/**
	 * <p>
	 * Replaces all occurences of a given string.
	 * </p>
	 * @param str String.
	 * @param searchStr String to replace.
	 * @param replacementStr Replacement string.
	 * @return Replaced string.
	 */
	public static String replaceAllStrings(String str, String searchStr,
		String replacementStr) {
		if (str == null) {
			throw new IllegalArgumentException("Str null!");
		}
		if (searchStr == null) {
			throw new IllegalArgumentException("SearchStr null!");
		}
		if (replacementStr == null) {
			throw new IllegalArgumentException("ReplacementStr null!");
		}
		//
		StringBuffer sb = new StringBuffer();
		int searchStringPos = str.indexOf(searchStr);
		int startPos = 0;
		int searchStringLength = searchStr.length();
		//
		while (searchStringPos != -1) {
			sb.append(
				str.substring(startPos, searchStringPos)).append(
					replacementStr);
			startPos = searchStringPos + searchStringLength;
			searchStringPos = str.indexOf(searchStr, startPos);
		}
		//
		sb.append(str.substring(startPos, str.length()));
		//
		return sb.toString();
	}
	
	/**
	 * <p>
	 * Checks whether the objects are equals. In case both are <code>null</code>,
	 * <code>true</code> is returned.
	 * </p>
	 * @param obj1 Object 1.
	 * @param obj2 Object 2.
	 * @return Equal (true).
	 */
	public static boolean areEqual(Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null) {
			return true;
		} else if (obj1 == null || obj2 == null) {
			return false;
		} else {
			return obj1.equals(obj2);
		}
	}
	
	/**
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private Util() {
	}
}