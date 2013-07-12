/* HttpRequest.java
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

/**
 * <p>
 * This class represents a HTTP request.
 * </p>
 * @author Ernandes Jr. (ernandes@emobtech.com)
 * @version 1.0
 * @since 1.0
 */
public final class HttpRequest extends Request {
	/**
	 * <p>
	 * This class defines a list with all HTTP methods supported.
	 * </p>
	 * <p>
	 * Reference: <br />
	 * <a href="https://en.wikipedia.org/wiki/HTTP_method#Request_methods" target="_blank">
	 *     https://en.wikipedia.org/wiki/HTTP_method#Request_methods
	 * </a>
	 * </p>
	 */
	public static final class Method {
		/**
		 * <p>GET.</p>
		 */
		public static final String GET = HttpConnection.GET;

		/**
		 * <p>POST.</p>
		 */
		public static final String POST = HttpConnection.POST;

		/**
		 * <p>HEAD.</p>
		 */
		public static final String HEAD = HttpConnection.HEAD;
		
		/**
		 * <p>
		 * Private constructor to avoid object instantiation.
		 * </p>
		 */
		private Method() {}
	}
	
	/**
	 * <p>
	 * This class defines a list of HTTP status code.
	 * </p>
	 * <p>
	 * Reference: <br />
	 * <a href="https://en.wikipedia.org/wiki/HTTP_response_codes" target="_blank">
	 *     https://en.wikipedia.org/wiki/HTTP_response_codes
	 * </a>
	 * </p>
	 */
	public static final class Code {
		/**
		 * <p>OK.</p>
		 */
		public static final int OK = 200;

		/**
		 * <p>Multiple Choices.</p>
		 */
		public static final int MULTIPLE_CHOICES = 300;
		
		/**
		 * <p>Moved Permanently.</p>
		 */
		public static final int MOVED_PERMANENTLY = 301;

		/**
		 * <p>FOUND.</p>
		 */
		public static final int FOUND = 302;
		
		/**
		 * <p>See Other.</p>
		 */
		public static final int SEE_OTHER = 303;

		/**
		 * <p>Temporary Redirect.</p>
		 */
		public static final int TEMPORARY_REDIRECT = 307;
		
		/**
		 * <p>Permanent Redirect.</p>
		 */
		public static final int PERMANENT_REDIRECT = 308;
		
		/**
		 * <p>Bad Request.</p>
		 */
		public static final int BAD_REQUEST = 400;

		/**
		 * <p>Unauthorized.</p>
		 */
		public static final int UNAUTHORIZED = 401;

		/**
		 * <p>Forbidden.</p>
		 */
		public static final int FORBIDDEN = 403;
		
		/**
		 * <p>Not Found.</p>
		 */
		public static final int NOT_FOUND = 404;
		
		/**
		 * <p>Not Acceptable.</p>
		 */
		public static final int NOT_ACCEPTABLE = 406;
		
		/**
		 * <p>Request Timeout.</p>
		 */
		public static final int REQUEST_TIMEOUT = 408;
		
		/**
		 * <p>Too Many Requests.</p>
		 */
		public static final int TOO_MANY_REQUESTS = 429;
		
		/**
		 * <p>Internal Error.</p>
		 */
		public static final int INTERNAL_ERROR = 500;

		/**
		 * <p>Not Implemented.</p>
		 */
		public static final int NOT_IMPLEMENTED = 501;
		
		/**
		 * <p>Bad Gateway.</p>
		 */
		public static final int BAD_GATEWAY = 502;
		
		/**
		 * <p>Service Unavailable.</p>
		 */
		public static final int SERVICE_UNAVAILABLE = 503;
		
		/**
		 * <p>
		 * Private constructor to avoid object instantiation.
		 * </p>
		 */
		private Code() {}
	}
	
	/**
	 * <p>
	 * This class defines a list of HTTP header field.
	 * </p>
	 * <p>
	 * Reference: <br />
	 * <a href="https://en.wikipedia.org/wiki/List_of_HTTP_header_fields" target="_blank">
	 *     https://en.wikipedia.org/wiki/List_of_HTTP_header_fields
	 * </a>
	 * </p>
	 */
	public static final class Header {
		/**
		 * <p>Accept.</p>
		 */
		public static final String ACCEPT = "Accept";

		/**
		 * <p>Accept Charset.</p>
		 */
		public static final String ACCEPT_CHARSET = "Accept-Charset";
		
		/**
		 * <p>Accept Encoding.</p>
		 */
		public static final String ACCEPT_ENCODING = "Accept-Encoding";
		
