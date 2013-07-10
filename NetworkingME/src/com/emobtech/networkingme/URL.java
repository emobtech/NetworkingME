/* URL.java
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

import java.util.Hashtable;

import com.emobtech.networkingme.util.Util;

/**
 * <p>
 * This class represents an URL - Uniform Resoure Locator.
 * </p>
 * <p>
 * Reference: <br />
 * <a href="http://en.wikipedia.org/wiki/Uniform_resource_locator" target="_blank">
 *     http://en.wikipedia.org/wiki/Uniform_resource_locator
 * </a>
 * </p>
 * @author Ernandes Jr. (ernandes@emobtech.com)
 * @version 1.0
 * @since 1.0
 */
public final class URL {
	/**
	 * <p>
	 * HTTP prefix.
	 * </p>
	 */
	private static final String HTTP_SCHEME = "http";

	/**
	 * <p>
	 * HTTP default port.
	 * </p>
	 */
	private static final int HTTP_PORT = 80;

	/**
	 * <p>
	 * HTTPS prefix.
	 * </p>
	 */
	private static final String HTTPS_SCHEME = "https";

	/**
	 * <p>
	 * HTTPS default port.
	 * </p>
	 */
	private static final int HTTPS_PORT = 443;
	
	/**
	 * <p>
	 * URL string.
	 * </p>
	 */
	private String url;
	
	/**
	 * <p>
	 * Creates a new URL with a given string, e.g. "http://www.emobtech.com".
	 * </p>
	 * @param url URL.
	 * @throws IllegalArgumentException URL null or empty!
	 */
	public URL(String url) {
		if (Util.isEmptyString(url)) {
			throw new IllegalArgumentException("URL null or empty!");
		}
		//
		this.url = url;
	}
	
	/**
	 * <p>
	 * Creates a new URL with a given base and path, e.g. 
	 * "http://www.emobtech.com" and "/page.html".
	 * </p>
	 * <p>
	 * The result URL is "http://www.emobtech.com/page.html".
	 * </p>
	 * @param url URL.
	 * @throws IllegalArgumentException URL null or empty!
	 */
	public URL(URL baseURL, String path) {
		this(baseURL.url + Util.formatPath(path));
	}
	
	/**
	 * <p>
	 * Creates a new URL with a given base, path and parameters, e.g. 
	 * "http://www.emobtech.com", "/page.html" and "parameter=value".
	 * </p>
	 * <p>
	 * The result URL is "http://www.emobtech.com/page.html?parameter=value".
	 * </p>
	 * @param url URL.
	 * @throws IllegalArgumentException URL null or empty!
	 */
	public URL(URL baseURL, String path, Hashtable parameters) {
		this(
			baseURL.url + 
			Util.formatPath(path) + 
			Util.formatQueryString(Util.toQueryString(parameters)));
	}
		
	/**
	 * <p>
	 * Returns the scheme.
	 * </p>
	 * <p>
	 * e.g., "http" from "http://www.emobtech.com".
	 * </p>
	 * @return Scheme.
	 */
	public String getScheme() {
		return url.substring(0, url.indexOf("://"));
	}

	/**
	 * <p>
	 * Returns the domain.
	 * </p>
	 * <p>
	 * e.g., "www.emobtech.com" from "http://www.emobtech.com".
	 * </p>
	 * @return Domain.
	 */
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

	/**
	 * <p>
	 * Returns the port.
	 * </p>
	 * <p>
	 * e.g., 8080 from "http://www.emobtech.com:8080".
	 * </p>
	 * <p>
	 * In the absence of a specified port, -1 is returned. In case of HTTP and 
	 * HTTPS schemes, their default port, 80 and 403, are returned.
	 * </p>
	 * @return Port.
	 */
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

	/**
	 * <p>
	 * Returns the path.
	 * </p>
	 * <p>
	 * e.g., "/page.html" from "http://www.emobtech.com/page.html".
	 * </p>
	 * @return Path.
	 */
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

	/**
	 * <p>
	 * Returns the query string.
	 * </p>
	 * <p>
	 * e.g., "parameter=value" from "http://www.emobtech.com?parameter=value".
	 * </p>
	 * @return Query string.
	 */
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

	/**
	 * <p>
	 * Returns the fragment.
	 * </p>
	 * <p>
	 * e.g., "fragment" from "http://www.emobtech.com#fragment".
	 * </p>
	 * @return Fragment.
	 */
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
	
	/**
	 * <p>
	 * Return an encoded URL as string, e.g., 
	 * "http://www.emobtech.com?parameter=value%20otherValue"
	 * </p>
	 * @return Encoded URL.
	 */
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
}
