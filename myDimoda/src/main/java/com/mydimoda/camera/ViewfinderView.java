package com.mydimoda.camera;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.mydimoda.R;

public final class ViewfinderView extends View {
	
	private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
	private static final int CURRENT_POINT_OPACITY = 0xA0;
	
	private boolean isTouchedLeft = false;
	private boolean isTouchedUp = false;
	private boolean isTouchedRight = false;
	private boolean isTouchedDown = false;
	
	private final Paint paint;
	private Bitmap resultBitmap;
	private Rect frame;
	
	private final int maskColor;
	private final int resultColor;
	private final int frameColor;
	private final int laserColor;
	private int scannerAlpha;
	  
	public boolean isTouchedLeft() {
		return isTouchedLeft;
	}
	
	public void setTouchedLeft(boolean isTouchedLeft) {
		this.isTouchedLeft = isTouchedLeft;
	}
	
	public boolean isTouchedUp() {
		return isTouchedUp;
	}
	public void setTouchedUp(boolean isTouchedUp) {
		this.isTouchedUp = isTouchedUp;
	}
	
	public boolean isTouchedRight() {
		return isTouchedRight;
	}
	public void setTouchedRight(boolean isTouchedRight) {
		this.isTouchedRight = isTouchedRight;
	}
	
	public boolean isTouchedDown() {
		return isTouchedDown;
	}
	public void setTouchedDown(boolean isTouchedDown) {
		this.isTouchedDown = isTouchedDown;
	}
	
	// This constructor is used when the class is built from an XML resource.
	public ViewfinderView(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		
		// Initialize these once for performance rather than calling them every time in onDraw().
		paint = new Paint();
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
	    resultColor = resources.getColor(R.color.result_view);
	    frameColor = 0x00ff00;
	    laserColor = resources.getColor(R.color.viewfinder_laser);
	    scannerAlpha = 0;
	}
	
	public void setFrame(Rect frame){
		this.frame = frame;
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		
		if (frame == null)
			return;
		
//		int width = canvas.getWidth();
//	    int height = canvas.getHeight();
	    
	    // Draw the exterior (i.e. outside the framing rect) darkened
	    paint.setColor(resultBitmap != null ? resultColor : maskColor);
	    
//	    canvas.drawRect(0, 0, width, frame.top, paint);
//	    canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
//	    canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
//	    canvas.drawRect(0, frame.bottom + 1, width, height, paint);
	    
	    if (resultBitmap != null) {
	    	// Draw the opaque result bitmap over the scanning rectangle
	    	paint.setAlpha(CURRENT_POINT_OPACITY);
	        canvas.drawBitmap(resultBitmap, null, frame, paint);
	    }
	    else {
	    	// Draw a two pixel solid black border inside the framing rect
	    	paint.setColor(frameColor);
	        paint.setAlpha(128);
	        
	        canvas.drawRect(frame.left, frame.top, frame.right + 1, frame.top + 2, paint);
	        canvas.drawRect(frame.left, frame.top + 2, frame.left + 2, frame.bottom - 1, paint);
	        canvas.drawRect(frame.right - 1, frame.top, frame.right + 1, frame.bottom - 1, paint);
	        canvas.drawRect(frame.left, frame.bottom - 1, frame.right + 1, frame.bottom + 1, paint);
	        if(isTouchedLeft){	      	  
	        }
	        
	        // Draw a red "laser scanner" line through the middle to show decoding is active
	        scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
	        paint.setColor(laserColor);
//		    paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
	        
	        if(isTouchedLeft)
			     canvas.drawRect(frame.left, frame.top, frame.left+5, frame.bottom, paint); // LEFT
	        if(isTouchedUp)
			     canvas.drawRect(frame.left, frame.top, frame.right, frame.top+5, paint); // TOP
	        if(isTouchedRight)
			     canvas.drawRect(frame.right-5, frame.top, frame.right, frame.bottom, paint); // Right
	        if(isTouchedDown)
			     canvas.drawRect(frame.left, frame.bottom-5, frame.right, frame.bottom, paint); // Bottom
	        
//	        int middle = frame.height() / 2 + frame.top;
//	        canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, paint);
	    }
	}
	
	public void drawTouchLine(){
		invalidate();
	}
}