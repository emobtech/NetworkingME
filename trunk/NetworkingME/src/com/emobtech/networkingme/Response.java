package com.emobtech.networkingme;

import java.io.InputStream;

public abstract class Response {
	
	public abstract boolean wasSuccessfull();
	
	public abstract int getCode();
	
	public abstract InputStream getStream();
	
	public abstract byte[] getBytes();
	
	public abstract long getSize();
	
	public abstract String getContentType();
}
