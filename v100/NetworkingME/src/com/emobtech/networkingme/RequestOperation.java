/* RequestOperation.java
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

/**
 * <p>
 * This class implements an operation that is used to run a request.
 * </p>
 * @author Ernandes Jr. (ernandes@emobtech.com)
 * @version 1.0
 * @since 1.0
 */
public final class RequestOperation {
	/**
	 * <p>
	 * This interface represents a listener to events that are triggered during
	 * a request execution.
	 * </p>
	 */
	public static interface Listener {
		/**
		 * <p>
		 * Called when the request is concluded successfully.
		 * </p>
		 * @param request Request.
		 * @param response Response.
		 */
		void onSuccess(Request request, Response response);

		/**
		 * <p>
		 * Called when the request is concluded regardless of result.
		 * </p>
		 * @param request Request.
		 * @param response Response.
		 */
		void onComplete(Request request, Response response);

		/**
		 * <p>
		 * Called when the request fails because of either an exception or 
		 * unsuccessful result.
		 * </p>
		 * @param request Request.
		 * @param exception Exception.
		 */
		void onFailure(Request request, RequestException exception);
	}

	/**
	 * <p>
	 * Request.
	 * </p>
	 */
	private Request request;
	
	/**
	 * <p>
	 * Listener.
	 * </p>
	 */
	private Listener listener;
	
	/**
	 * <p>
	 * Creates a RequestOperation to run a given request.
	 * </p>
	 * @param request Request.
	 * @throws IllegalArgumentException Request is null!
	 */
	public RequestOperation(Request request) {
		if (request == null) {
			throw new IllegalArgumentException("Request is null!");
		}
		//
		this.request = request;
	}
	
	/**
	 * <p>
	 * Starts the run of the request.
	 * </p>
	 * @param listener Listener.
	 * @see TextListener
	 * @see BinaryListener
	 */
	public void start(Listener listener) {
		this.listener = listener;
		//
		dispath();
	}

	/**
	 * <p>
	 * Dispatches the request.
	 * </p>
	 */
	private void dispath() {
		ThreadDispatcher.getInstance().dispatch(new Runnable() {
			public void run() {
				try {
					Response response = request.send();
					//
					if (listener != null) {
						listener.onComplete(request, response);
						//
						if (response.wasSuccessful()) {
							listener.onSuccess(request, response);
						} else {
							listener.onFailure(
								request, new RequestException(response));
						}
					}
				} catch (Exception e) {
					if (listener != null) {
						listener.onFailure(request, new RequestException(e));
					}
				}
			}
		});
	}
}
