/* URLButton.java
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
package com.emobtech.networkingme.lwuit;

import com.emobtech.networkingme.Request;
import com.emobtech.networkingme.RequestException;
import com.emobtech.networkingme.RequestOperation;
import com.emobtech.networkingme.URL;
import com.emobtech.networkingme.http.HttpRequest;
import com.sun.lwuit.Button;
import com.sun.lwuit.Image;

/**
 * This class implements a Button that loads and displays an icon from an URL.
 * @author Ernandes Jr. (ernandes@emobtech.com)
 * @version 1.0
 * @since 1.1
 */
public final class URLButton extends Button {
	/**
	 * <p>
	 * Icon's URL.
	 * </p>
	 */
	private URL iconUrl;

	/**
	 * <p>
	 * Creates a new URLButton with a given icon's URL and a placeholder.
	 * </p>
	 * @param iconUrl Icon's URL.
	 * @param placeholder Placeholder, displayed while icon is loaded.
	 * @throws IllegalArgumentException URL is null!
	 */
	public URLButton(URL iconUrl, Image placeholder) {
		this(iconUrl);
		//
		setIcon(placeholder);
	}
	
	/**
	 * <p>
	 * Creates a new URLButton with a given icon's URL and a placeholder.
	 * </p>
	 * @param iconUrl Icon's URL.
	 * @param placeholder Placeholder, displayed while icon is loaded.
	 * @throws IllegalArgumentException URL is null!
	 */
	public URLButton(URL iconUrl, String placeholder) {
		this(iconUrl);
		//
		setText(placeholder);
	}

	/**
	 * <p>
	 * Creates a new URLButton with a given icon's URL.
	 * </p>
	 * @param iconUrl Icon's URL.
	 * @throws IllegalArgumentException URL is null!
	 */
	public URLButton(URL iconUrl) {
		if (iconUrl == null) {
			throw new IllegalArgumentException("Icon's URL is null!");
		}
		//
		this.iconUrl = iconUrl;
	}
	
	/**
	 * @see com.sun.lwuit.Component#initComponent()
	 */
	protected void initComponent() {
		super.initComponent();
		//
		requestIcon();
	}
	
	/**
	 * Request the icon.
	 */
	private void requestIcon() {
		RequestOperation oper = new RequestOperation(new HttpRequest(iconUrl));
		oper.start(new ImageListener() {
			public void onImage(Image image) {
				setIcon(image);
			}

			public void onFailure(Request request, RequestException exception) {
			}
		});
	}
}
