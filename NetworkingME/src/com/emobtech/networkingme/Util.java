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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    	if (str == null) {
    		throw new IllegalArgumentException("Str must not be null.");
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
		return URLEncoder.encode(str, "UTF-8");
	}
	
	/**
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private Util() {
	}
}