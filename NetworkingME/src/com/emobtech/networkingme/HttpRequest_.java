package com.emobtech.networkingme;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public final class HttpRequest_ extends Request {
	
	public static interface Method {
		String GET = "GET";
		String POST = "POST";
	}
	
	public static interface Code {
		int OK = 200;
		int FORBIDDEN = 403;
		int UNAVAILABLE = 503;
		int NOT_MODIFIED = 304;
		int BAD_REQUEST = 400;
		int UNAUTHORIZED = 401;
		int NOT_FOUND = 404;
		int NOT_ACCEPTABLE = 406;
		int INTERNAL_ERROR = 500;
		int BAD_GATEWAY = 502;
		int TOO_MANY_REQUESTS = 429;		
	}
	
	public static interface Header {
		String CONTENT_LENGTH = "Content-Length";
		String CONTENT_TYPE = "Content-Type";
	}

	private String method;
	private Hashtable headers;
	private Body body;

	public HttpRequest_(URL url) {
		this(url, Method.GET);
	}

	public HttpRequest_(URL url, String method) {
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
		this.body = body;
	}
	
	Response send() throws IOException {
		HttpConnection conn =
			(HttpConnection)Connector.open(getURL().toEncodedString());
		//
		conn.setRequestMethod(method);
		//
		attachHeader(conn);
		attachBody(conn);
		//
		try {
			HttpResponse_ response = new HttpResponse_(conn);
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
		if (!Method.GET.equals(method) && !Method.POST.equals(method)) {
			throw new IllegalArgumentException("Method invalid!");
		}
	}
}
