package com.mydimoda;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mydimoda.widget.MyVideoView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DMHomeFragment extends Fragment {

	View vEmpty;
	MyVideoView vHomeVideo;

	DMHomeActivity mActivity;
	float mVideoWidth;
	float mVideoHeight;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		vHomeVideo = (MyVideoView) view.findViewById(R.id.home_videoview);
		vEmpty = (View) view.findViewById(R.id.view1);
		TextView tvPrivacyPolicy = (TextView) view.findViewById(R.id.tvPrivacyPolicy);

		vEmpty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mActivity.slideMenu();
			}
		});

		tvPrivacyPolicy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacypolicyurl)));
				startActivity(browserIntent);
			}
		});

		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		init();
	}

	public void init() {
		calculateVideoSize();
		playVideo();
		// t.start();

	}

	// / --------------------------------- play video
	// --------------------------------------
	public void playVideo() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		int screenHeight = displaymetrics.heightPixels;
		int screenWidth = displaymetrics.widthPixels;

		String uri = "android.resource://" + getActivity().getPackageName()
				+ "/" + R.raw.latest;

		if (mVideoWidth / screenWidth > mVideoHeight / screenHeight) {
			int videoViewWidth = (int) (screenHeight / mVideoHeight * mVideoWidth);
			int xOffset = (screenWidth - videoViewWidth) / 2;
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) vHomeVideo
					.getLayoutParams();
			params.setMargins(xOffset, 0, xOffset, 0);
			vHomeVideo.setLayoutParams(params);
			vHomeVideo.setVideoSize(videoViewWidth, screenHeight);
		} else {

			int videoViewHeight = (int) (screenWidth / mVideoWidth * mVideoHeight);
			int yOffset = (screenHeight - videoViewHeight) / 2;
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) vHomeVideo
					.getLayoutParams();
			params.setMargins(0, yOffset, 0, yOffset);
			vHomeVideo.setLayoutParams(params);
			vHomeVideo.setVideoSize(screenWidth, videoViewHeight);
		}

		vHomeVideo.setVideoURI(Uri.parse(uri));
		vHomeVideo.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.setLooping(true);
			}
		});
		vHomeVideo.requestFocus();
		vHomeVideo.start();

	}

	// /// ---------------------------------- tick function call section
	// CountDownTimer t = new CountDownTimer( Long.MAX_VALUE , 1000) {
	// public void onTick(long millisUntilFinished) {
	//
	// if(!vHomeVideo.isPlaying())
	// {
	// vHomeVideo.start();
	// }
	// }
	// public void onFinish() {
	// Log.d("test","Timer last tick");
	// }
	//
	// }.start();

	@Override
	public void onPause() {
		// t.cancel();
		super.onPause();
	}

	private void calculateVideoSize() {
		try {
			// AssetFileDescriptor afd = getAssets().openFd("home_video.mp4");
			/*
			 * AssetFileDescriptor afd = getResources().
			 * openRawResourceFd(R.raw.home_video); MediaMetadataRetriever
			 * metaRetriever = new MediaMetadataRetriever();
			 * metaRetriever.setDataSource( afd.getFileDescriptor(),
			 * afd.getStartOffset(), afd.getLength()); String height =
			 * metaRetriever
			 * .extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT
			 * ); String width = metaRetriever
			 * .extractMetadata(MediaMetadataRetriever
			 * .METADATA_KEY_VIDEO_WIDTH);
			 */
			mVideoHeight = Float.parseFloat("1280");
			mVideoWidth = Float.parseFloat("720");

		} catch (NumberFormatException e) {
			// Log.d(TAG, e.getMessage());
		} catch (Exception exception) {

		}
	}

}
