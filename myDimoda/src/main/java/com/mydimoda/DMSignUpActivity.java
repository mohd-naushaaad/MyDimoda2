package com.mydimoda;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class DMSignUpActivity extends Activity {

	Button vBtnSignUp, vBtnBack;
	EditText vUsername, vEmail, vPassword;
	ProgressDialog vProgress;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);

		context = this;
		vBtnSignUp = (Button) findViewById(R.id.signup_btn);
		FontsUtil.setLight(this, vBtnSignUp);

		vBtnBack = (Button) findViewById(R.id.back_btn);
		FontsUtil.setLight(this, vBtnBack);

		vUsername = (EditText) findViewById(R.id.username_view);
		FontsUtil.setLight(this, vUsername);

		vEmail = (EditText) findViewById(R.id.email_view);
		FontsUtil.setLight(this, vEmail);

		vPassword = (EditText) findViewById(R.id.pass_view);
		FontsUtil.setLight(this, vPassword);

		vBtnBack.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		vBtnSignUp.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				preSignup();
			}
		});
	}

	// / ------------------------------------ previous process before sign up to
	// parse.com----------------
	public void preSignup() {
		String username = vUsername.getText().toString().toLowerCase();
		String email = vEmail.getText().toString();
		String password = vPassword.getText().toString();

		if (username.equals("") || email.equals("") || password.equals("")) {
			constant.alertbox("Warning!",
					"Please fill username, email, password.",
					DMSignUpActivity.this);
		} else {
			signupToParse(username, email, password);
		}

	}

	// / ----------------------------------- sign up to parse.com
	// --------------------------------------
	public void signupToParse(final String username, final String email,
			final String pass) {
		ParseUser user = new ParseUser();
		user.setUsername(username);
		user.setPassword(pass);
		user.setEmail(email);

		constant.showProgress(this, "Registering");
		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {

				constant.hideProgress();
				if (e == null) {
					constant.gUserName = username;
					constant.gPassword = pass;
					constant.gEmail = email;
					finish();
				} else {
					Toast.makeText(DMSignUpActivity.this, e.toString(),
							Toast.LENGTH_LONG).show();

//					if (e.toString().contains("already taken")) {
//						Toast.makeText(
//								DMSignUpActivity.this,
//								context.getResources() .getString( R.string.user_name_cn) + constant.gUserName
//								+ context .getResources() .getString( R.string.user_alreadTaken_cn),
//								Toast.LENGTH_LONG).show();
//
//					}
					
				/*	String test=e.toString();
					try {
					        String translatedText = Translate.execute(test, Language.ENGLISH, Language.CHINESE_SIMPLIFIED);//You can pass params as per text input and desired output.
					        System.out.println(translatedText);
					        Toast.makeText(DMSignUpActivity.this, translatedText,
									Toast.LENGTH_LONG).show();
					    } catch (Exception e1) {
					        e1.printStackTrace();
					    }*/
					
					// show custom toast here.
					// already register

				}
			}
		});
	}
}
