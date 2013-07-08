/* Util.java
 * 
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
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.Vector;

public final class Util {
	public static final String UTF8 = "UTF-8";
	public static final String USER_AGENT = "NetworkingME/1.0 (JavaME; Profile/MIDP-2.0 Configuration/CLDC-1.0)";
	
	/**
     * <p>
     * Split a string based on a given delimiter.
     * </p>
     * @param str String.
     * @param delimiter Delimiter.
     * @return String tokens.
     * @throws IllegalArgumentException If str is null.
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
	 * Verify whether the given string is null or empty.
	 * </p>
	 * @param str The string.
	 * @return true null/empty.
	 */
	public static boolean isEmptyString(String str) {
		return str == null || str.trim().length() == 0;
	}
	
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
	
	public static String encodeStringURL(String str) {
		if (str.indexOf('%') == -1) {
			return encodeStringURL(str, UTF8);
		} else {
			return str;
		}
	}
	
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
		}
		//
		return encodedQueryString.toString();
	}
	
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
	
	public static String formatCookie(String name, String value) {
		return name + '=' + value;
	}
	
	//Wed, 13 Jan 2021 22:23:01 GMT
	public static Date parseCookieDate(String date) {
		if (isEmptyString(date)) {
			return null;
		}
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
	 * Get a hex value of a char.
	 * </p>
	 * @param c Char.
	 */
	public static String getHexChar(int c) {
		return (c < 16 ? "%0" : "%") + Integer.toHexString(c).toUpperCase();
	}
	
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
	
	public static String getSystemProperty(String key, String defaultValue) {
		String value = System.getProperty(key);
		//
		return isEmptyString(value) ? defaultValue : value;
	}
	
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
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private Util() {
	}
}