package com.emobtech.networkingme;

public abstract class Request {
	
	private URL url;
	
	public Request(URL url) {
		this.url = url;
	}
	
	abstract Response send();
}
