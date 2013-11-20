/* ImageListener.java
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

import com.emobtech.networkingme.BinaryListener;
import com.sun.lwuit.Image;

/**
 * <p>
 * This class represents a listener to request events, which provide a utility 
 * method to obtain the image content easily, result of a successful response.
 * </p>
 * <p>
 * This class implementation is based on LWUIT image by 
 * <a href="http://lwuit.java.net" target="_blank">LWUIT</a>.
 * </p>
 * @author Ernandes Jr. (ernandes@emobtech.com)
 * @version 1.0
 * @since 1.1
 */
public abstract class ImageListener extends BinaryListener {
	/**
	 * <p>
	 * Called when the request is concluded successfully.
	 * </p>
	 * @param image Response content as image.
	 */
	public abstract void onImage(Image image);
	
	/**
	 * @see com.emobtech.networkingme.BinaryListener#onBinary(byte[])
	 */
	public final void onBinary(byte[] bytes) {
		onImage(Image.createImage(bytes, 0, bytes.length));
	}
}
