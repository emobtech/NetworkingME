package com.emobtech.networkingme;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.microedition.io.HttpConnection;

public final class HttpResponse extends Response {
	
	private int code;
	private byte[] buffer;
	private String type;
	
	HttpResponse(HttpConnection conn) throws IOException {
		code = conn.getResponseCode();
		buffer = Util.readBytes(conn.openInputStream());
		type = conn.getRequestProperty(HttpRequest.Header.CONTENT_TYPE);
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
		return type;
	}
}
