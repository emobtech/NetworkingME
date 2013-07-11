/* HttpClient.java
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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.emobtech.networkingme.BinaryListener;
import com.emobtech.networkingme.Payload;
import com.emobtech.networkingme.Request;
import com.emobtech.networkingme.RequestException;
import com.emobtech.networkingme.RequestOperation;
import com.emobtech.networkingme.RequestOperation.Listener;
import com.emobtech.networkingme.Response;
import com.emobtech.networkingme.TextListener;
import com.emobtech.networkingme.URL;
import com.emobtech.networkingme.http.HttpRequest.Header;
import com.emobtech.networkingme.util.Util;

/**
 * <p>
 * This class implements the common patterns of communicating with an web 
 * application over HTTP. It encapsulates information like base URL, 
 * authorization, headers, cookies and uses them to construct and manage the 
 * execution of HTTP request operations.
 * </p>
 * <p>
 * The following headers are defined by default:<br/>
 * <br/>- Accept-Language: (local language), en-US;q=0.8
 * <br/>- Accept-Charset: (local charset), UTF-8
 * <br/>- User-Agent: (generated user agent)
 * <br/><br/>You can override these headers or define new ones using 
 * {@link HttpClient#setHeader(String, String)}.
 * </p>
 * <p>
 * URL redirect is automatically handled. To disable it, use 
 * {@link HttpClient#setHandleRedirect(boolean)}.  
 * </p>
 * <p>
 * Cookies are automatically collected and sent back in following requests. To 
 * disable it, use {@link HttpClient#setTrackCookie(boolean)}.  
 * </p>
 * @author Ernandes Jr. (ernandes@emobtech.com)
 * @version 1.0
 * @since 1.0
 */
public final class HttpClient {
	/**
	 * <p>
	 * Base URL.
	 * </p>
	 */
	private URL baseURL;

	/**
	 * <p>
	 * Header.
	 * </p>
	 */
	private Hashtable header;

	/**
	 * <p>
	 * Handle redirect flag.
	 * </p>
	 */
	private boolean handleRedirect = true;
	
	/**
	 * <p>
	 * Track cookie flag.
	 * </p>
	 */
	private boolean trackCookie = true;
	
	/**
	 * <p>
	 * Cookies.
	 * </p>
	 */
	private Vector cookies;
	
	/**
	 * <p>
	 * Creates a HttpClient for a given base URL.
	 * </p>
	 * @param baseURL Base URL.
	 * @throws IllegalArgumentException BaseURL null!
	 */
	public HttpClient(URL baseURL) {
		if (baseURL == null) {
			throw new IllegalArgumentException("BaseURL null!");
		}
		//
		this.baseURL = baseURL;
		//
		setDefaultHeaders();
	}
	
	/**
	 * <p>
	 * Returns the base URL.
	 * </p>
	 * @return URL.
	 */
	public URL getBaseURL() {
		return baseURL;
	}
	
	/**
	 * <p>
	 * Sets the user agent.
	 * </p>
	 * @param userAgent User agent.
	 * @throws IllegalArgumentException UserAgent null or empty!
	 * @see HttpClient#setHeader(String, String)
	 * @see HttpRequest.Header#USER_AGENT
	 */
	public void setUserAgent(String userAgent) {
		if (Util.isEmptyString(userAgent)) {
			throw new IllegalArgumentException("UserAgent null or empty!");
		}
		//
		setHeader(HttpRequest.Header.USER_AGENT, userAgent);
	}
	
	/**
	 * <p>
	 * Sets the automatic cookie tracking enabled.
	 * </p>
	 * @param enabled Enabled (true).
	 */
	public void setTrackCookie(boolean enabled) {
		trackCookie = enabled;
	}
	
	/**
	 * <p>
	 * Sets the automatic URL redirect enabled.
	 * </p>
	 * @param enabled Enabled (true).
	 */
	public void setHandleRedirect(boolean enabled) {
		handleRedirect = enabled;
	}
	
