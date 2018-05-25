package com.mydimoda;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import org.acra.annotation.ReportsCrashes;

import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@ReportsCrashes(mailTo = "nirmal@solulab.com")
public class ParseApplication extends Application {

    static volatile ParseApplication application = null;
    public DisplayImageOptions options;
    public ImageLoader mImageLoader = ImageLoader.getInstance();
    Context mContext;

    public ParseApplication() {
        super();
        application = this;
    }

    public static ParseApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        Fabric.with(this, new Crashlytics());
        mContext = this;
        try {
            //ACRA.init(this); // Open only in development mode.
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Keyur
        //Starting helper service.
        mContext.startService(new Intent(this, ServiceHelper.class));

        initImageLoader(getApplicationContext());
        // Use for troubleshooting -- remove this line for production
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        // Use for monitoring Parse OkHttp traffic
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);
        /*Parse.initialize(this, "SPi9A2wXDrfMonNm9PBbJzumanlXMiEYfqpRCkJd",
                "MGuamZwIkAVisZWyZ5ZGqGR50Cl42kOAhNNgFQLm");*/
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("SPi9A2wXDrfMonNm9PBbJzumanlXMiEYfqpRCkJd") // should correspond to APP_ID env variable
                .clientKey("MGuamZwIkAVisZWyZ5ZGqGR50Cl42kOAhNNgFQLm")  // set explicitly unless clientKey is explicitly configured on Parse server
                .clientBuilder(builder)
                .server("http://52.25.182.16:1337/parse/").build());

//		ParseFacebookUtils.initialize("608361809277602");
        ParseFacebookUtils.initialize(this);//mayur updated
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);

        ParseUser.enableAutomaticUser();

        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);

        SharedPreferenceUtil.init(this);
        SharedPreferenceUtil.save();

    }

    public void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                .threadPoolSize(1)
                // 50 Mb
                .diskCacheExtraOptions(480, 320, null)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.white_bg)
                .showImageForEmptyUri(R.drawable.white_bg)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }
}
