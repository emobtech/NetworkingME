/* Payload.java
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
package com.emobtech.networkingme;

import com.emobtech.networkingme.http.MultipartBody;
import com.emobtech.networkingme.http.WebFormBody;

/**
 * <p>
 * This class represents a payload.
 * </p>
 * <p>
 * Reference: <br />
 * <a href="http://en.wikipedia.org/wiki/Payload_(computing)" target="_blank">
 *     http://en.wikipedia.org/wiki/Payload_(computing)
 * </a>
 * </p>
 * @author Ernandes Jr. (ernandes@emobtech.com)
 * @version 1.0
 * @since 1.0
 * @see WebFormBody
 * @see MultipartBody
 */
public interface Payload {
	/**
	 * <p>
	 * Returns the type.
	 * </p>
	 * @return Type.
	 */
	String getType();
	
	/**
	 * <p>
	 * Returns the length.
	 * </p>
	 * @return Length.
	 */
	long getLength();
	
	/**
	 * <p>
	 * Returns the data.
	 * </p>
	 * @return Data.
	 */
	byte[] getBytes();
}
