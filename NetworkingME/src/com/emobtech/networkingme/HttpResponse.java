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
