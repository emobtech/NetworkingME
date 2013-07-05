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
package com.emobtech.networkingme;

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

final class Util {
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
	
	public static String encodeString(String str) {
		if (str.indexOf('%') == -1) {
			return encodeString(str, "UTF-8");
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
			encodedQueryString.append(encodeString(paramValue[0]));
			encodedQueryString.append('=');
			encodedQueryString.append(encodeString(paramValue[1]));
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
			queryStr.append(encodeString(key));
			queryStr.append('=');
			queryStr.append(encodeString((String)parameters.get(key)));
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
	
	public static String encodeString(String str, String encoding) {
		if (isEmptyString(str)) {
			return str;
		}
		//
		if (encoding == null) {
			encoding = System.getProperty("microedition.encoding");
		}
		if (encoding == null) {
			encoding = "UTF-8";
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
	
	/**
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private Util() {
	}
}