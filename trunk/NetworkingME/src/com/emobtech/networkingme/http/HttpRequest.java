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
package com.emobtech.networkingme.http;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import com.emobtech.networkingme.Payload;
import com.emobtech.networkingme.Request;
import com.emobtech.networkingme.Response;
import com.emobtech.networkingme.URL;
import com.emobtech.networkingme.util.Util;

public final class HttpRequest extends Request {
	
	public static interface Method {
		String GET = HttpConnection.GET;
		String POST = HttpConnection.POST;
		String HEAD = HttpConnection.HEAD;
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
		String ACCEPT = "Accept";
		String ACCEPT_CHARSET = "Accept-Charset";
		String ACCEPT_ENCODING = "Accept-Encoding";
		String ACCEPT_LANGUAGE = "Accept-Language";
		String AUTHORIZATION = "Authorization";
		String CACHE_CONTROL = "Cache-Control";
		String CONTENT_LENGTH = "Content-Length";
		String CONTENT_TYPE = "Content-Type";
		String COOKIE = "Cookie";
		String LOCATION = "Location";
		String SET_COOKIE = "Set-Cookie";
		String USER_AGENT = "User-Agent";
		String CONTENT_DISPOSITION = "Content-Disposition";
	}

	private URL url;
	private String method;
	private Hashtable header;
	private Payload body;

	public HttpRequest(URL url) {
		this(url, Method.GET);
	}

	public HttpRequest(URL url, String method) {
		if (url == null) {
			throw new IllegalArgumentException("URL null!");
		}
		if (Util.isEmptyString(method)) {
			throw new IllegalArgumentException("Method null or empty!");
		}
		//
		checkMethod(method);
		//
		this.url = url;
		this.method = method;
	}
	
	public URL getURL() {
		return url;
	}
	
	public String getMethod() {
		return method;
	}
	
	public void setHeader(String key, String value) {
		if (Util.isEmptyString(key) || Util.isEmptyString(value)) {
			throw new IllegalArgumentException("Key/Value null or empty!");
		}
		//
		if (header == null) {
			header = new Hashtable();
		}
		//
		String headerValue = (String)header.get(key);
		//
		if (headerValue != null) {
			if (Header.COOKIE.toLowerCase().equals(key.toLowerCase())) {
				headerValue += ';' + value;
			} else {
				headerValue += ',' + value;
			}
		} else {
			headerValue = value;
		}
		//
		header.put(key, headerValue);
	}
	
	public void addCookie(Cookie cookie) {
		if (cookie == null) {
			throw new IllegalArgumentException("Cookie null!");
		}
		//
		final String cookieValue =
			Util.formatCookie(cookie.getName(), cookie.getValue());
		//
		setHeader(Header.COOKIE, cookieValue);
	}
	
	public Payload getBody() {
		return body;
	}

	public void setBody(Payload body) {
		if (!Method.POST.equals(method)) {
			throw new IllegalStateException("Request's method must be POST!");
		}
		//
		this.body = body;
	}
	
	protected Response send() throws IOException {
		final String url = getURL().toEncodedString();
		//
		HttpConnection conn =
			(HttpConnection)Connector.open(url, Connector.READ_WRITE, true);
		//
		conn.setRequestMethod(method);
		//
		writeHeader(conn);
		writeBody(conn);
		//
		try {
			HttpResponse response = new HttpResponse(conn);
			//
			return response;
		} finally {
			conn.close();
		}
	}
	
	private void writeHeader(HttpConnection conn) throws IOException {
		if (header != null && header.size() > 0) {
			String key;
			Enumeration keys = header.keys();
			//
			while (keys.hasMoreElements()) {
				key = (String)keys.nextElement();
				//
				conn.setRequestProperty(key, (String)header.get(key));
			}
		}
	}
	
	private void writeBody(HttpConnection conn) throws IOException {
		if (Method.POST.equals(method) && body != null) {
			conn.setRequestProperty(Header.CONTENT_TYPE, body.getType());
			conn.setRequestProperty(
				Header.CONTENT_LENGTH, String.valueOf(body.getLength()));
			//
			OutputStream out = conn.openDataOutputStream();
			//
			try {
				Util.writeBytes(body.getBytes(), out);
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
