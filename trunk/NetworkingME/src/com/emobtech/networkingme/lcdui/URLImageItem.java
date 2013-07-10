/* URLImageItem.java
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
package com.emobtech.networkingme.lcdui;

import javax.microedition.lcdui.CustomItem;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.emobtech.networkingme.Request;
import com.emobtech.networkingme.RequestException;
import com.emobtech.networkingme.RequestOperation;
import com.emobtech.networkingme.URL;
import com.emobtech.networkingme.http.HttpRequest;

/**
 * This class implements a Custom Item that loads and displays an image from an
 * URL.
 * @author Ernandes Jr. (ernandes@emobtech.com)
 * @version 1.0
 * @since 1.0
 */
public final class URLImageItem extends CustomItem {
	/**
	 * <p>
	 * Min item's size.
	 * </p>
	 */
	private static final int MIN_SIZE = 25;
	
	/**
	 * <p>
	 * Image's URL.
	 * </p>
	 */
	private URL url;
	
	/**
	 * <p>
	 * Placeholder.
	 * </p>
	 */
	private Image placeholder;
	
	/**
	 * <p>
	 * Image.
	 * </p>
	 */
	private Image image;
	
	/**
	 * <p>
	 * Alternate text.
	 * </p>
	 */
	private String altText;

	/**
	 * <p>
	 * Creates a new URLImageItem with the given label, URL, layout directive, 
	 * placeholder and alternate text string.
	 * </p>
	 * @param label Label.
	 * @param url Image's URL.
	 * @param layout Layout directive.
	 * @param placeholder Placeholder, displayed while image is loaded.
	 * @param altText Alternate text.
	 */
	public URLImageItem(String label, URL url, int layout, Image placeholder, 
		String altText) {
		super(label);
		//
		if (url == null) {
			throw new IllegalArgumentException("URL is null!");
		}
		//
		this.url = url;
		this.placeholder = placeholder;
		this.altText = altText;
		//
		setLayout(layout);
	}

	/**
	 * Get the image's URL.
	 * @return URL.
	 */
	public URL getUrl() {
		return url;
	}

	/**
	 * Get the placeholder.
	 * @return Placeholder.
	 */
	public Image getPlaceholder() {
		return placeholder;
	}

	/**
	 * Get the image.
	 * @return Image.
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * Get the alternate text.
	 * @return Text.
	 */
	public String getAltText() {
		return altText;
	}

	/**
	 * @see javax.microedition.lcdui.CustomItem#showNotify()
	 */
	protected void showNotify() {
		if (image == null) {
			requestImage();
		}
	}	

	/**
	 * @see javax.microedition.lcdui.CustomItem#getMinContentHeight()
	 */
	protected int getMinContentHeight() {
		if (image != null) {
			return image.getHeight();
		} else if (placeholder != null) {
			return placeholder.getHeight();
		} else if (altText != null) {
			return Font.getDefaultFont().getHeight();
		} else {
			return MIN_SIZE;
		}
	}

	/**
	 * @see javax.microedition.lcdui.CustomItem#getMinContentWidth()
	 */
	protected int getMinContentWidth() {
		if (image != null) {
			return image.getWidth();
		} else if (placeholder != null) {
			return placeholder.getWidth();
		} else if (altText != null) {
			return Font.getDefaultFont().stringWidth(altText);
		} else {
			return MIN_SIZE;
		}
	}

	/**
	 * @see javax.microedition.lcdui.CustomItem#getPrefContentHeight(int)
	 */
	protected int getPrefContentHeight(int width) {
		return getMinContentHeight();
	}

	/**
	 * @see javax.microedition.lcdui.CustomItem#getPrefContentWidth(int)
	 */
	protected int getPrefContentWidth(int height) {
		return getMinContentWidth();
	}

	/**
	 * @see javax.microedition.lcdui.CustomItem#paint(javax.microedition.lcdui.Graphics, int, int)
	 */
	protected void paint(Graphics g, int w, int h) {
		final boolean matchSize =
			w == getPreferredWidth() && h == getPreferredHeight();
		//
		if (matchSize && image != null) {
			g.drawImage(image, 0, 0, 0);
		} else if (matchSize && placeholder != null) {
			g.drawImage(placeholder, 0, 0, 0);
		} else if (altText != null) {
			g.setColor(255, 255, 255);
			g.fillRect(0, 0, w, h);
			g.setColor(0, 0, 0);
			g.setFont(Font.getDefaultFont());
			g.drawString(altText, 0, 0, 0);
		}
	}
	
	/**
	 * Request the image.
	 */
	private void requestImage() {
		RequestOperation oper = new RequestOperation(new HttpRequest(url));
		oper.start(new ImageListener() {
			public void onImage(Image image) {
				URLImageItem.this.image = image;
				//
				invalidate();
				repaint();
			}

			public void onFailure(Request request, RequestException exception) {
				invalidate();
				repaint();
			}
		});
	}
}
