/* MultipartBody.java
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
package com.emobtech.networkingme.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.emobtech.networkingme.Payload;
import com.emobtech.networkingme.util.Util;

public final class MultipartBody implements Payload {
	
	private ByteArrayOutputStream body = new ByteArrayOutputStream(1024);

	public String getType() {
		return "multipart/form-data; boundary=" + getBoundary();
	}

	public long getLength() {
		return body.size();
	}

	public byte[] getBytes() {
		return body.toByteArray();
	}
	
	public void addPart(String name, String value) {
		if (Util.isEmptyString(name) || Util.isEmptyString(value)) {
			throw new IllegalArgumentException("Name/Value null or empty!");
		}
		//
		final String header = createHeader(name, null, null);
		//
		try {
			body.write(Util.toBytesString(header));
			body.write(Util.toBytesString(value));
			body.write(Util.toBytesString("\n--" + getBoundary() + "--"));
		} catch (IOException e) {
			throw new IllegalStateException("Error by writing part!");
		}
	}
	
	public void addPart(String name, String filename, String contentType, 
		byte[] data) {
		if (Util.isEmptyString(name) 
				|| Util.isEmptyString(filename) 
				|| Util.isEmptyString(contentType) 
				|| data == null 
				|| data.length == 0) {
			throw new IllegalArgumentException(
				"Name/Filename/Content Type/Data null or empty!");
		}
		final String header = createHeader(name, filename, contentType);
		//
		try {
			body.write(Util.toBytesString(header.toString()));
			body.write(data);
			body.write(Util.toBytesString("\n--" + getBoundary() + "--"));
		} catch (IOException e) {
			throw new IllegalStateException("Error by writing part!");
		}
	}

	public void addPart(String name, String filename, String contentType,
		InputStream data) throws IOException {
		addPart(name, filename, contentType, Util.readBytes(data));
	}

	private String getBoundary() {
		return "----------NetworkingMEBoundary_" + String.valueOf(hashCode());
	}
	
	private String createHeader(String name, String filename, 
		String contentType) {
		StringBuffer header = new StringBuffer();
		//
		header.append('\n');
		header.append("--" + getBoundary());
		header.append('\n');
		header.append(HttpRequest.Header.CONTENT_DISPOSITION + ": form-data; ");
		header.append("name=\"" + name + '\"');
		if (filename != null) {
			header.append("; filename=\"" + filename + "\"");
		}
		header.append('\n');
		if (contentType != null) {
			header.append(HttpRequest.Header.CONTENT_TYPE + ": " + contentType);
			header.append('\n');
		}
		header.append('\n');
		//
		return header.toString();
	}
}
