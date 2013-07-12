/* HttpResponse.java
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
package com.emobtech.networkingme.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;

import com.emobtech.networkingme.Payload;
import com.emobtech.networkingme.Response;
import com.emobtech.networkingme.URL;
import com.emobtech.networkingme.util.Util;

/**
 * <p>
 * This class represents a HTTP response.
 * </p>
 * @author Ernandes Jr. (ernandes@emobtech.com)
 * @version 1.0
 * @since 1.0
 */
public final class HttpResponse extends Response {
	/**
	 * <p>
	 * Code.
	 * </p>
	 */
	private int code;

	/**
	 * <p>
	 * Payload.
	 * </p>
	 */
	private byte[] payload;
	
	/**
	 * <p>
	 * Header.
	 * </p>
	 */
	private Hashtable header;
	
	/**
	 * <p>
	 * Creates a HttpResponse from a given connection.
	 * </p>
	 * @param conn Connection.
	 * @throws IOException If any I/O error occurs.
	 */
	HttpResponse(HttpConnection conn) throws IOException {
		code = conn.getResponseCode();
		payload = readPayload(conn);
		header = readHeader(conn);
	}

	/**
	 * @see com.emobtech.networkingme.Response#wasSuccessful()
	 */
	public boolean wasSuccessful() {
		return code >= HttpRequest.Code.OK
			&& code < HttpRequest.Code.BAD_REQUEST;
	}
	
	/**
	 * @see com.emobtech.networkingme.Response#getCode()
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * @see com.emobtech.networkingme.Response#getPayload()
	 */
	public Payload getPayload() {
		return new Payload() {
			public String getType() {
				return getHeader(HttpRequest.Header.CONTENT_TYPE);
			}
			public long getLength() {
				return payload.length;
			}
			public byte[] getBytes() {
				return payload;
			}
		};
	}

	/**
	 * <p>
	 * Returns the payload's content as string.
	 * </p>
	 * @return Payload.
	 */
	public String getPayloadAsString() {
		return Util.toString(payload);
	}
	
	/**
	 * <p>
	 * Returns the value of a given field.
	 * </p>
	 * @param key Field key.
	 * @return Value.
	 * @see HttpRequest.Header
	 */
	public String getHeader(String key) {
		return (String)header.get(key.toLowerCase());
	}
	
	/**
	 * <p>
	 * Returns whether the request should redirect to a different location.
	 * </p>
	 * @return Should redirect (true).
	 * @see HttpResponse#getRedirectURL()
	 */
	public boolean wasRedirected() {
		return code >= HttpRequest.Code.MULTIPLE_CHOICES
			&& code < HttpRequest.Code.BAD_REQUEST;
	}

	/**
	 * <p>
	 * Returns the URL which the request must be redirected to. <code>null</code>
	 * is returned is case redirect was not necessary.
	 * </p>
	 * @return Redirect URL.
	 * @see HttpResponse#getHeader(String)
	 * @see HttpRequest.Header#LOCATION
	 */
	public URL getRedirectURL() {
		String location = getHeader(HttpRequest.Header.LOCATION);
		//
		return location != null ? new URL(location) : null;
	}
	
	/**
	 * <p>
	 * Returns the cookies.
	 * </p>
	 * @return Cookies.
	 */
	public Cookie[] getCookies() {
		String cookie = getHeader(HttpRequest.Header.SET_COOKIE);
		//
		if (Util.isEmptyString(cookie)) {
			return new Cookie[0];
		} else {
			String[] cookiesStr = Util.splitString(cookie, '|');
			Cookie[] cookies = new Cookie[cookiesStr.length];
			//
			for (int i = 0; i < cookiesStr.length; i++) {
				cookies[i] = new Cookie(cookiesStr[i]);
			}
			//
			return cookies;
		}
	}
	
	/**
	 * <p>
	 * Reads the payload from a given connection.
	 * </p>
	 * @param conn Connection.
	 * @return Payload.
	 * @throws IOException If any I/O error occurs.
	 */
	private byte[] readPayload(HttpConnection conn) throws IOException {
		InputStream in = conn.openInputStream();
		//
		try {
			return Util.readBytes(in);
		} finally {
			in.close();
		}
	}

	/**
	 * <p>
	 * Reads the header fields from a given connection.
	 * </p>
	 * @param conn Connection.
	 * @return Header fields.
	 * @throws IOException If any I/O error occurs.
	 */
	private Hashtable readHeader(HttpConnection conn) throws IOException {
		final int END = 100;
		final String SET_COOKIE = HttpRequest.Header.SET_COOKIE.toLowerCase();
		//
		String key;
		String value;
		Hashtable headers = new Hashtable(10);
		//
		for (int i = 0; i < END; i++) {
			key = conn.getHeaderFieldKey(i);
			//
			if (key != null) {
				key = key.toLowerCase();
				value = (String)headers.get(key);
				//
				if (value != null) {
					if (SET_COOKIE.equals(key)) {
						value += '|' + conn.getHeaderField(i);
					} else {
						value += ',' + conn.getHeaderField(i);
					}
				} else {
					value = conn.getHeaderField(i);
				}
				//
				headers.put(key, value);
			} else {
				break;
			}
		}
		//
		return headers;
	}
}
