/* MultipartBody.java
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.emobtech.networkingme.Payload;
import com.emobtech.networkingme.util.Util;

/**
 * <p>
 * This class represents a multipart body. It is used in a POST HTTP request 
 * to send either text or binary data. Mix of text and binary is also supported.
 * </p>
 * <p>
 * Reference: <br />
 * <a href="http://en.wikipedia.org/wiki/Multipart/form-data#Multipart_messages" target="_blank">
 *     http://en.wikipedia.org/wiki/Multipart/form-data#Multipart_messages
 * </a>
 * </p>
 * @author Ernandes Jr. (ernandes@emobtech.com)
 * @version 1.0
 * @since 1.0
 */
public final class MultipartBody implements Payload {
	/**
	 * <p>
	 * Payload.
	 * </p>
	 */
	private ByteArrayOutputStream payload = new ByteArrayOutputStream(1024);

	/**
	 * @see com.emobtech.networkingme.Payload#getType()
	 */
	public String getType() {
		return "multipart/form-data; boundary=" + getBoundary();
	}

	/**
	 * @see com.emobtech.networkingme.Payload#getLength()
	 */
	public long getLength() {
		return payload.size();
	}

	/**
	 * @see com.emobtech.networkingme.Payload#getBytes()
	 */
	public byte[] getBytes() {
		return payload.toByteArray();
	}
	
	/**
	 * <p>
	 * Adds a part to carry a name=value content.
	 * </p>
	 * @param name Name.
	 * @param value Value.
	 * @throws IllegalArgumentException Name/Value null or empty!
	 */
	public void addPart(String name, String value) {
		if (Util.isEmptyString(name) || Util.isEmptyString(value)) {
			throw new IllegalArgumentException("Name/Value null or empty!");
		}
		//
		final String header = createHeader(name, null, null);
		//
		try {
			payload.write(Util.toBytesString(header));
			payload.write(Util.toBytesString(value));
			payload.write(Util.toBytesString("\n--" + getBoundary() + "--"));
		} catch (IOException e) {
			throw new IllegalStateException("Error by writing part!");
		}
	}
	
	/**
	 * <p>
	 * Adds a part to carry a binary content.
	 * </p>
	 * @param name Name.
	 * @param filename Filename.
	 * @param contentType Content type, e.g. "image/png".
	 * @param data Data.
	 * @throws IllegalArgumentException Name, filename, contentType and data 
	 *         null or empty!
	 */
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
			payload.write(Util.toBytesString(header.toString()));
			payload.write(data);
			payload.write(Util.toBytesString("\n--" + getBoundary() + "--"));
		} catch (IOException e) {
			throw new IllegalStateException("Error by writing part!");
		}
	}

	/**
	 * <p>
	 * Adds a part to carry a binary content.
	 * </p>
	 * @param name Name.
	 * @param filename Filename.
	 * @param contentType Content type, e.g. "image/png".
	 * @param data Data.
	 * @throws IllegalArgumentException Name, filename, contentType and data 
	 *         null or empty!
	 * @throws IOException If any I/O error occurs.
	 */
	public void addPart(String name, String filename, String contentType,
		InputStream data) throws IOException {
		addPart(name, filename, contentType, Util.readBytes(data));
	}

	/**
	 * <p>
	 * Returns the part boundary.
	 * </p>
	 * @return Boundary.
	 */
	private String getBoundary() {
		return "----------NetworkingMEBoundary_" + String.valueOf(hashCode());
	}
	
	/**
	 * <p>
	 * Creates a part header.
	 * </p>
	 * @param name Name.
	 * @param filename Filename.
	 * @param contentType Content type.
	 * @return Header.
	 */
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
