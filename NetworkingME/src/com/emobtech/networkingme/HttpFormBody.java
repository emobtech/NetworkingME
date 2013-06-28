package com.emobtech.networkingme;

public final class HttpFormBody extends Body {

	public String getType() {
		return "application/x-www-form-urlencoded";
	}

	public long getLength() {
		return 0;
	}

	public byte[] getContent() {
		return null;
	}
}
