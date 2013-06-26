package com.emobtech.networkingme;

//scheme://domain:port/path?query_string#fragment_id

public final class URL {
	
	private String url;
	
	static void validate(String url) {
	}
	
	public URL(String url) {
		if (StringUtil.isEmpty(url)) {
			throw new IllegalArgumentException("URL null or empty!");
		}
		//
		validate(url);
		//
		this.url = url;
	}
	
	public String getScheme() {
		return url.substring(0, url.indexOf("://"));
	}

	public String getDomain() {
		final int endSchemeIndex = url.indexOf("://") +3;
		final int portIndex = url.indexOf(":", endSchemeIndex);
		final int pathIndex = url.indexOf("/", endSchemeIndex);
		//
		if (portIndex != -1) {
			return url.substring(endSchemeIndex, portIndex);
		} else if (pathIndex != -1) {
			return url.substring(endSchemeIndex, pathIndex);
		} else {
			return url.substring(endSchemeIndex);
		}
	}

	public int getPort() {
		String port = null;
		//
		final int endSchemeIndex = url.indexOf("://") +3;
		final int portIndex = url.indexOf(":", endSchemeIndex);
		//
		if (portIndex != -1) {
			final int pathIndex = url.indexOf("/", endSchemeIndex);
			//
			if (pathIndex != -1) {
				port = url.substring(portIndex +1, pathIndex);
			} else {
				port = url.substring(portIndex +1);
			}
		}
		//
		if (port == null) {
			final String scheme = getScheme().toLowerCase();
			//
			if ("http".equals(scheme)) {
				port = "80";
			} else if ("https".equals(scheme)) {
				
			}
			
		}
		//
		return Integer.parseInt(port);
	}

	public String getPath() {
		return null;
	}

	public String getQueryString() {
		return null;
	}

	public String getFragment() {
		return null;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || !(obj instanceof URL)) {
			return false;
		}
		//
		return url.equals(((URL)obj).url);
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return url.hashCode();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return url;
	}
	
	public static void main(String[] args) {
		URL url = new URL("http://www.emobtech.com:8080");
		//
		System.out.println("scheme: " + url.getScheme());
		System.out.println("domain: " + url.getDomain());
	}
}
