package com.mydimoda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mydimoda.widget.cropper.util.FontsUtil;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class DMResetPasswordActivity extends Activity {
	TextView title_layout1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resetpassword);

		title_layout1 = (TextView) findViewById(R.id.title_layout1);
		FontsUtil.setLight(this, title_layout1);

		Button resetPasswordButton = (Button) findViewById(R.id.resetpassword_btn);
		FontsUtil.setLight(this, resetPasswordButton);
		resetPasswordButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (AppUtils.isConnectingToInternet(DMResetPasswordActivity.this)) {
					resetPassword();
				} else {
					Toast.makeText(DMResetPasswordActivity.this, getString(R.string.no_internet_msg), Toast.LENGTH_LONG).show();
				}
			}
		});

		Button backButton = (Button) findViewById(R.id.back_btn);
		FontsUtil.setLight(this, backButton);

		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DMResetPasswordActivity.this,
						DMLoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void resetPassword() {
		EditText emailText = (EditText) findViewById(R.id.email_view);
		FontsUtil.setLight(this, emailText);
		if (emailText.getText().toString().isEmpty()) {
			Toast.makeText(DMResetPasswordActivity.this,
					"Please enter your email address.", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		ParseUser.requestPasswordResetInBackground(emailText.getText()
				.toString(), new RequestPasswordResetCallback() {
			@Override
			public void done(com.parse.ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					Toast.makeText(DMResetPasswordActivity.this,
							"Please check your email.", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(
							DMResetPasswordActivity.this,
							"That email is not registered or unknown error.",
							Toast.LENGTH_SHORT).show();
				}
			}

		});
	}
}
