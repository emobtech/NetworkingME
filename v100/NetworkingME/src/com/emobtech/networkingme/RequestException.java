/* RequestException.java
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

import com.emobtech.networkingme.util.Util;

/**
 * <p>
 * This class represents an exception that can be thrown during a request, 
 * because of either an exception or unsuccessful response.
 * </p>
 * @author Ernandes Jr. (ernandes@emobtech.com)
 * @version 1.0
 * @since 1.0
 */
public final class RequestException extends Exception {
	/**
	 * <p>
	 * Exception.
	 * </p> 
	 */
	private Exception cause;

	/**
	 * <p>
	 * Unsuccessful response.
	 * </p> 
	 */
	private Response response;
	
	/**
	 * <p>
	 * Creates a new RequestException with an exception as cause.
	 * </p>
	 * @param cause Exception.
	 * @throws IllegalArgumentException Cause null!
	 */
	public RequestException(Exception cause) {
		if (cause == null) {
			throw new IllegalArgumentException("Cause null!");
		}
		//
		this.cause = cause;
	}

	/**
	 * <p>
	 * Creates a new RequestException with an unsuccessful response as cause.
	 * </p>
	 * @param response Response.
	 * @throws IllegalArgumentException Response null!
	 */
	public RequestException(Response response) {
		if (response == null) {
			throw new IllegalArgumentException("Response null!");
		}
		//
		this.response = response;
	}
	
	/**
	 * <p>
	 * Returns code. -1 is always returned when an exception is the cause.
	 * </p>
	 * @return Code.
	 */
	public int getCode() {
		if (cause != null) {
			return -1;
		} else {
			return response.getCode();
		}
	}
	
	/**
	 * <p>
	 * Returns the exception cause.
	 * </p>
	 * @return Exception.
	 */
	public Exception getCause() {
		return cause;
	}
	
	/**
	 * <p>
	 * Returns the unsuccessful response.
	 * </p>
	 * @return Response.
	 */
	public Response getResponse() {
		return response;
	}

	/**
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		if (cause != null) {
			return cause.getMessage();
		} else {
			return Util.toString(response.getPayload().getBytes());
		}
	}

	/**
	 * @see java.lang.Throwable#printStackTrace()
	 */
	public void printStackTrace() {
		if (cause != null) {
			cause.printStackTrace();
		} else {
			System.out.println(
				Util.toString(response.getPayload().getBytes()));
		}
	}

	/**
	 * @see java.lang.Throwable#toString()
	 */
	public String toString() {
		return getMessage();
	}
}