	/**
	 * <p>
	 * Sets a value to a given header field.
	 * </p>
	 * <p>
	 * Multiple values for a single field is supported. Just set different 
	 * values to the same field.
	 * </p>
	 * @param key Field key.
	 * @param value Value.
	 * @throws IllegalArgumentException Key/Value null or empty!
	 * @see HttpRequest.Header
	 */
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
		if (headerValue != null && !headerValue.equals(value)) {
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
	
	/**
	 * <p>
	 * Removes a given header field.
	 * </p>
	 * @param key Field key.
	 * @throws IllegalArgumentException Key null or empty!
	 * @see HttpRequest.Header
	 */
	public void removeHeader(String key) {
		if (Util.isEmptyString(key)) {
			throw new IllegalArgumentException("Key null or empty!");
		}
		//
		if (header != null) {
			header.remove(key);
		}
	}
	
	/**
	 * <p>
	 * Sets the credentials for a Basic Access Authentication.
	 * </p>
	 * <p>
	 * Reference: <br />
	 * <a href="http://en.wikipedia.org/wiki/Basic_access_authentication" target="_blank">
	 *     http://en.wikipedia.org/wiki/Basic_access_authentication
	 * </a>
	 * </p>
	 * @param username Username.
	 * @param password Password.
	 * @throws IllegalArgumentException Username/Password null or empty!
	 */
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

	/**
	 * <p>
	 * Removes all headers fields.
	 * </p>
	 */
	public void clearHeader() {
		if (header != null) {
			header.clear();
		}
	}
	
	/**
	 * <p>
	 * Removes all cookies.
	 * </p>
	 */
	public void clearCookies() {
		if (cookies != null) {
			cookies.removeAllElements();
		}
	}
	
	/**
	 * <p>
	 * Starts a GET HTTP request for a given path.
	 * </p>
	 * @param path Path.
	 * @param listener Request event listener.
	 * @throws IllegalArgumentException Path null or empty!
	 * @see BinaryListener
	 * @see TextListener
	 */
	public void get(String path, Listener listener) {
		get(path, null, listener);
	}

	/**
	 * <p>
	 * Starts a GET HTTP request for a given path with some parameters.
	 * </p>
	 * @param path Path.
	 * @param parameters Parameters.
	 * @param listener Request event listener.
	 * @throws IllegalArgumentException Path null or empty!
	 * @see BinaryListener
	 * @see TextListener
	 */
	public void get(String path, Hashtable parameters, Listener listener) {
		checkPath(path);
		//
		start(new HttpRequest(new URL(baseURL, path, parameters)), listener);
	}
	
	/**
	 * <p>
	 * Starts a POST HTTP request for a given path with some parameters.
	 * </p>
	 * @param path Path.
	 * @param parameters Parameters.
	 * @param listener Request event listener.
	 * @throws IllegalArgumentException Path null or empty!
	 * @see BinaryListener
	 * @see TextListener
	 */
	public void postForm(String path, Hashtable parameters, Listener listener) {
		post(path, new WebFormBody(parameters), listener);
	}
	
	/**
	 * <p>
	 * Starts the upload of a file to a given path.
	 * </p>
	 * @param path Path.
	 * @param name Field name.
	 * @param filename Filename.
	 * @param contentType Content type, e.g. "image/png".
	 * @param data File data.
	 * @param listener Request event listener.
	 * @throws IllegalArgumentException Path, name, filename, contentType and 
	 *         data null or empty!
	 * @see BinaryListener
	 * @see TextListener
	 */
	public void uploadFile(String path, String name, String filename, 
		String contentType, byte[] data, Listener listener) {
		MultipartBody body = new MultipartBody();
		body.addPart(name, filename, contentType, data);
		//
		post(path, body, listener);
	}

	/**
	 * <p>
	 * Starts a POST HTTP request for a given path with a body.
	 * </p>
	 * @param path Path.
	 * @param body Body.
	 * @param listener Request event listener.
	 * @throws IllegalArgumentException Path null or empty!
	 * @see WebFormBody
	 * @see MultipartBody
	 * @see BinaryListener
	 * @see TextListener
	 */
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

	/**
	 * <p>
	 * Starts a HEAD HTTP request for a given path.
	 * </p>
	 * @param path Path.
	 * @param listener Request event listener.
	 * @throws IllegalArgumentException Path null or empty!
	 * @see BinaryListener
	 * @see TextListener
	 */
	public void head(String path, Listener listener) {
		head(path, null, listener);
	}
	
	/**
	 * <p>
	 * Starts a HEAD HTTP request for a given path with some parameters.
	 * </p>
	 * @param path Path.
	 * @param parameters Parameters.
	 * @param listener Request event listener.
	 * @throws IllegalArgumentException Path null or empty!
	 * @see BinaryListener
	 * @see TextListener
	 */
	public void head(String path, Hashtable parameters, Listener listener) {
		checkPath(path);
		//
		start(
			new HttpRequest(
				new URL(baseURL, path, parameters),
				HttpRequest.Method.HEAD),
			listener);
	}

	/**
	 * <p>
	 * Starts a given HTTP request.
	 * </p>
	 * @param request Request.
	 * @param listener Request event listener.
	 */
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
	
	/**
	 * <p>
	 * Writes all header fields to the request.
	 * </p>
	 * @param request HTTP request.
	 */
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
	
	/**
	 * <p>
	 * Writes all cookies to the request.
	 * </p>
	 * @param request HTTP request.
	 */
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
	
	/**
	 * <p>
	 * Reads all cookies from the response.
	 * </p>
	 * @param response HTTP response.
	 */
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
	
	/**
	 * <p>
	 * Checks whether the path is valid.
	 * </p>
	 * @param path Path.
	 * @throws IllegalArgumentException Path null or empty!
	 */
	private void checkPath(String path) {
		if (Util.isEmptyString(path)) {
			throw new IllegalArgumentException("Path null or empty!");
		}
	}
	
	/**
	 * <p>
	 * Sets all default header fields.
	 * </p>
	 */
	private void setDefaultHeaders() {
		final String locale =
			Util.getSystemProperty("microedition.locale", Util.LOCALE_ENUS);
		//
		setHeader(HttpRequest.Header.ACCEPT_LANGUAGE, locale);
		//
		if (!locale.equals(Util.LOCALE_ENUS)) {
			setHeader(
				HttpRequest.Header.ACCEPT_LANGUAGE,
				Util.LOCALE_ENUS + ";q=0.8");
		}
		//
		final String encoding =
			Util.getSystemProperty("microedition.encoding", Util.UTF8);
		//
		setHeader(HttpRequest.Header.ACCEPT_CHARSET, encoding);
		//
		if (!encoding.equals(Util.UTF8)) {
			setHeader(HttpRequest.Header.ACCEPT_CHARSET, Util.UTF8);
		}
		//
		setHeader(HttpRequest.Header.USER_AGENT, Util.USER_AGENT);
	}
}
