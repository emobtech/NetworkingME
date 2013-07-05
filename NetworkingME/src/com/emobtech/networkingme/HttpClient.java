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

import java.util.Enumeration;
import java.util.Hashtable;

import com.emobtech.networkingme.RequestOperation.Listener;

public final class HttpClient {
	
	private URL baseURL;
	private Hashtable headers;
	private boolean autoRedirect = true;
	private boolean trackCookie = true;
	private Cookie[] cookies;
	
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
		if (Util.isEmptyString(userAgent)) {
			throw new IllegalArgumentException("UserAgent null or empty!");
		}
		//
		setHeader(HttpRequest.Header.USER_AGENT, userAgent);
	}
	
	public void setTrackCookie(boolean enabled) {
		trackCookie = enabled;
	}
	
	public void setAutoRedirect(boolean enabled) {
		autoRedirect = true;
	}
	
	public void setHeader(String key, String value) {
		if (Util.isEmptyString(key) || Util.isEmptyString(value)) {
			throw new IllegalArgumentException("Key/Value null or empty!");
		}
		//
		if (headers == null) {
			headers = new Hashtable();
		}
		//
		headers.put(key, value);
	}
	
	public void removeHeader(String key) {
		if (Util.isEmptyString(key)) {
			throw new IllegalArgumentException("Key null or empty!");
		}
		//
		if (headers != null) {
			headers.remove(key);
		}
	}
	
	public void setBasicAuthentication(String username, String password) {
		if (Util.isEmptyString(username) || Util.isEmptyString(password)) {
			throw new IllegalArgumentException(
				"Username/Password null or empty!");
		}
	}
	
	public void setTokenAuthentication(String token) {
		if (Util.isEmptyString(token)) {
			throw new IllegalArgumentException("Token null or empty!");
		}
	}

	public void clearHeaders() {
		if (headers != null) {
			headers.clear();
		}
	}
	
	public void get(String path, Listener listener) {
		get(path, null, listener);
	}

	public void get(String path, Hashtable parameters, Listener listener) {
		checkPath(path);
		//
		request(new HttpRequest(new URL(baseURL, path, parameters)), listener);
	}
	
	public void post(String path, Body body, Listener listener) {
		checkPath(path);
		//
		HttpRequest req =
			new HttpRequest(new URL(baseURL, path), HttpRequest.Method.POST);
		//
		req.setBody(body);
		//
		request(req, listener);
	}

	public void postForm(String path, Hashtable parameters, Listener listener) {
		post(path, new WebFormBody(parameters), listener);
	}

	public void head(String path, Listener listener) {
		head(path, null, listener);
	}
	
	public void head(String path, Hashtable parameters, Listener listener) {
		checkPath(path);
		//
		request(
			new HttpRequest(
				new URL(baseURL, path, parameters),
				HttpRequest.Method.HEAD),
			listener);
	}

	public void request(final HttpRequest request, final Listener listener) {
		writeHeaders(request);
		writeCookies(request);
		//
		new RequestOperation(request).execute(new RequestOperation.Listener() {
			public void onSuccess(Request request, Response response) {
				HttpResponse res = (HttpResponse)response;
				//
				if (res.wasRedirected() && autoRedirect) {
					HttpRequest req = (HttpRequest)request;
					URL redirectURL = res.getRedirectURL();
					//
					request(new HttpRequest(redirectURL, req), listener);
				} else {
					if (listener != null) {
						listener.onSuccess(request, response);
					}
				}
			}

			public void onComplete(Request request, Response response) {
				readCookies((HttpResponse)response);
				//
				if (listener != null) {
					listener.onComplete(request, response);
				}
			}

			public void onFailure(Request request, RequestException exception) {
				if (listener != null) {
					listener.onFailure(request, exception);
				}
			}
		});
	}
	
	private void writeHeaders(HttpRequest request) {
		if (headers != null && headers.size() > 0) {
			String key;
			Enumeration keys = headers.keys();
			//
			while (keys.hasMoreElements()) {
				key = (String)keys.nextElement();
				//
				request.setHeader(key, (String)headers.get(key));
			}
		}
	}
	
	private void writeCookies(HttpRequest request) {
		if (trackCookie && cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				request.addCookie(cookies[i]);
			}
		}
	}
	
	private void readCookies(HttpResponse response) {
		if (trackCookie) {
			cookies = response.getCookies();
		}
	}
	
	private void checkPath(String path) {
		if (Util.isEmptyString(path)) {
			throw new IllegalArgumentException("Path null or empty!");
		}
	}
}
