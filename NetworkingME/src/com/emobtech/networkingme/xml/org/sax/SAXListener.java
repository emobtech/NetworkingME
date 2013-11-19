/* SAXListener.java
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
package com.emobtech.networkingme.xml.org.sax;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.helpers.DefaultHandler;

import com.emobtech.networkingme.BinaryListener;

/**
 * <p>
 * This class represents a listener to request events, which provide a utility 
 * method to obtain the default handler easily, result of a successful response.
 * </p>
 * <p>
 * This class implementation is based on SAX XML parser by 
 * <a href="http://www.jcp.org/aboutJava/communityprocess/final/jsr172" target="_blank">JSR 172 - J2ME Web
 * Services Specification</a>.
 * </p>
 * @author Ernandes Jr. (ernandes@emobtech.com)
 * @version 1.0
 * @since 1.1
 */
public abstract class SAXListener extends BinaryListener {
	/**
	 * Handler.
	 */
	private DefaultHandler handler;
	
	/**
	 * Creates a new instance of SAXListener with a given handler.
	 * @param handler Handler.
	 * @throws IllegalArgumentException Handler is null!
	 */
	public SAXListener(DefaultHandler handler) {
		if (handler == null) {
			throw new IllegalArgumentException("Handler is null!");
		}
		//
		this.handler = handler;
	}
	
	/**
	 * <p>
	 * Called when the request is concluded successfully.
	 * </p>
	 * @param handler Response content as default handler.
	 */
	public abstract void onDefaultHandler(DefaultHandler handler);

	/**
	 * @see com.emobtech.networkingme.BinaryListener#onBinary(byte[])
	 */
	public final void onBinary(byte[] data) {
		try {
			SAXParser p = SAXParserFactory.newInstance().newSAXParser();
			//
			p.parse(new ByteArrayInputStream(data), handler);
			//
			onDefaultHandler(handler);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
