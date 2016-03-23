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

    /**
     * get dialog's interface to dismiss the dialog when required
     * @param mDialogInterface
     */
    void onDialogVisible(DialogInterface mDialogInterface);

}