		/**
		 * <p>Accept Language.</p>
		 */
		public static final String ACCEPT_LANGUAGE = "Accept-Language";
		
		/**
		 * <p>Authorization.</p>
		 */
		public static final String AUTHORIZATION = "Authorization";
		
		/**
		 * <p>Cache Control.</p>
		 */
		public static final String CACHE_CONTROL = "Cache-Control";
		
		/**
		 * <p>Content Length.</p>
		 */
		public static final String CONTENT_LENGTH = "Content-Length";
		
		/**
		 * <p>Content Type.</p>
		 */
		public static final String CONTENT_TYPE = "Content-Type";
		
		/**
		 * <p>Cookie.</p>
		 */
		public static final String COOKIE = "Cookie";
		
		/**
		 * <p>Location.</p>
		 */
		public static final String LOCATION = "Location";
		
		/**
		 * <p>Set Cookie.</p>
		 */
		public static final String SET_COOKIE = "Set-Cookie";
		
		/**
		 * <p>User Agent.</p>
		 */
		public static final String USER_AGENT = "User-Agent";
		
		/**
		 * <p>Content Disposition.</p>
		 */
		public static final String CONTENT_DISPOSITION = "Content-Disposition";
	}

	/**
	 * <p>
	 * URL.
	 * </p>
	 */
	private URL url;

	/**
	 * <p>
	 * Method.
	 * </p>
	 */
	private String method;
	
	/**
	 * <p>
	 * Header.
	 * </p>
	 */
	private Hashtable header;
	
	/**
	 * <p>
	 * Body.
	 * </p>
	 */
	private Payload body;

	/**
	 * <p>
	 * Creates a HttpRequest for a given URL.
	 * </p>
	 * @param url URL.
	 * @throws IllegalArgumentException URL null!
	 */
	public HttpRequest(URL url) {
		this(url, Method.GET);
	}

	/**
	 * <p>
	 * Creates a HttpRequest for a given URL and method.
	 * </p>
	 * @param url URL.
	 * @param method Method.
	 * @throws IllegalArgumentException URL or Method null!
	 * @see Method#GET
	 * @see Method#POST
	 * @see Method#HEAD
	 */
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
	
	/**
	 * <p>
	 * Returns the URL.
	 * </p>
	 * @return URL.
	 */
	public URL getURL() {
		return url;
	}
	
	/**
	 * <p>
	 * Returns the method.
	 * </p>
	 * @return Method.
	 */
	public String getMethod() {
		return method;
	}
	
	/**
	 * <p>
	 * Sets a value to a given header field.
	 * </p>
	 * <p>
	 * Multiple values for a single field is supported. Just set different 
	 * values to the same field.
	 * </p>
	 * @param key Field key.
	 * @param value Value.
	 * @throws IllegalArgumentException Key/Value null or empty!
	 * @see Header
	 */
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
		if (headerValue != null && !headerValue.equals(value)) {
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
	
	/**
	 * <p>
	 * Adds a cookie.
	 * </p>
	 * @param cookie Cookie.
	 * @throws IllegalArgumentException Cookie null!
	 */
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
	
	/**
	 * <p>
	 * Returns the body.
	 * </p>
	 * @return Body.
	 */
	public Payload getBody() {
		return body;
	}

	/**
	 * <p>
	 * Sets the body of a POST request.
	 * </p>
	 * @param body Body.
	 * @throws IllegalArgumentException Request's method must be POST!
	 * @see WebFormBody
	 * @see MultipartBody
	 */
	public void setBody(Payload body) {
		if (body != null && !Method.POST.equals(method)) {
			throw new IllegalStateException("Request's method must be POST!");
		}
		//
		this.body = body;
	}
	
	/**
	 * @see com.emobtech.networkingme.Request#send()
	 */
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
	
	/**
	 * <p>
	 * Writes all header fields to the connection.
	 * </p>
	 * @param conn Connection.
	 * @throws IOException If any I/O error occurs.
	 */
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
	
	/**
	 * <p>
	 * Writes the body to the connection.
	 * </p>
	 * @param conn Connection.
	 * @throws IOException If any I/O error occurs.
	 */
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
	
	/**
	 * <p>
	 * Checks whether the given method is supported.
	 * </p>
	 * @param method Method.
	 * @throws IllegalArgumentException Method invalid!
	 */
	private void checkMethod(String method) {
		if (!Method.GET.equals(method)
				&& !Method.POST.equals(method) 
				&& !Method.HEAD.equals(method)) {
			throw new IllegalArgumentException("Method invalid!");
		}
	}
}
