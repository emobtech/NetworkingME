package com.emobtech.networkingme;

import java.io.IOException;

public abstract class Request {
	
	private URL url;
	
	public Request(URL url) {
		if (url == null) {
			throw new IllegalArgumentException("URL null!");
		}
		//
		this.url = url;
	}
	
	public URL getURL() {
		return url;
	}
	
	abstract Response send() throws IOException;
}
