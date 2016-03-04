package com.mydimoda.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.zlightness;
import com.mydimoda.DMCaptureOptionActivity;
import com.mydimoda.DMHangUpActivity;
import com.mydimoda.R;
import com.mydimoda.constant;
import com.mydimoda.adapter.DMMenuListAdapter;
import com.mydimoda.widget.cropper.util.FontsUtil;

public class CameraActivity extends Activity implements SurfaceHolder.Callback,
		Camera.PreviewCallback, OnClickListener {

	public static final String LOGTAG = "CameraActivity";
	private static final Pattern COMMA_PATTERN = Pattern.compile(",");

	private Context context;
	private Camera camera;
	private Camera.Parameters cameraParameters;

	private SurfaceView cameraView;
	private SurfaceHolder holder;
	private ViewfinderView viewfinderView;
	private TextView msg_view;
	private TextView result_view;

	private zlightness m_zlightness;

	private Point screenResolution;
	private Point cameraResolution;
	private boolean previewRunning = false;

	private String imageUri = "";

	private byte[] previewCallbackBuffer;

	ListView vMenuList;
	LinearLayout vMenuLayout;
	DrawerLayout vDrawerLayout;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.activity_camera);

		this.context = getApplication();
		TextView typeText = (TextView) findViewById(R.id.typeText);
		TextView back_txt = (TextView) findViewById(R.id.back_txt);

		FontsUtil.setExistenceLight(this, back_txt);
		FontsUtil.setExistenceLight(this, typeText);

		cameraView = (SurfaceView) findViewById(R.id.CameraView);
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		viewfinderView.setVisibility(View.VISIBLE);

		vDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		vMenuList = (ListView) findViewById(R.id.menu_list);
		vMenuLayout = (LinearLayout) findViewById(R.id.menu_layout);

		m_zlightness = new zlightness();

		holder = cameraView.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		msg_view = (TextView) findViewById(R.id.msg_textview);
		msg_view.setTextColor(Color.GREEN);
		msg_view.setVisibility(View.INVISIBLE);
		msg_view.setText("Light : 0");

		result_view = (TextView) findViewById(R.id.result_textview);
		FontsUtil.setExistenceLight(this, result_view);

		result_view.setTextColor(Color.RED);
		result_view.setText("");

		vMenuList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				vDrawerLayout.closeDrawer(vMenuLayout);

				if (position != 2)
					constant.selectMenuItem(CameraActivity.this, position, true);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(LOGTAG, "onActivityResult requestCode " + requestCode);
	}

	@Override
	public void onResume() {

		super.onResume();

		showMenu();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {

			Camera.CameraInfo info = new Camera.CameraInfo();

			for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
				Camera.getCameraInfo(i, info);

				if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
					camera = Camera.open(i);
					break;
				} else {
					camera = Camera.open(i);
					break;
				}
			}
		}

		if (camera == null)
			camera = Camera.open();

		if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
			camera.setDisplayOrientation(90);
		else
			camera.setDisplayOrientation(0);

		camera.startPreview();
		previewRunning = true;
	}

	@Override
	public void onPause() {
		if (previewRunning) {
			camera.stopPreview();
			camera.setPreviewCallback(null);
		}

		camera.release();
		camera = null;
		previewRunning = false;

		super.onPause();
	}

	@Override
	public void onClick(View v) {

		Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.button_click);
		v.startAnimation(anim);

		switch (v.getId()) {
		case R.id.captureBtn: {
			Intent intent = getIntent();
			imageUri = intent.getStringExtra("photoname");
			camera.takePicture(null, null, mPicture);
			break;
		}
		case R.id.back_layout: {
			Intent intent = new Intent(CameraActivity.this,
					DMCaptureOptionActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		case R.id.menu_btn:
			slideMenu();
			break;
		default:
			break;
		}
	}

	@Override
	public void onConfigurationChanged(Configuration conf) {
		super.onConfigurationChanged(conf);
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	@SuppressWarnings("deprecation")
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			camera.setPreviewDisplay(holder);
			cameraParameters = camera.getParameters();

			WindowManager manager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			Display display = manager.getDefaultDisplay();
			screenResolution = new Point(display.getWidth(),
					display.getHeight());
			Log.d(LOGTAG, "Screen resolution: " + screenResolution);
			cameraResolution = getCameraResolution(cameraParameters,
					screenResolution);

			// m_zcard.create(cameraResolution.x, cameraResolution.y, 0.636);

			camera.startPreview();
			previewRunning = true;
		} catch (IOException e) {
			Log.e(LOGTAG, e.getMessage());
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (previewRunning) {
			camera.stopPreview();
		}

		try {
			cameraParameters.setPreviewSize(cameraResolution.x,
					cameraResolution.y);
			List<String> focusModes = cameraParameters.getSupportedFocusModes();
			if (focusModes
					.contains(cameraParameters.FOCUS_MODE_CONTINUOUS_VIDEO))
				cameraParameters
						.setFocusMode(cameraParameters.FOCUS_MODE_CONTINUOUS_VIDEO);

			// List<Size> sizes = cameraParameters.getSupportedPictureSizes();
			// cameraParameters.setPictureSize(sizes.get(0).width,
			// sizes.get(0).height);
			camera.setParameters(cameraParameters);

			camera.setPreviewDisplay(holder);

			previewCallbackBuffer = new byte[(cameraParameters.getPreviewSize().width
					* cameraParameters.getPreviewSize().height * 3)];
			camera.addCallbackBuffer(previewCallbackBuffer);
			camera.setPreviewCallbackWithBuffer(this);
			previewRunning = true;

			camera.setPreviewCallback(this);
			camera.startPreview();
		} catch (IOException e) {
			Log.e(LOGTAG, e.getMessage());
			e.printStackTrace();
		}
	}

	public void onPreviewFrame(byte[] b, Camera c) {
		previewCallbackBuffer = b;

		float rlight = m_zlightness.calc(b, cameraResolution.x,
				cameraResolution.y);
		float light = rlight / 255;

		msg_view.setText("Value : " + String.valueOf(light));

		String result = "";
		int color = Color.RED;
		if (light >= 0.6f && light <= 1) {
			result = "Light is good, take your picture";
			color = Color.GREEN;
		} else if (light >= 0.4f && light < 0.6f) {
			result = "Light is ok, results might suffer. \nTry more light behind you, facing the items";
			color = Color.BLUE;
		} else {
			result = "Light is bad. \nTry more light behind you, facing the items";
			color = Color.RED;
		}

		result_view.setText(result);
		result_view.setTextColor(color);

		viewfinderView.drawTouchLine();
	}

	public static byte[] convertYUV420_NV21toRGB888(byte[] data, int width,
			int height) {

		int size = width * height;
		int pixeloffset = width * 3;

		byte[] pixels = new byte[size * 3];

		int offset = size;
		int u, v, y1, y2, y3, y4;
		int m = 0;

		for (int i = 0, k = 0; i < size; i += 2, k += 2, m += 6) {

			y1 = data[i] & 0xff;
			y2 = data[i + 1] & 0xff;
			y3 = data[width + i] & 0xff;
			y4 = data[width + i + 1] & 0xff;

			u = data[offset + k] & 0xff;
			v = data[offset + k + 1] & 0xff;
			u = u - 128;
			v = v - 128;

			int r1 = (int) 1.402f * v;
			int g1 = (int) (0.344f * u + 0.714f * v);
			int b1 = (int) 1.772f * u;

			pixels[m] = (byte) (y1 + r1);
			pixels[m + 1] = (byte) (y1 - g1);
			pixels[m + 2] = (byte) (y1 + b1);
			pixels[m + 3] = (byte) (y2 + r1);
			pixels[m + 4] = (byte) (y2 - g1);
			pixels[m + 5] = (byte) (y2 + b1);
			pixels[pixeloffset + m] = (byte) (y3 + r1);
			pixels[pixeloffset + m + 1] = (byte) (y3 - g1);
			pixels[pixeloffset + m + 2] = (byte) (y3 + b1);
			pixels[pixeloffset + m + 3] = (byte) (y4 + r1);
			pixels[pixeloffset + m + 4] = (byte) (y4 - g1);
			pixels[pixeloffset + m + 5] = (byte) (y4 + b1);

			if (i != 0 && (i + 2) % width == 0) {
				i += width;
				m += pixeloffset;
			}
		}

		return pixels;
	}

	private static Point getCameraResolution(Camera.Parameters parameters,
			Point screenResolution) {

		String previewSizeValueString = parameters.get("preview-size-values");

		// saw this on Xperia
		if (previewSizeValueString == null)
			previewSizeValueString = parameters.get("preview-size-value");

		Point cameraResolution = null;

		if (previewSizeValueString != null) {
			Log.d(LOGTAG, "preview-size-values parameter: "
					+ previewSizeValueString);
			cameraResolution = findBestPreviewSizeValue(previewSizeValueString,
					screenResolution);
		}

		if (cameraResolution == null) {
			// Ensure that the camera resolution is a multiple of 8, as the
			// screen may not be.
			cameraResolution = new Point((screenResolution.x >> 3) << 3,
					(screenResolution.y >> 3) << 3);
		}

		return cameraResolution;
	}

	private static Point findBestPreviewSizeValue(
			CharSequence previewSizeValueString, Point screenResolution) {

		int bestX = 0;
		int bestY = 0;
		int diff = Integer.MAX_VALUE;

		for (String previewSize : COMMA_PATTERN.split(previewSizeValueString)) {

			previewSize = previewSize.trim();
			int dimPosition = previewSize.indexOf('x');
			if (dimPosition < 0) {
				Log.w(LOGTAG, "Bad preview-size: " + previewSize);
				continue;
			}

			int newX;
			int newY;
			try {
				newX = Integer.parseInt(previewSize.substring(0, dimPosition));
				newY = Integer.parseInt(previewSize.substring(dimPosition + 1));
			} catch (NumberFormatException nfe) {
				Log.w(LOGTAG, "Bad preview-size: " + previewSize);
				continue;
			}

			int newDiff = Math.abs(newX - screenResolution.x)
					+ Math.abs(newY - screenResolution.y);
			if (newDiff == 0) {
				bestX = newX;
				bestY = newY;
				break;
			} else if (newDiff < diff) {
				bestX = newX;
				bestY = newY;
				diff = newDiff;
			}
		}

		if (bestX > 0 && bestY > 0)
			return new Point(bestX, bestY);

		return null;
	}

	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			if (imageUri.equals(""))
				return;

			File photoFile = new File(imageUri);

			try {
				FileOutputStream fos = new FileOutputStream(photoFile);
				fos.write(data);
				fos.flush();
				fos.close();

				if (getParent() == null)
					setResult(RESULT_OK);
				else
					getParent().setResult(RESULT_OK);

				finish();
			} catch (FileNotFoundException e) {
				Log.d(LOGTAG, "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d(LOGTAG, "Error accessing file: " + e.getMessage());
			}
		}
	};

	public void showMenu() {
		vMenuList.setAdapter(new DMMenuListAdapter(this, constant.gMenuList));
	}

	public void slideMenu() {
		if (vDrawerLayout.isDrawerOpen(vMenuLayout)) {
			vDrawerLayout.closeDrawer(vMenuLayout);
		} else
			vDrawerLayout.openDrawer(vMenuLayout);
	}
}