package com.mydimoda.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.mydimoda.ParseApplication;
import com.mydimoda.R;
import com.mydimoda.constant;
import com.mydimoda.image.ImageLoader;
import com.mydimoda.widget.CircularProgressBar;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DMDeleteGridAdapter extends BaseAdapter {

    private final String TAG = "DMDeleteGridAdapter";
    private Context m_Context;
    private LayoutInflater layoutInflater;
    private List<String> mList = new ArrayList<String>();
    private ImageLoader mLoader;
    private DMListItemCallback mCallback;
   // private List<ParseObject> mClothList;
    ArrayList<Bitmap> mImagelists = new ArrayList<>();
    boolean isUploadStarted = false;


    public DMDeleteGridAdapter(Context context, List<String> list, List<ParseObject> mClothList, DMListItemCallback callback) {

        m_Context = context;
        layoutInflater = LayoutInflater.from(m_Context);
        mList = list;
        mLoader = new ImageLoader(m_Context);
        mCallback = callback;
     //   this.mClothList = mClothList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.grid_cloth_delete_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.item_view);
            holder.progress = (CircularProgressBar) convertView.findViewById(R.id.item_progress);
            holder.deleteBtn = (Button) convertView.findViewById(R.id.btn_delete);
            holder.formalBtn = (Button) convertView.findViewById(R.id.btn_formal);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.deleteBtn.setFocusable(false);
        holder.formalBtn.setFocusable(false);
        holder.deleteBtn.setVisibility(View.GONE);
        holder.formalBtn.setVisibility(View.GONE);
        //final String mbitKey = MemoryCacheUtils.findCacheKeysForImageUri(mList.get(position),ParseApplication.getInstance().mImageLoader.getMemoryCache()).get(0);
        // for getting
       /* ParseApplication.getInstance().mImageLoader.loadImage(mList.get(position), opts, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                //final Bitmap mbit =bitmap;
                //save image to parse

                if (position == mImagelists.size() && !mImagelists.contains(bitmap)) {
                    //mImagelists.add(position, ((BitmapDrawable) holder.imageView.getDrawable()).getBitmap());
                    mImagelists.add(position, bitmap);
                    if (!isUploadStarted) {
                        uploadSequencially(mClothList, mImagelists);
                    }
                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });*/

//for first image
        ParseApplication.getInstance().mImageLoader.displayImage(mList.get(position), holder.imageView, ParseApplication.getInstance().options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                System.out.println("DMDeleteGridAdapter.onLoadingStarted : " + imageUri);
                holder.progress.setProgress(0);
                holder.progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {
                holder.progress.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                System.out.println("DMDeleteGridAdapter.onLoadingStarted : loadedImage Bitmap : " + imageUri);
                // saveFile(loadedImage);
                holder.progress.setVisibility(View.GONE);
            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current,
                                         int total) {
                int progress = Math.round(100.0f * current / total);
                holder.progress.setProgress(progress);
                holder.progress.setTitle(String.valueOf(progress) + "%");
            }

        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                holder.deleteBtn.setVisibility(View.VISIBLE);
                holder.formalBtn.setVisibility(View.VISIBLE);
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mCallback != null)
                    mCallback.selectButton(position, 1);

                holder.deleteBtn.setVisibility(View.GONE);
                holder.formalBtn.setVisibility(View.GONE);
            }
        });

        holder.formalBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mCallback != null)
                    mCallback.selectButton(position, 2);

                holder.deleteBtn.setVisibility(View.GONE);
                holder.formalBtn.setVisibility(View.GONE);
            }
        });

        return convertView;
    }

    void saveFile(Bitmap bitmap) {
        String filename = "pippo.png";
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, filename);
        try {
            FileOutputStream out = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ViewHolder {
        ImageView imageView;
        CircularProgressBar progress;
        Button deleteBtn, formalBtn;
    }


    // mayur for uploading image on parse
    public void savePhotosToParse(final ParseObject mModel, Bitmap mBitmap, int pos) {

        constant.showProgressHard(m_Context, m_Context.getString(R.string.uploading_count_txt, pos + 1));
        byte[] byteArray = getByteArray(mBitmap);
        if (byteArray != null) {
            String category = "";


            ParseFile file = new ParseFile("image.jpg", byteArray);
            mModel.put("ImageContent", file);

            mModel.saveInBackground(new SaveCallback() {

                @Override
                public void done(ParseException e) {
                    // TODO Auto-generated method stub
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        ParseFile urlObject = (ParseFile) mModel.get("ImageContent");
                        String url = urlObject.getUrl();
                        Log.e("grid adapter", "image uploaded: " + url);
                    }
                    constant.hideProgress();

                }
            });
        }
    }

    //keep a count of uploaded images
    private int uploadCount = 0;

    public void uploadSequencially(final List<ParseObject> mClothList, final ArrayList<Bitmap> mImagelists) {

        constant.showProgressHard(m_Context, m_Context.getString(R.string.uploading_count_txt, uploadCount + 1));
        isUploadStarted = true;
        byte[] byteArray = getByteArray(mImagelists.get(uploadCount));
        if (byteArray != null) {
            final ParseObject mModel = mClothList.get(uploadCount);
            ParseFile file = new ParseFile("image.jpg", byteArray);
            mModel.put("ImageContent", file);
            mModel.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    // TODO Auto-generated method stub
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        ParseFile urlObject = (ParseFile) mModel.get("ImageContent");
                        String url = urlObject.getUrl();
                        Log.e("grid adapter", "image uploaded: " + url);
                    }
                    constant.hideProgress();
                    uploadCount++;
                    if (uploadCount < mImagelists.size() && mImagelists.size() <= mClothList.size()) {
                        uploadSequencially(mClothList, mImagelists);
                    } else {
                        isUploadStarted = false;
                    }
                }
            });
        }
    }


    public byte[] getByteArray(Bitmap mBitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] byteArray = null;
        try {
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byteArray = stream.toByteArray();
            stream.flush();
            stream.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return byteArray;
    }


}



