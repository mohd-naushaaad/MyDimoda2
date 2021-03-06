package com.mydimoda.widget.cropper.cropwindow.handle;

import android.graphics.Rect;

import com.mydimoda.widget.cropper.cropwindow.edge.Edge;
import com.mydimoda.widget.cropper.cropwindow.edge.EdgePair;

class CornerHandleHelper extends HandleHelper {
	
	CornerHandleHelper(Edge horizontalEdge, Edge verticalEdge) {
        super(horizontalEdge, verticalEdge);
    }
	
	@Override
    void updateCropWindow(float x, float y,
                          float targetAspectRatio, Rect imageRect, float snapRadius) {

        final EdgePair activeEdges = getActiveEdges(x, y, targetAspectRatio);
        final Edge primaryEdge = activeEdges.primary;
        final Edge secondaryEdge = activeEdges.secondary;

        primaryEdge.adjustCoordinate(x, y, imageRect, snapRadius, targetAspectRatio);
        secondaryEdge.adjustCoordinate(targetAspectRatio);

        if (secondaryEdge.isOutsideMargin(imageRect, snapRadius)) {
            secondaryEdge.snapToRect(imageRect);
            primaryEdge.adjustCoordinate(targetAspectRatio);
        }
    }
}