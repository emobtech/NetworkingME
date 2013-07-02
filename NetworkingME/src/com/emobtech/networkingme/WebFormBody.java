package com.emobtech.networkingme;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

public final class WebFormBody extends Body {
	
	private Vector parameters;
	private byte[] content;
	
	public void add(String key, String value) {
		if (Util.isEmptyString(key) || Util.isEmptyString(value)) {
			throw new IllegalArgumentException("Key/Value null or empty!");
		}
		//
		key = Util.encodeString(key);
		value = Util.encodeString(value);
		//
		final String keyValue = key + '=' + value;
		//
		if (parameters == null) {
			parameters = new Vector();
		}
		//
		parameters.addElement(keyValue);
		//
		content = null; //reprocess content.
	}

	public String getType() {
		return "application/x-www-form-urlencoded";
	}

	public long getLength() {
		processContent();
		//
		return content.length;
	}

	public byte[] getBytes() {
		processContent();
		//
		return content;
	}
	
	private void processContent() {
		if (content == null || content.length == 0) {
			StringBuffer buffer = new StringBuffer();
			//
			if (parameters != null && parameters.size() > 0) {
				for (int i = parameters.size() -1; i >= 0; i--) {
					buffer.append(parameters.elementAt(i));
					buffer.append('&');
				}
				//
				buffer.deleteCharAt(buffer.length() -1); //last '&' char.
				//
				try {
					content = buffer.toString().getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					content = new byte[0];
				}
			}
		}
	}
}
