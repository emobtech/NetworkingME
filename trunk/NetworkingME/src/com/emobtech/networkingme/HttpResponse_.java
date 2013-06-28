package com.emobtech.networkingme;

import javax.microedition.io.HttpConnection;

public class HttpResponse_ extends Response {
	
	HttpResponse_(HttpConnection conn) {
		
	}

	public boolean wasSuccessfull() {
		return false;
	}

	public int getCode() {
		return 0;
	}

	public byte[] getBytes() {
		return null;
	}

	public long getSize() {
		return 0;
	}

	public String getContentType() {
		return null;
	}
}
