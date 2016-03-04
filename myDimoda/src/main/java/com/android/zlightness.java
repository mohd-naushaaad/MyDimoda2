package com.android;

public class zlightness {
	
	public native float	calc(byte[] buffer, int width,  int height);
	
	static {
		System.loadLibrary("cvlibbase");
	    System.loadLibrary("zlightness");
	}
}