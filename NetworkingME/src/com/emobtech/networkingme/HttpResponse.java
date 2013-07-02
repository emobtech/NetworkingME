package com.emobtech.networkingme;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;

public final class HttpResponse extends Response {
	
	private int code;
	private byte[] buffer;
	private Hashtable headers;
	
	HttpResponse(HttpConnection conn) throws IOException {
		code = conn.getResponseCode();
		buffer = Util.readBytes(conn.openInputStream());
		headers = readHeaders(conn);
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
			return null;
		}
	}

	public long getLength() {
		return buffer.length;
	}

	public String getType() {
		return getHeader(HttpRequest.Header.CONTENT_TYPE);
	}
	
	public String getHeader(String key) {
		return (String)headers.get(key.toLowerCase());
	}
	
	private Hashtable readHeaders(HttpConnection conn) throws IOException {
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
