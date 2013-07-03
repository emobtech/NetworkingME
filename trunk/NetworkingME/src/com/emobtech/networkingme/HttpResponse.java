/* HttpResponse.java
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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;

public final class HttpResponse extends Response {
	
	private int code;
	private byte[] buffer;
	private Hashtable header;
	
	HttpResponse(HttpConnection conn) throws IOException {
		code = conn.getResponseCode();
		buffer = readBody(conn);
		header = readHeader(conn);
	}

	public boolean wasSuccessfull() {
		return code >= HttpRequest.Code.OK
			&& code < HttpRequest.Code.BAD_REQUEST;
	}
	
	public int getCode() {
		return code;
	}

	public byte[] getBytes() {
		return buffer;
	}

	public String getString() {
		try {
			return new String(buffer, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return new String(buffer);
		}
	}

	public long getLength() {
		return buffer.length;
	}

	public String getType() {
		return getHeader(HttpRequest.Header.CONTENT_TYPE);
	}
	
	public String getHeader(String key) {
		return (String)header.get(key.toLowerCase());
	}
	
	public boolean wasRedirected() {
		return code >= HttpRequest.Code.MULTIPLE_CHOICES
			&& code < HttpRequest.Code.BAD_REQUEST;
	}

	public URL getRedirectURL() {
		String location = getHeader(HttpRequest.Header.LOCATION);
		//
		return location != null ? new URL(location) : null;
	}
	
	private byte[] readBody(HttpConnection conn) throws IOException {
		InputStream in = conn.openInputStream();
		//
		try {
			return Util.readBytes(in);
		} finally {
			in.close();
		}
	}

	private Hashtable readHeader(HttpConnection conn) throws IOException {
		final int END = 100;
		//
		String key;
		String value;
		Hashtable headers = new Hashtable(10);
		//
		for (int i = 0; i < END; i++) {
			key = conn.getHeaderFieldKey(i);
			//
			if (key != null) {
				value = (String)headers.get(key);
				//
				if (value != null) {
					value += ';' + conn.getHeaderField(i);
				} else {
					value = conn.getHeaderField(i);
				}
				//
				headers.put(key.toLowerCase(), value);
			} else {
				break;
			}
		}
		//
		return headers;
	}
}
