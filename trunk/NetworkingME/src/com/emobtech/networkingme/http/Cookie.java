/* Cookie.java
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

import java.util.Date;

import com.emobtech.networkingme.util.Util;

public final class Cookie {
	
	private String cookie;
	
	Cookie(String cookie) {
		if (Util.isEmptyString(cookie)) {
			throw new IllegalArgumentException("Cookie null or empty!");
		}
		//
		this.cookie = cookie;
	}
	
	public String getName() {
		return cookie.substring(0, cookie.indexOf('='));
	}
	
	public String getValue() {
		return cookie.substring(
			cookie.indexOf('=') +1,
			Util.indexOrLastOfString(cookie, ";", 0, true));
	}
	
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
	
	public boolean isSecure() {
		return Util.indexOfString(cookie, "Secure", 0, false) != -1;
	}
	
	public boolean isHttpOnly() {
		return Util.indexOfString(cookie, "HttpOnly", 0, false) != -1;
	}
	
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
		try {
			if (!getName().equals(cookie.getName())) {
				return false;
			}
			if (!getDomain().equals(cookie.getDomain())) {
				return false;
			}
			if (!getPath().equals(cookie.getPath())) {
				return false;
			}
			//
			return true;
		} catch (NullPointerException e) {
			return false;
		}
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
	
	public static void main(String[] args) {
		Cookie cookie = new Cookie("SSID=Ap4PGTEq; Domain=.foo.com; Path=/; Expires=Wed, 13 Jan 2013 22:23:01 GMT; Secure; HttpOnly");
		//
		System.out.println("Name: " + cookie.getName());
		System.out.println("Value: " + cookie.getValue());
		System.out.println("Path: " + cookie.getPath());
		System.out.println("Domain: " + cookie.getDomain());
		System.out.println("Expiration: " + cookie.getExpiration());
		System.out.println("Secure: " + cookie.isSecure());
		System.out.println("HttpOnly: " + cookie.isHttpOnly());
		System.out.println("Expired: " + cookie.isExpired());
	}
}
