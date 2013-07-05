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
package com.emobtech.networkingme;

import java.util.Date;

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
		String nameValue = cookie.substring(0, cookie.indexOf(';'));
		//
		return Util.splitString(nameValue, '=')[0];
	}
	
	public String getValue() {
		String nameValue = cookie.substring(0, cookie.indexOf(';'));
		//
		return Util.splitString(nameValue, '=')[1];
	}
	
	public Date getExpiration() {
		int expireIndex = cookie.indexOf("Expires=");
		//
		if (expireIndex != -1) {
			String date =
				cookie.substring(
					expireIndex + 8, cookie.indexOf(';', expireIndex));
			//
			return Util.parseCookieDate(date);
		} else {
			return null;
		}
	}
	
	public String getPath() {
		int pathIndex = cookie.indexOf("Path=");
		//
		if (pathIndex != -1) {
			return cookie.substring(
				pathIndex + 5, cookie.indexOf(';', pathIndex)); 
		} else {
			return null;
		}
	}
	
	public String getDomain() {
		int domainIndex = cookie.indexOf("Domain=");
		//
		if (domainIndex != -1) {
			return cookie.substring(
				domainIndex + 7, cookie.indexOf(';', domainIndex)); 
		} else {
			return null;
		}
	}
	
	public boolean isSecure() {
		return cookie.indexOf("Secure") != -1;
	}
	
	public boolean isHttpOnly() {
		return cookie.indexOf("HttpOnly") != -1;
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
