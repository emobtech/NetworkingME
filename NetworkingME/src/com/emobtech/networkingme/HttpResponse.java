package com.emobtech.networkingme;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.microedition.io.HttpConnection;

public final class HttpResponse extends Response {
	
	private int code;
	private byte[] buffer;
	private String type;
	
	HttpResponse(HttpConnection conn) throws IOException {
		code = conn.getResponseCode();
		buffer = readBytes(conn.openInputStream());
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

	public long getLength() {
		return buffer.length;
	}

	public String getType() {
		return type;
	}
	
	public String getString() {
		try {
			return new String(buffer, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	private byte[] readBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];
		//
		for (int n; (n = in.read(buffer)) > 0;) {
			out.write(buffer, 0, n);
		}
		//
		return out.toByteArray();
	}
}
