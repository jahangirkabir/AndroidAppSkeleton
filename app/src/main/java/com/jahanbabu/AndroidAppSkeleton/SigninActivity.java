package com.jahanbabu.AndroidAppSkeleton;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jahanbabu.AndroidAppSkeleton.Utils.Util;
import com.orhanobut.hawk.Hawk;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SigninActivity extends AppCompatActivity {
	private EditText emailEditText, passwordEditText;
	private String name, email, password, status, session;
	private TextView signupTextView, forgetPassTextView;
	private Button submitButton;
	Context context;
	ProgressDialog progressDialog;
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
		context = this;
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
						.setDefaultFontPath("fonts/ClassicRobot.otf")
						.setFontAttrId(R.attr.fontPath)
						.build()
		);

//        getSupportActionBar().setTitle("Signin");

		emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        
        signupTextView = (TextView) findViewById(R.id.signupTextView);
		forgetPassTextView = (TextView) findViewById(R.id.forgetPassTextView);
        
        submitButton = (Button) findViewById(R.id.submitButton);
        
        signupTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent signupIntent = new Intent(SigninActivity.this, SignupActivity.class);
				startActivity(signupIntent);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);
			}
		});

		forgetPassTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent forgetPassIntent = new Intent(SigninActivity.this,
						ForgetPasswordActivity.class);
				startActivity(forgetPassIntent);
				overridePendingTransition(R.anim.right_in, R.anim.right_out);
			}
		});
        
		submitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(Util.isNetworkAvailable(context)){
					email = emailEditText.getText().toString();
					password = passwordEditText.getText().toString();
					Timber.e("INPUT : " + email+" ---- "+password);

					if (!email.isEmpty() && !password.isEmpty()){
						final JSONObject mainObj = new JSONObject();
						JSONObject objbody = new JSONObject();
						try {
							objbody.put("email", email);
							objbody.put("password", password);

							//          main.put("token", token);
							mainObj.put("body", objbody);
							Timber.e("JSON : " + mainObj.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}
//						requestLogin(mainObj.toString());
						Intent mainIntent = new Intent(SigninActivity.this, MainActivity.class);
						startActivity(mainIntent);
						overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
						finish();
					} else
						Toast.makeText(SigninActivity.this, "Please enter Email and Password to signin!", Toast.LENGTH_LONG).show();

				} else {
					Util.showNoInternetDialog(context, SigninActivity.this);
				}
			}
		});
    }

	public void requestLogin(final String body){
		progressDialog = ProgressDialog.show(this, "","Verifying Please Wait...", true);
		// Instantiate the RequestQueue.
		RequestQueue queue = Volley.newRequestQueue(this);
		String url ="http://api.mysmartciti.com/iot/user/login";

		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						progressDialog.dismiss();
						Timber.e("RESPONSE : " + response);
						try {
							JSONObject mainJsonObject = new JSONObject(response);
							if (response.contains("Invalid User")){
								Toast.makeText(SigninActivity.this, "Invalid User!!!", Toast.LENGTH_LONG).show();
							} else {
								status = mainJsonObject.optString("status");
								if (status.equalsIgnoreCase("1111")){
									JSONObject bodyJsonObject = mainJsonObject.optJSONObject("body");
									session = bodyJsonObject.optString("session");
									name = bodyJsonObject.optString("name");
									if (session.length() > 5){
										Hawk.put("name", name);
										Hawk.put("email", email);
										Hawk.put("password", password);
										Hawk.put("session", session);
										Intent mainIntent = new Intent(SigninActivity.this, MainActivity.class);
										startActivity(mainIntent);
										overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
										finish();
									} else
										Toast.makeText(SigninActivity.this, "Email or Password is not valid!", Toast.LENGTH_LONG).show();
								} else
									Toast.makeText(SigninActivity.this, "Email or Password is not valid!", Toast.LENGTH_LONG).show();
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				progressDialog.dismiss();
				Timber.e("ERROR : " + error);
				Toast.makeText(SigninActivity.this, "Server Error, Please try again!", Toast.LENGTH_LONG).show();
			}
		}){
			@Override
			public String getBodyContentType() {
				return "application/json; charset=utf-8";
			}

			@Override
			public byte[] getBody() throws AuthFailureError {
				return body.getBytes();
			}
		};
		// Add the request to the RequestQueue.
		queue.add(stringRequest);
	}

}
