/* WebFormBody.java
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

import java.util.Hashtable;

import com.emobtech.networkingme.Payload;
import com.emobtech.networkingme.util.Util;

/**
 * <p>
 * This class represents a web form body. It is used in a POST HTTP request 
 * to send name-value pairs.
 * </p>
 * <p>
 * Reference: <br />
 * <a href="http://en.wikipedia.org/wiki/POST_(HTTP)" target="_blank">
 *     http://en.wikipedia.org/wiki/POST_(HTTP)
 * </a>
 * </p>
 * @author Ernandes Jr. (ernandes@emobtech.com)
 * @version 1.0
 * @since 1.0
 */
public final class WebFormBody implements Payload {
	/**
	 * <p>
	 * Fields.
	 * </p>
	 */
	private StringBuffer fieldsStr = new StringBuffer();
	
	/**
	 * <p>
	 * Payload.
	 * </p>
	 */
	private byte[] payload;
	
	/**
	 * <p>
	 * Creates a WebFormBody.
	 * </p>
	 */
	public WebFormBody() {
	}

	/**
	 * <p>
	 * Creates a WebFormBody.
	 * </p>
	 * @param fields Fields.
	 */
	public WebFormBody(Hashtable fields) {
		fieldsStr.append(Util.toQueryString(fields));
	}
	
	/**
	 * @see com.emobtech.networkingme.Payload#getType()
	 */
	public String getType() {
		return "application/x-www-form-urlencoded";
	}

	/**
	 * @see com.emobtech.networkingme.Payload#getLength()
	 */
	public long getLength() {
		process();
		//
		return payload.length;
	}

	/**
	 * @see com.emobtech.networkingme.Payload#getBytes()
	 */
	public byte[] getBytes() {
		process();
		//
		return payload;
	}
	
	/**
	 * <p>
	 * Adds a field.
	 * </p>
	 * @param name Name.
	 * @param value Value.
	 * @throws IllegalArgumentException Name/Value null or empty!
	 */
	public void addField(String name, String value) {
		if (Util.isEmptyString(name) || Util.isEmptyString(value)) {
			throw new IllegalArgumentException("Name/Value null or empty!");
		}
		//
		if (fieldsStr.length() > 0) {
			fieldsStr.append('&');
		}
		//
		fieldsStr.append(Util.encodeStringURL(name));
		fieldsStr.append('=');
		fieldsStr.append(Util.encodeStringURL(value));
		//
		payload = null;
	}

	/**
	 * <p>
	 * Process the payload.
	 * </p>
	 */
	private void process() {
		if (payload == null) {
			payload = Util.toBytes(fieldsStr.toString());
		}
	}
}
