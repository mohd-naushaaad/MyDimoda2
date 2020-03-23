package com.mydimoda;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by Mayur on 06-04-17.
 */

public class UploadPicService extends IntentService {
    byte[] byteArray;
    int pos;
    public static List<ParseObject> mClothList;
    final String TAG = "UploadPicService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UploadPicService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        byteArray = intent.getByteArrayExtra(constant.IMAGE_BYTEARRY_KEY);
        pos = intent.getIntExtra(constant.IMAGE_POS_KEY, -1);

        if (pos > -1) {

        } else {
            Log.e(TAG, "cant get pos from intent");
        }

    }

    private void uploadImage(ParseObject mObject, byte[] byteArray) {
        if (byteArray != null) {
            String category = "";

            final ParseObject mModel = mObject;
            ParseFile file = new ParseFile("image.jpg", byteArray);
            mModel.put("ImageContent", file);
            mModel.saveEventually(new SaveCallback() { // no need for save in background, as in a separate thread already
                @Override
                public void done(ParseException e) {
                    // TODO Auto-generated method stub
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        ParseFile urlObject = (ParseFile) mModel.get("ImageContent");
                        String url = urlObject.getUrl();
                        Log.e(TAG, "image uploaded: " + url);
                    }
                }
            });
        }
    }

}
