/* URL.java
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

//scheme://domain:port/path?query_string#fragment_id

public final class URL {
	
	private static final String HTTP_SCHEME = "http";
	private static final int HTTP_PORT = 80;
	private static final String HTTPS_SCHEME = "https";
	private static final int HTTPS_PORT = 443;
	
	private String url;
	
	public URL(String url) {
		if (Util.isEmptyString(url)) {
			throw new IllegalArgumentException("URL null or empty!");
		}
		//
		this.url = url;
	}
	
	URL(URL baseURL, String path) {
		this(baseURL.url + Util.formatPath(path));
	}
	
	URL(URL baseURL, String path, Hashtable parameters) {
		this(
			baseURL.url + 
			Util.formatPath(path) + 
			Util.formatQueryString(Util.toQueryString(parameters)));
	}
		
	public String getScheme() {
		return url.substring(0, url.indexOf("://"));
	}

	public String getDomain() {
		int endSchemeIndex = url.indexOf("://") +3;
		int portIndex = url.indexOf(":", endSchemeIndex);
		int pathIndex = url.indexOf("/", endSchemeIndex);
		//
		if (portIndex != -1) {
			return url.substring(endSchemeIndex, portIndex);
		} else if (pathIndex != -1) {
			return url.substring(endSchemeIndex, pathIndex);
		} else {
			return url.substring(endSchemeIndex);
		}
	}

	public int getPort() {
		String port = null;
		//
		int endSchemeIndex = url.indexOf("://") +3;
		int portIndex = url.indexOf(":", endSchemeIndex);
		//
		if (portIndex != -1) {
			int pathIndex = url.indexOf("/", endSchemeIndex);
			//
			if (pathIndex != -1) {
				port = url.substring(portIndex +1, pathIndex);
			} else {
				port = url.substring(portIndex +1);
			}
		}
		//
		if (port == null) {
			String scheme = getScheme().toLowerCase();
			//
			if (HTTP_SCHEME.equals(scheme)) {
				port = String.valueOf(HTTP_PORT);
			} else if (HTTPS_SCHEME.equals(scheme)) {
				port = String.valueOf(HTTPS_PORT);
			} else {
				port = "-1";
			}
		}
		//
		return Integer.parseInt(port);
	}

	public String getPath() {
		int endSchemeIndex = url.indexOf("://") +3;
		int pathIndex = url.indexOf("/", endSchemeIndex);
		//
		if (pathIndex != -1) {
			int queryStringIndex = url.indexOf("?", pathIndex);
			//
			if (queryStringIndex != -1) {
				return url.substring(pathIndex, queryStringIndex);
			} else {
				return url.substring(pathIndex);
			}
		} else {
			return null;
		}
	}

	public String getQueryString() {
		int queryStringIndex = url.indexOf("?");
		//
		if (queryStringIndex != -1) {
			int fragmentIndex = url.indexOf("#", queryStringIndex);
			//
			if (fragmentIndex != -1) {
				return url.substring(queryStringIndex +1, fragmentIndex);
			} else {
				return url.substring(queryStringIndex +1);
			}
		} else {
			return null;
		}
	}

	public String getFragment() {
		final int fragmentIndex = url.indexOf("#");
		//
		if (fragmentIndex != -1) {
			return url.substring(fragmentIndex +1);
		} else {
			return null;
		}
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || !(obj instanceof URL)) {
			return false;
		}
		//
		return url.equals(((URL)obj).url);
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return url.hashCode();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return url;
	}
	
	public String toEncodedString() {
		StringBuffer encodedUrl = new StringBuffer();
		//
		encodedUrl.append(getScheme());
		encodedUrl.append("://");
		encodedUrl.append(getDomain());
		//
		int port = getPort();
		//
		if (port != -1 & port != HTTP_PORT && port != HTTPS_PORT) {
			encodedUrl.append(":");
			encodedUrl.append(port);
		}
		//
		String path = getPath();
		//
		if (path != null) {
			encodedUrl.append(path);
		}
		//
		String queryString = getQueryString();
		//
		if (queryString != null) {
			encodedUrl.append("?");
			encodedUrl.append(Util.encodeQueryString(queryString));
		}
		//
		String fragment = getFragment();
		//
		if (fragment != null) {
			encodedUrl.append("#");
			encodedUrl.append(fragment);
		}
		//
		return encodedUrl.toString();
	}
	
	public static void main(String[] args) {
		URL url = new URL("http://www.emobtech.com:7777/page/path.html?param=val ue&param2=val ue2#fragment");
		//
		System.out.println("scheme: " + url.getScheme());
		System.out.println("domain: " + url.getDomain());
		System.out.println("port: " + url.getPort());
		System.out.println("path: " + url.getPath());
		System.out.println("queryString: " + url.getQueryString());
		System.out.println("fragment: " + url.getFragment());
		System.out.println("encodedString: " + url.toEncodedString());
	}
}