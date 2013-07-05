/* RequestOperation.java
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

public final class RequestOperation implements Runnable {
	
	public static interface Listener {
		void onSuccess(Request request, Response response);
		void onComplete(Request request, Response response);
		void onFailure(Request request, RequestException exception);
	}
	
	public static abstract class ContentListener implements Listener {
		public abstract void onString(String string);
		public abstract void onBytes(byte[] bytes);
		
		public void onSuccess(Request request, Response response) {
			onBytes(response.getBytes());
			onString(response.getString());
		}
		
		public void onComplete(Request request, Response response) {}
	}

	private Request request;
	private Listener listener;
	
	public RequestOperation(Request request) {
		if (request == null) {
			throw new IllegalArgumentException("Request is null!");
		}
		//
		this.request = request;
	}
	
	public void perform(Listener listener) {
		this.listener = listener;
		//
		ThreadDispatcher.getInstance().dispatch(this);
	}

	public void run() {
		try {
			Response response = request.send();
			//
			if (listener != null) {
				listener.onComplete(request, response);
				//
				if (response.wasSuccessfull()) {
					listener.onSuccess(request, response);
				} else {
					listener.onFailure(request, new RequestException(response));
				}
			}
		} catch (Exception e) {
			if (listener != null) {
				listener.onFailure(request, new RequestException(e));
			}
		}
	}
}
