package com.emobtech.networkingme;

import java.io.IOException;
import java.io.OutputStream;
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
	}
	
	public static interface Header {
		String CONTENT_LENGTH = "Content-Length";
		String CONTENT_TYPE = "Content-Type";
	}

	private String method;
	private Hashtable headers;

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
		headers.put(key, value);
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
		return new HttpResponse_(conn);
	}
	
	private void attachHeader(HttpConnection conn) throws IOException {
		
	}
	
	private void attachBody(HttpConnection conn) throws IOException {
		Body body = getBody();
		//
		if (Method.POST.equals(method) && body != null) {
			conn.setRequestProperty(Header.CONTENT_TYPE, body.getType());
			conn.setRequestProperty(
				Header.CONTENT_LENGTH, String.valueOf(body.getLength()));
			//
			OutputStream out = conn.openOutputStream();
			//
			try {
				out.write(body.getContent());
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
