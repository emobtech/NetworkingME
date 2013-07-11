/* BinaryListener.java
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

import com.emobtech.networkingme.lcdui.ImageListener;

/**
 * <p>
 * This class represents a listener to request events, which provide a utility 
 * method to obtain the binary content easily, result of a successful response.
 * </p>
 * @author Ernandes Jr. (ernandes@emobtech.com)
 * @version 1.0
 * @since 1.0
 * @see ImageListener
 */
public abstract class BinaryListener implements RequestOperation.Listener {
	/**
	 * <p>
	 * Called when the request is concluded successfully.
	 * </p>
	 * @param data Response content as bytes.
	 */
	public abstract void onBinary(byte[] data);
	
	/**
	 * @see com.emobtech.networkingme.RequestOperation.Listener#onSuccess(com.emobtech.networkingme.Request, com.emobtech.networkingme.Response)
	 */
	public final void onSuccess(Request request, Response response) {
		try {
			onBinary(response.getPayload().getBytes());
		} catch (Exception e) {
			onFailure(request, new RequestException(e));
		}
	}
	
	/**
	 * @see com.emobtech.networkingme.RequestOperation.Listener#onComplete(com.emobtech.networkingme.Request, com.emobtech.networkingme.Response)
	 */
	public final void onComplete(Request request, Response response) {}
}
