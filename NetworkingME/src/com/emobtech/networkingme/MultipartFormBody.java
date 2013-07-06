/* MultipartFormBody.java
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

import java.io.IOException;
import java.io.InputStream;

public final class MultipartFormBody implements Body {
	
	private byte[] body;
	private WebFormBody formBody = new WebFormBody();
	
	public MultipartFormBody(InputStream stream) throws IOException {
		this(Util.readBytes(stream));
	}
	
	public MultipartFormBody(byte[] data) {
		if (data == null) {
			throw new IllegalArgumentException("Data is null!");
		}
		//
		body = data;
	}

	public String getType() {
		return "multipart/form-data";
	}

	public long getLength() {
		return body.length + formBody.getLength();
	}

	public byte[] getBytes() {
		if (formBody.getLength() > 0) {
			byte[] webBody = formBody.getBytes();
			byte[] newBody = new byte[body.length + webBody.length];
			//
			System.arraycopy(body, 0, newBody, 0, body.length);
			System.arraycopy(webBody, 0, newBody, body.length, webBody.length);
			//
			return newBody;
		} else {
			return body;
		}
	}
	
	public void addField(String name, String value) {
		formBody.addField(name, value);
	}
}
