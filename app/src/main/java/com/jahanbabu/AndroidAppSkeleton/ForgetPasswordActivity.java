package com.jahanbabu.AndroidAppSkeleton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ForgetPasswordActivity extends AppCompatActivity {

	private EditText etEmail;
	private TextView tvSignin;
	private Button btnSubmit;

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);

		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/Raleway-Light.ttf")
//						.setDefaultFontPath("fonts/Digital_tech.otf")
						.setDefaultFontPath("fonts/ClassicRobot.otf")
//                .setDefaultFontPath("fonts/Roboto-Light.ttf")
						.setFontAttrId(R.attr.fontPath)
						.build()
		);

		getSupportActionBar().setTitle("Forgot Password");

        etEmail = (EditText) findViewById(R.id.etEmail);
        
        tvSignin = (TextView) findViewById(R.id.tvSignin);
        
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        
        tvSignin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent signinIntent = new Intent(ForgetPasswordActivity.this, SigninActivity.class);
				signinIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(signinIntent);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);
				finish();
			}
		});

        btnSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent secretIntent = new Intent(ForgetPasswordActivity.this, SigninActivity.class);
				secretIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(secretIntent);
				overridePendingTransition(R.anim.right_in, R.anim.right_out);
				finish();
			}
		});
    }
    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	Intent signinIntent = new Intent(ForgetPasswordActivity.this, SigninActivity.class);
		signinIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(signinIntent);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
		finish();
    	super.onBackPressed();
    }

    
}
