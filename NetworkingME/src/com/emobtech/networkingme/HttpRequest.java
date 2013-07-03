/* HttpRequest.java
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
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public final class HttpRequest extends Request {
	
	public static interface Method {
		String GET = "GET";
		String POST = "POST";
		String HEAD = "HEAD";
	}
	
	public static interface Code {
		int OK = 200;
		//
		int MULTIPLE_CHOICES = 300;
		int MOVED_PERMANENTLY = 301;
		int FOUND = 302;
		int SEE_OTHER = 303;
		int TEMPORARY_REDIRECT = 307;
		int PERMANENT_REDIRECT = 308;
		//
		int BAD_REQUEST = 400;
		int UNAUTHORIZED = 401;
		int FORBIDDEN = 403;
		int NOT_FOUND = 404;
		int NOT_ACCEPTABLE = 406;
		int REQUEST_TIMEOUT = 408;
		int TOO_MANY_REQUESTS = 429;
		//
		int INTERNAL_ERROR = 500;
		int NOT_IMPLEMENTED = 501;
		int BAD_GATEWAY = 502;
		int SERVICE_UNAVAILABLE = 503;
	}
	
	public static interface Header {
		String CONTENT_LENGTH = "Content-Length";
		String CONTENT_TYPE = "Content-Type";
		String LOCATION = "Location";
	}

	private String method;
	private Hashtable headers;
	private Body body;

	public HttpRequest(URL url) {
		this(url, Method.GET);
	}

	public HttpRequest(URL url, String method) {
		super(url);
		//
		checkMethod(method);
		//
		this.method = method;
	}
	
	public void setHeader(String key, String value) {
		if (headers == null) {
			headers = new Hashtable();
		}
		//
		headers.put(key, value);
	}
	
	public void setBody(Body body) {
		if (!Method.POST.equals(method)) {
			throw new IllegalStateException("Request's method must be POST!");
		}
		//
		this.body = body;
	}
	
	Response send() throws IOException {
		final String url = getURL().toEncodedString();
		//
		HttpConnection conn =
			(HttpConnection)Connector.open(url, Connector.READ_WRITE);
		//
		conn.setRequestMethod(method);
		//
		attachHeader(conn);
		attachBody(conn);
		//
		try {
			HttpResponse response = new HttpResponse(conn);
			//
			return response;
		} finally {
			conn.close();
		}
	}
	
	private void attachHeader(HttpConnection conn) throws IOException {
		if (headers != null && headers.size() > 0) {
			String key;
			Enumeration keys = headers.keys();
			//
			while (keys.hasMoreElements()) {
				key = (String)keys.nextElement();
				//
				conn.setRequestProperty(key, (String)headers.get(key));
			}
		}
	}
	
	private void attachBody(HttpConnection conn) throws IOException {
		if (Method.POST.equals(method) && body != null) {
			conn.setRequestProperty(Header.CONTENT_TYPE, body.getType());
			conn.setRequestProperty(
				Header.CONTENT_LENGTH, String.valueOf(body.getLength()));
			//
			OutputStream out = conn.openOutputStream();
			//
			try {
				out.write(body.getBytes());
				out.flush();
			} finally {
				out.close();
			}
		}
	}
	
	private void checkMethod(String method) {
		if (!Method.GET.equals(method)
				&& !Method.POST.equals(method) 
				&& !Method.HEAD.equals(method)) {
			throw new IllegalArgumentException("Method invalid!");
		}
	}
}
