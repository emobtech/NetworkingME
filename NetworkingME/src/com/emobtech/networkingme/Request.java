package com.emobtech.networkingme;

import java.io.IOException;

public abstract class Request {
	
	private URL url;
	private Body body;
	
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
	
	public void setBody(Body body) {
		this.body = body;
	}
	
	public Body getBody() {
		return body;
	}
	
	abstract Response send() throws IOException;
}
