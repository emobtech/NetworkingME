/* Cookie.java
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

import java.util.Date;

import com.emobtech.networkingme.util.Util;

/**
 * <p>
 * This class represents a HTTP cookie.
 * </p>
 * <p>
 * Reference: <br />
 * <a href="https://en.wikipedia.org/wiki/HTTP_cookie" target="_blank">
 *     https://en.wikipedia.org/wiki/HTTP_cookie
 * </a>
 * </p>
 * @author Ernandes Jr. (ernandes@emobtech.com)
 * @version 1.0
 * @since 1.0
 */
public final class Cookie {
	/**
	 * <p>
	 * Cookie string.
	 * </p>
	 */
	private String cookie;
	
	/**
	 * <p>
	 * Creates a Cookie from given string.
	 * </p>
	 * @param cookie Cookie.
	 * @throws IllegalArgumentException Cookie null, empty or invalid!
	 */
	public Cookie(String cookie) {
		if (Util.isEmptyString(cookie)) {
			throw new IllegalArgumentException("Cookie null or empty!");
		}
		//
		this.cookie = cookie;
		//
		if (Util.isEmptyString(getName())) {
			throw new IllegalArgumentException("Cookie's name missing!");
		}
		if (Util.isEmptyString(getValue())) {
			throw new IllegalArgumentException("Cookie's value missing!");
		}
	}
	
	/**
	 * <p>
	 * Returns the name.
	 * </p>
	 * @return Name.
	 */
	public String getName() {
		return cookie.substring(0, cookie.indexOf('='));
	}
	
	/**
	 * <p>
	 * Returns the value.
	 * </p>
	 * @return Value.
	 */
	public String getValue() {
		return cookie.substring(
			cookie.indexOf('=') +1,
			Util.indexOrLastOfString(cookie, ";", 0, true));
	}
	
	/**
	 * <p>
	 * Returns the expiration date.
	 * </p>
	 * @return Date.
	 */
	public Date getExpiration() {
		int expireIndex = Util.indexOfString(cookie, "Expires=", 0, false);
		//
		if (expireIndex != -1) {
			String date =
				cookie.substring(
					expireIndex + 8,
					Util.indexOrLastOfString(cookie, ";", expireIndex, true));
			//
			return Util.parseCookieDate(date);
		} else {
			return null;
		}
	}
	
	/**
	 * <p>
	 * Returns the path.
	 * </p>
	 * @return Path.
	 */
	public String getPath() {
		int pathIndex = Util.indexOfString(cookie, "Path=", 0, false);
		//
		if (pathIndex != -1) {
			return cookie.substring(
				pathIndex + 5,
				Util.indexOrLastOfString(cookie, ";", pathIndex, true)); 
		} else {
			return null;
		}
	}
	
	/**
	 * <p>
	 * Returns the domain.
	 * </p>
	 * @return Domain.
	 */
	public String getDomain() {
		int domainIndex = Util.indexOfString(cookie, "Domain=", 0, false);
		//
		if (domainIndex != -1) {
			return cookie.substring(
				domainIndex + 7,
				Util.indexOrLastOfString(cookie, ";", domainIndex, true)); 
		} else {
			return null;
		}
	}
	
	/**
	 * <p>
	 * Returns whether it is secure.
	 * </p>
	 * @return Secure (true).
	 */
	public boolean isSecure() {
		return Util.indexOfString(cookie, "Secure", 0, false) != -1;
	}
	
	/**
	 * <p>
	 * Returns whether it is HTTP only.
	 * </p>
	 * @return HTTP only (true).
	 */
	public boolean isHttpOnly() {
		return Util.indexOfString(cookie, "HttpOnly", 0, false) != -1;
	}
	
	/**
	 * <p>
	 * Returns whether it is expired.
	 * </p>
	 * @return Expired (true).
	 */
	public boolean isExpired() {
		Date expiration = getExpiration();
		//
		if (expiration != null) {
			return expiration.getTime() < new Date().getTime();
		} else {
			return false;
		}
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || !(obj instanceof Cookie)) {
			return false;
		}
		//
		Cookie cookie = (Cookie)obj;
		//
		if (!Util.areEqual(getName(), cookie.getName())) {
			return false;
		}
		if (!Util.areEqual(getDomain(), cookie.getDomain())) {
			return false;
		}
		if (!Util.areEqual(getPath(), cookie.getPath())) {
			return false;
		}
		//
		return true;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return cookie.hashCode();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return cookie;
	}
}
