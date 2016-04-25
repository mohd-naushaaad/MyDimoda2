package com.mydimoda;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.mydimoda.database.DbAdapter;
import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class DMLoginActivity extends Activity {

    Button vBtnFacebook, vBtnSignUp, vBtnLogin;
    EditText vUsername, vPassword;
    TextView vTxtRemember;
    ImageButton vBtnRemember;
    ProgressDialog vProgress;

    boolean mIsRemember = true;
    DbAdapter mDbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//		mDbAdapter = new DbAdapter(DMLoginActivity.this);
//		mDbAdapter.createDatabase();
//		mDbAdapter.open();
        new CreateDbTask().execute();
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.mydimoda", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String strHashKey = Base64.encodeToString(md.digest(),
                        Base64.DEFAULT);
                Log.d("Testing:", "Hi key ::" + strHashKey);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        vBtnFacebook = (Button) findViewById(R.id.facebook_btn);
        vBtnSignUp = (Button) findViewById(R.id.signup_btn);
        vBtnLogin = (Button) findViewById(R.id.login_btn);
        vUsername = (EditText) findViewById(R.id.username_view);
        vPassword = (EditText) findViewById(R.id.pass_view);
        vTxtRemember = (TextView) findViewById(R.id.remember_view);
        vBtnRemember = (ImageButton) findViewById(R.id.remember_btn);
        Button forgotPasswordButton = (Button) findViewById(R.id.forgot_password_btn);
        forgotPasswordButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (AppUtils.isConnectingToInternet(DMLoginActivity.this)) {
                    Intent intent = new Intent(DMLoginActivity.this,
                            DMResetPasswordActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(DMLoginActivity.this, getString(R.string.no_internet_msg), Toast.LENGTH_LONG).show();
                }

            }
        });

        init();
        vBtnRemember.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mIsRemember) {
                    mIsRemember = false;
                    vBtnRemember.setBackgroundResource(R.drawable.remember_bg);
                } else {
                    mIsRemember = true;
                    vBtnRemember.setBackgroundResource(R.drawable.remember_checked_bg);
                }
            }
        });

        vBtnFacebook.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (AppUtils.isConnectingToInternet(DMLoginActivity.this)) {
                    loginWithFacebook();
                } else {
                    Toast.makeText(DMLoginActivity.this, getString(R.string.no_internet_msg), Toast.LENGTH_LONG).show();
                }
            }
        });

        vBtnSignUp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (AppUtils.isConnectingToInternet(DMLoginActivity.this)) {
                    Intent intent = new Intent(DMLoginActivity.this,
                            DMSignUpActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(DMLoginActivity.this, getString(R.string.no_internet_msg), Toast.LENGTH_LONG).show();
                }

            }
        });

        vBtnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (AppUtils.isConnectingToInternet(DMLoginActivity.this)) {
                    loginToParse();
                } else {
                    Toast.makeText(DMLoginActivity.this, getString(R.string.no_internet_msg), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void init() {
        // / Global font setting
        constant.boldfontface = Typeface.createFromAsset(getAssets(),
                "Brixton Bold.ttf");
        constant.fontface = Typeface.createFromAsset(getAssets(),
                "Brixton Light.ttf");
        setViewWithFont();
        if (getUserData()) {
            goHomeActivity();
        }
    }

    // / -------------------------------------- font set -------------------
    public void setViewWithFont() {
        vUsername.setTypeface(constant.fontface);
        vPassword.setTypeface(constant.fontface);
        vBtnSignUp.setTypeface(constant.fontface);
        vBtnLogin.setTypeface(constant.fontface);
        vTxtRemember.setTypeface(constant.fontface);
        Button forgotPasswordButton = (Button) findViewById(R.id.forgot_password_btn);
        forgotPasswordButton.setTypeface(constant.fontface);
    }

    // /--------------------------------------- login with facebook data
    // -----------------------------------------
    public void loginWithFacebook() {
        constant.showProgress(this, "Logging in..");
        List<String> permissions = Arrays.asList("email","basic_info");
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Toast.makeText(
                            DMLoginActivity.this,
                            "Uh oh. The user cancelled the Facebook login.",
                            Toast.LENGTH_LONG).show();
                    // constant.hideProgress();
                } else if (user.isNew()) {
                    Log.d("MyApp",
                            "User signed up and logged in through Facebook!");
                    AppUtils.getDefaults(DMLoginActivity.this, constant.PREF_IS_GALRY_DIALOG_SHOWN, false);
                    saveUserData(DMLoginActivity.this);
                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
                    saveUserData(DMLoginActivity.this);
                }

            }
        });
    }
