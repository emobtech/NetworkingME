package com.emobtech.networkingme;

import java.io.UnsupportedEncodingException;

public final class WebFormBody extends Body {
	
	private StringBuffer fields;
	private byte[] body;
	
	public String getType() {
		return "application/x-www-form-urlencoded";
	}

	public long getLength() {
		process();
		//
		return body.length;
	}

	public byte[] getBytes() {
		process();
		//
		return body;
	}
	
	public void addField(String name, String value) {
		if (Util.isEmptyString(name) || Util.isEmptyString(value)) {
			throw new IllegalArgumentException("Name/Value null or empty!");
		}
		//
		if (fields == null) {
			fields = new StringBuffer();
		}
		//
		if (fields.length() > 0) {
			fields.append('&');
		}
		//
		fields.append(Util.encodeString(name));
		fields.append('=');
		fields.append(Util.encodeString(value));
		//
		body = null;
	}

	private void process() {
		if (body == null && fields != null && fields.length() > 0) {
			try {
				body = fields.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				body = fields.toString().getBytes();
			}
		}
	}
}
