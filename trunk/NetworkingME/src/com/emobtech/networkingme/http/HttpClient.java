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
package com.emobtech.networkingme.http;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.emobtech.networkingme.Payload;
import com.emobtech.networkingme.Request;
import com.emobtech.networkingme.RequestException;
import com.emobtech.networkingme.RequestOperation;
import com.emobtech.networkingme.RequestOperation.Listener;
import com.emobtech.networkingme.Response;
import com.emobtech.networkingme.URL;
import com.emobtech.networkingme.http.HttpRequest.Header;
import com.emobtech.networkingme.util.Util;

public final class HttpClient {
	
	private URL baseURL;
	private Hashtable header;
	private boolean handleRedirect = true;
	private boolean trackCookie = true;
	private Vector cookies;
	
	public HttpClient(URL baseURL) {
		if (baseURL == null) {
			throw new IllegalArgumentException("BaseURL null!");
		}
		//
		this.baseURL = baseURL;
		//
		setDefaultHeaders();
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
	
	public void setHandleRedirect(boolean enabled) {
		handleRedirect = enabled;
	}
	
	public void setHeader(String key, String value) {
		if (Util.isEmptyString(key) || Util.isEmptyString(value)) {
			throw new IllegalArgumentException("Key/Value null or empty!");
		}
		//
		if (header == null) {
			header = new Hashtable();
		}
		//
		String headerValue = (String)header.get(key);
		//
		if (headerValue != null) {
			if (Header.COOKIE.toLowerCase().equals(key.toLowerCase())) {
				headerValue += ';' + value;
			} else {
				headerValue += ',' + value;
			}
		} else {
			headerValue = value;
		}
		//
		header.put(key, headerValue);
	}
	
	public void removeHeader(String key) {
		if (Util.isEmptyString(key)) {
			throw new IllegalArgumentException("Key null or empty!");
		}
		//
		if (header != null) {
			header.remove(key);
		}
	}
	
	public void setBasicAuthentication(String username, String password) {
		if (Util.isEmptyString(username) || Util.isEmptyString(password)) {
			throw new IllegalArgumentException(
				"Username/Password null or empty!");
		}
		//
		final String credential =
			Util.encodeStringBase64(username + ':' + password);
		//
		setHeader(HttpRequest.Header.AUTHORIZATION, "Basic " + credential);
	}

	public void clearHeader() {
		if (header != null) {
			header.clear();
		}
	}
	
	public void clearCookies() {
		if (cookies != null) {
			cookies.removeAllElements();
		}
	}
	
	public void get(String path, Listener listener) {
		get(path, null, listener);
	}

	public void get(String path, Hashtable parameters, Listener listener) {
		checkPath(path);
		//
		start(new HttpRequest(new URL(baseURL, path, parameters)), listener);
	}
	
	public void postForm(String path, Hashtable parameters, Listener listener) {
		post(path, new WebFormBody(parameters), listener);
	}
	
	public void postFile(String path, String name, String filename, 
		String contentType, byte[] data, Listener listener) {
		MultipartBody body = new MultipartBody();
		body.addPart(name, filename, contentType, data);
		//
		post(path, body, listener);
	}

	public void post(String path, Payload body, Listener listener) {
		checkPath(path);
		//
		HttpRequest req =
			new HttpRequest(new URL(baseURL, path), HttpRequest.Method.POST);
		//
		req.setBody(body);
		//
		start(req, listener);
	}

	public void head(String path, Listener listener) {
		head(path, null, listener);
	}
	
	public void head(String path, Hashtable parameters, Listener listener) {
		checkPath(path);
		//
		start(
			new HttpRequest(
				new URL(baseURL, path, parameters),
				HttpRequest.Method.HEAD),
			listener);
	}

	private void start(final HttpRequest request, final Listener listener) {
		writeHeader(request);
		writeCookies(request);
		//
		new RequestOperation(request).start(new RequestOperation.Listener() {
			public void onSuccess(Request request, Response response) {
				HttpResponse res = (HttpResponse)response;
				//
				if (res.wasRedirected() && handleRedirect) {
					HttpRequest req = (HttpRequest)request;
					//
					HttpRequest newRequest =
						new HttpRequest(res.getRedirectURL(), req.getMethod());
					newRequest.setBody(req.getBody());
					//
					start(newRequest, listener);
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
	
	private void writeHeader(HttpRequest request) {
		if (header != null && header.size() > 0) {
			String key;
			Enumeration keys = header.keys();
			//
			while (keys.hasMoreElements()) {
				key = (String)keys.nextElement();
				//
				request.setHeader(key, (String)header.get(key));
			}
		}
	}
	
	private void writeCookies(HttpRequest request) {
		if (trackCookie && cookies != null) {
			Cookie cookie;
			//
			for (int i = cookies.size() -1; i >= 0; i--) {
				cookie = (Cookie)cookies.elementAt(i);
				//
				if (!cookie.isExpired()) {
					request.addCookie(cookie);
				}
			}
		}
	}
	
	private void readCookies(HttpResponse response) {
		if (trackCookie) {
			Cookie[] respCookies = response.getCookies();
			//
			if (respCookies.length > 0) {
				if (cookies == null) {
					cookies = new Vector(respCookies.length);
				}
				//
				for (int i = 0, ci = -1; i < respCookies.length; i++) {
					ci = cookies.indexOf(respCookies[i]);
					//
					if (ci == -1) {
						cookies.addElement(respCookies[i]);
					} else {
						cookies.setElementAt(respCookies[i], ci);
					}
				}
			}
		}
	}
	
	private void checkPath(String path) {
		if (Util.isEmptyString(path)) {
			throw new IllegalArgumentException("Path null or empty!");
		}
	}
	
	private void setDefaultHeaders() {
		setHeader(
			HttpRequest.Header.ACCEPT_LANGUAGE,
			Util.getSystemProperty("microedition.locale", "en-US"));
		setHeader(HttpRequest.Header.ACCEPT_CHARSET, Util.UTF8);
		setHeader(HttpRequest.Header.USER_AGENT, Util.USER_AGENT);
	}
}