// updated according to new sdk
    public void saveUserData(final Activity activity) {
       /* Session session = ParseFacebookUtils.getSession();
        if (session == null || session.isOpened() == false)
            return;*/

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        Log.e("dmlogin", object.toString() + " response: "+response.getRawResponse());
                        constant.gUserName = object.optString("name");;

                        if (object.has("email")) {
                            constant.gEmail = object.optString("email");
                        }
                        // / save data to parse.com
                        final ParseUser parseUser = ParseUser.getCurrentUser();
                        constant.gUserId = parseUser.getObjectId();

                        if (!constant.gUserName.equals(""))
                            parseUser.put("username", constant.gUserName);
                        if (!constant.gEmail.equals("")){
                            parseUser.put("email", constant.gEmail);
                        }
                        parseUser.put("loggedInWay", "facebook");

                        // if(parseUser.isNew())
                        {
                            parseUser.setUsername(constant.gUserName);
                            if (TextUtils.isEmpty(parseUser.getEmail()) && !TextUtils.isEmpty(constant.gEmail)) {
                                parseUser.setEmail(constant.gEmail);
                            }
                            parseUser.setPassword("");
                        }

                        parseUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException arg0) {
                                // TODO Auto-generated method stub
                                if (arg0 == null) {
                                    if (!ParseFacebookUtils.isLinked(parseUser)) {
                                        ParseFacebookUtils.linkInBackground(parseUser,
                                                AccessToken.getCurrentAccessToken(), new SaveCallback() {

                                                    @Override
                                                    public void done(
                                                            ParseException arg0) {
                                                        // TODO Auto-generated
                                                        // method stub
                                                        if (ParseFacebookUtils
                                                                .isLinked(parseUser)) {
                                                            Log.d("Wooho",
                                                                    "user logged in with Facebook");
                                                         //   ParseFacebookUtils.save
                                                          //          .saveLatestSessionData(parseUser);
                                                        }
                                                    }

                                                });
                                    }
                                    saveUserToInstallation(parseUser);
                                } else {
                                    Toast.makeText(DMLoginActivity.this,
                                            AppUtils.asUpperCaseFirstChar(arg0.getMessage()), Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });
                    }
                });
        request.executeAsync();
    }

    // / -------------------------------------- login to parse.com
    // -----------------------------------------------
    @SuppressLint("DefaultLocale")
    public void loginToParse() {
        String username = vUsername.getText().toString().toLowerCase().trim();
        final String password = vPassword.getText().toString();

        if (username.equals("") || password.equals("")) {
            constant.alertbox("Warning!", "Please enter username & password.",
                    this);
        } else {
            constant.showProgress(this, "Submit Login");
            ParseUser.logInInBackground(username, password,
                    new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {

                            if (user != null) {

                                boolean isVerify = user
                                        .getBoolean("emailVerified");
                                if (isVerify) {
                                    constant.gUserName = user.getUsername();
                                    constant.gEmail = user.getEmail();
                                    constant.gUserId = user.getObjectId();
                                    constant.gPassword = password;
                                    constant.gIsCloset = user.getBoolean("isDemoCloset");
                                    saveUserToInstallation(user);
                                } else {
                                    // Toast.makeText(DMLoginActivity.this,
                                    // "Please verify email.",
                                    // Toast.LENGTH_LONG).show();
                                    constant.hideProgress();
                                    showVerifyDialog();
                                }
                            } else {
                                Toast.makeText(DMLoginActivity.this,
                                        AppUtils.asUpperCaseFirstChar(e.getMessage()), Toast.LENGTH_LONG).show();
                                constant.hideProgress();
                            }
                        }
                    });
        }
    }

    // / --------------------------------------------------- save user to
    // installation --------------------
    public void saveUserToInstallation(ParseUser user) {
        ParseInstallation installation = ParseInstallation
                .getCurrentInstallation();
        installation.put("User", user);
        installation.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException arg0) {
                // TODO Auto-generated method stub
                constant.hideProgress();
                goHomeActivity();
            }
        });
    }

    // / ---------------------------------- go to home activity
    // ------------------------------------
    public void goHomeActivity() {
        Intent intent = new Intent(DMLoginActivity.this, DMHomeActivity.class);
        startActivity(intent);
        finish();
    }

    // / --------------------------------- save user data to shared preference
    // And get user data from shared preference
    // --------------------------------------
    public void saveUserData() {
        SharedPreferences settings = getSharedPreferences(constant.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString("username", constant.gUserName);
        editor.putString("email", constant.gEmail);
        editor.putString("password", constant.gPassword);
        editor.putString("userid", constant.gUserId);
        editor.putBoolean("isCloset", constant.gIsCloset);


        System.out.println("isCLosetLogin" + constant.gIsCloset);
        editor.commit();
    }

    public boolean getUserData() {
        SharedPreferences settings = getSharedPreferences(constant.PREFS_NAME, 0);
        constant.gUserName = settings.getString("username", "");
        constant.gEmail = settings.getString("email", "");
        constant.gPassword = settings.getString("password", "");
        constant.gUserId = settings.getString("userid", "");
        constant.gIsCloset = settings.getBoolean("isCloset", false);

        if (constant.gUserId.equals(""))
            return false;
        else
            return true;
    }

    // /
    // ------------------------------------------------------------------------------------

    public void showVerifyDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.empty);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.setCanceledOnTouchOutside(true);

        TextView title = (TextView) dialog.findViewById(R.id.title);
        FontsUtil.setExistenceLight(this, title);
        title.setText("Oops!\nPlease check your mail box and verify your account.");

        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        FontsUtil.setExistenceLight(this, okBtn);
        okBtn.setText("OK");
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
        FontsUtil.setExistenceLight(this, cancelBtn);
        cancelBtn.setText("Cancel");
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);// mayur updated
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!constant.gUserName.equals("") && !constant.gPassword.equals("")) {
            vUsername.setText(constant.gUserName);
            vPassword.setText(constant.gPassword);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mIsRemember) {
            saveUserData();
        }
    }

    public class CreateDbTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            mDbAdapter = new DbAdapter(DMLoginActivity.this);
            mDbAdapter.createDatabase();
            mDbAdapter.open();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
}
