package com.mydimoda.interfaces;

import android.content.DialogInterface;

/**
 * Created by Mayur on 22-03-2016.
 */
public interface DialogItemClickListener {
    void onImageClick(String imagePath);
    /**
     * start gallery intent on this click
     */
    void onGalleryClick();

    void onCloseClick();

    void onDialogVisible(DialogInterface mDialogInterface);

}
