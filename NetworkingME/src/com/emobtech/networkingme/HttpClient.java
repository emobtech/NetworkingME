/* HttpClient.java
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

import java.util.Hashtable;

public final class HttpClient {
	
	public static interface Listener {
		public void onSuccess(HttpRequest request, HttpResponse response);
		public void onFailure(HttpRequest request, RequestException response);
	}
	
	public static abstract class ContentListener implements Listener {
		
		public abstract void onStringReceived(String string);
		public abstract void onBytesReceived(byte[] bytes);
		
		public void onSuccess(HttpRequest request, HttpResponse response) {
			onBytesReceived(response.getBytes());
			onStringReceived(response.getString());
		}
	}
	
	private URL baseURL;
	
	public HttpClient(URL baseURL) {
		if (baseURL == null) {
			throw new IllegalArgumentException("BaseURL null!");
		}
		//
		this.baseURL = baseURL;
	}
	
	public URL getBaseURL() {
		return baseURL;
	}
	
	public void setUserAgent(String userAgent) {
		setHeader(HttpRequest.Header.USER_AGENT, userAgent);
	}
	
	public void setHeader(String key, String value) {
	}
	
	public void removeHeader(String key) {
	}
	
	public void setBasicAuthentication(String username, String password) {
	}
	
	public void setTokenAuthentication(String token) {
	}

	public void clearHeaders() {
	}
	
	public void clearAuthentications() {
	}

	public void get(String path, Listener listener) {
	}
	
	public void get(String path, Hashtable parameters, Listener listener) {
	}

	public void post(String path, Listener listener) {
	}

	public void post(String path, Hashtable parameters, Listener listener) {
	}

	public void head(String path, Listener listener) {
	}
	
	public void request(HttpRequest request, Listener listener) {
	}
}
