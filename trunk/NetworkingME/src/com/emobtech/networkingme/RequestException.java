/* RequestException.java
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

public final class RequestException extends Exception {
	
	private Exception cause;
	private Response response;
	
	RequestException(Exception cause) {
		if (cause == null) {
			throw new IllegalArgumentException("Cause null!");
		}
		//
		this.cause = cause;
	}

	RequestException(Response response) {
		if (response == null) {
			throw new IllegalArgumentException("Response null!");
		}
		//
		this.response = response;
	}
	
	public int getCode() {
		if (cause != null) {
			return -1;
		} else {
			return response.getCode();
		}
	}
	
	public Exception getCause() {
		return cause;
	}
	
	public Response getResponse() {
		return response;
	}

	public String getMessage() {
		if (cause != null) {
			return cause.getMessage();
		} else {
			return response.getString();
		}
	}

	public void printStackTrace() {
		if (cause != null) {
			cause.printStackTrace();
		} else {
			System.out.println(response.getString());
		}
	}

	public String toString() {
		return getMessage();
	}
}
