/* WebFormBody.java
 * 
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
package com.emobtech.networkingme;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

public final class WebFormBody extends Body {
	
	private StringBuffer fieldsStr;
	private byte[] body;
	
	public WebFormBody(Hashtable fields) {
		fieldsStr.append(Util.toQueryString(fields));
	}
	
	public WebFormBody() {
		fieldsStr = new StringBuffer();
	}
	
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
		if (fieldsStr.length() > 0) {
			fieldsStr.append('&');
		}
		//
		fieldsStr.append(Util.encodeStringURL(name));
		fieldsStr.append('=');
		fieldsStr.append(Util.encodeStringURL(value));
		//
		body = null;
	}

	private void process() {
		if (body == null && fieldsStr.length() > 0) {
			try {
				body = fieldsStr.toString().getBytes(Util.UTF8);
			} catch (UnsupportedEncodingException e) {
				body = fieldsStr.toString().getBytes();
			}
		}
	}
}
