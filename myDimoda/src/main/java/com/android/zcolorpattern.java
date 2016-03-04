package com.android;

public class zcolorpattern {
	
	public  native void		create ();
	public  native void		release ();
	
	public  native int		recognize(int[] buffer, int width, int height, int channel);
	public  native int		getColor();
	public  native int		getColorValue();
	public  native int		getPattern();

	public zcolorpattern()
	{
		create();
	}
    static {
       System.loadLibrary("cvlibbase");
       System.loadLibrary("zcolorpattern");
    }
}
