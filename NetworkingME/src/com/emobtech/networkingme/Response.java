package com.emobtech.networkingme;

public abstract class Response {
	
	public abstract boolean wasSuccessfull();
	
	public abstract int getCode();
	
	public abstract byte[] getBytes();
	
	public abstract long getSize();
	
	public abstract String getContentType();
}
