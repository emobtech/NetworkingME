/*
 * StringUtil.java
 * 28/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.networkingme;

import java.util.Vector;

/**
 * <p>
 * This class provides some useful methods to work with strings.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.6
 * @since 1.1
 */
final class StringUtil {
	/**
     * <p>
     * Split a string based on a given delimiter.
     * </p>
     * @param str String.
     * @param delimiter Delimiter.
     * @return String tokens.
     * @throws IllegalArgumentException If str is null.
     */
    public static String[] split(String str, char delimiter) {
    	if (str == null) {
    		throw new IllegalArgumentException("Str must not be null.");
    	}
    	//
        Vector v = new Vector();
        int start = 0;
        int iof;
        //
        while ((iof = str.indexOf(delimiter, start)) != -1) {
            v.addElement(str.substring(start, iof).trim());
            start = iof +1;
        }
        //
        v.addElement(str.substring(start, str.length()).trim());
        String[] codes = new String[v.size()];
        v.copyInto(codes);
        //
        return codes;
    }
	
	/**
	 * <p>
	 * Verify whether the given string is null or empty.
	 * </p>
	 * @param str The string.
	 * @return true null/empty.
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}
	
	/**
	 * <p>
	 * Replaces a given substring to another substring.
	 * <p/>
	 * @param text String.
	 * @param searchStr Substring to be replaced.
	 * @param replacementStr Replacement substring.
	 * @return String replaced.
	 * @throws IllegalArgumentException If any parameter is null.
	 */
	public static String replace(String text, String searchStr,
		String replacementStr) {
		if (text == null) {
			throw new IllegalArgumentException("Text must not be null.");
		}
		if (searchStr == null) {
			throw new IllegalArgumentException(
				"Search string must not be null.");
		}
		if (replacementStr == null) {
			throw new IllegalArgumentException(
				"Replacement string must not be null.");
		}
		//
		if (text.length() == 0 || searchStr.length() == 0) {
			return text;
		}
		//
		StringBuffer sb = new StringBuffer();
		int searchStringPos = text.indexOf(searchStr);
		int startPos = 0;
		int searchStringLength = searchStr.length();
		//
		while (searchStringPos != -1) {
			sb.append(
				text.substring(startPos, searchStringPos)).append(
					replacementStr);
			startPos = searchStringPos + searchStringLength;
			searchStringPos = text.indexOf(searchStr, startPos);
		}
		//
		sb.append(text.substring(startPos, text.length()));
		//
		return sb.toString();
	}
	
	/**
	 * <p>
	 * Get the value of a given param in the Url.
	 * </p>
	 * @param url Url.
	 * @param param Parameter.
	 * @return Value.
	 */
	public static String getUrlParamValue(String url, String param) {
		int ix = url.indexOf('?');
		//
		if (ix != -1) {
			url = url.substring(ix +1);
		}
		//
		String[] params = StringUtil.split(url, '&');
		//
		for (int i = 0; i < params.length; i++) {
			if (params[i].startsWith(param + '=')) {
				return StringUtil.split(params[i], '=')[1];
			}
		}
		//
		return null;
	}
	
    /**
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private StringUtil() {
	}
}