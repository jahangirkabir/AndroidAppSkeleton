package com.jahanbabu.AndroidAppSkeleton;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.jahanbabu.AndroidAppSkeleton.Utils.Util;
import com.orhanobut.hawk.Hawk;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SigninActivity extends AppCompatActivity implements
		GoogleApiClient.OnConnectionFailedListener{
	private EditText emailEditText, passwordEditText;
	private String name, email, password, status, session;
	private TextView signupTextView, forgetPassTextView;
	private Button submitButton;
	Context context;
	ProgressDialog progressDialog;
	SignInButton signInButton;
	private static final String TAG = "GoogleActivity";
	private static final int RC_SIGN_IN = 9001;

	// [START declare_auth]
	private FirebaseAuth mAuth;
	// [END declare_auth]

	private GoogleApiClient mGoogleApiClient;

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	@Override
	public void onStart() {
		super.onStart();
		// Check if user is signed in (non-null) and update UI accordingly.
		FirebaseUser currentUser = mAuth.getCurrentUser();
		updateUI(currentUser);
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

		// [START config_signin]
		// Configure Google Sign In
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();
		// [END config_signin]

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();

		// [START initialize_auth]
		mAuth = FirebaseAuth.getInstance();
		// [END initialize_auth]

//        getSupportActionBar().setTitle("Signin");

		emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        
        signupTextView = (TextView) findViewById(R.id.signupTextView);
		forgetPassTextView = (TextView) findViewById(R.id.forgetPassTextView);
        
        submitButton = (Button) findViewById(R.id.submitButton);
		signInButton = (SignInButton) findViewById(R.id.sign_in_button);

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

		signInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				signIn();
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
//						Intent mainIntent = new Intent(SigninActivity.this, MainActivity.class);
						Intent mainIntent = new Intent(SigninActivity.this, HomeActivity.class);
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

	// [START onactivityresult]
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			if (result.isSuccess()) {
				// Google Sign In was successful, authenticate with Firebase
				GoogleSignInAccount account = result.getSignInAccount();
				firebaseAuthWithGoogle(account);
			} else {
				// Google Sign In failed, update UI appropriately
				// [START_EXCLUDE]
				updateUI(null);
				// [END_EXCLUDE]
			}
		}
	}
	// [END onactivityresult]

	// [START auth_with_google]
	private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
		Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
		// [START_EXCLUDE silent]
//		showProgressDialog();
		// [END_EXCLUDE]

		AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
		mAuth.signInWithCredential(credential)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							// Sign in success, update UI with the signed-in user's information
							Log.d(TAG, "signInWithCredential:success");
							FirebaseUser user = mAuth.getCurrentUser();
							updateUI(user);
							Log.e(TAG, user.getEmail()
									+ "\n -- " + user.getDisplayName()
									+ "\n -- " + user.getProviderId()
									+ "\n -- " + user.getUid()
									+ "\n -- " + user.getPhotoUrl());
							Hawk.put("uName", user.getDisplayName());
							Hawk.put("uEmail", user.getEmail());
							Hawk.put("uImage", user.getPhotoUrl().toString());
							Intent mainIntent = new Intent(SigninActivity.this, HomeActivity.class);
							startActivity(mainIntent);
							overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
							finish();
						} else {
							// If sign in fails, display a message to the user.
							Log.w(TAG, "signInWithCredential:failure", task.getException());
							Toast.makeText(SigninActivity.this, "Authentication failed.",
									Toast.LENGTH_SHORT).show();
							updateUI(null);
						}

						// [START_EXCLUDE]
//						hideProgressDialog();
						// [END_EXCLUDE]
					}
				});
	}
	// [END auth_with_google]

	// [START signin]
	private void signIn() {
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}
	// [END signin]

	private void updateUI(FirebaseUser user) {
//		hideProgressDialog();
		if (user != null) {

//			mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
//			mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
//
//			findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//			findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
		} else {
//			mStatusTextView.setText(R.string.signed_out);
//			mDetailTextView.setText(null);
//
//			findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
//			findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
		}
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		// An unresolvable error has occurred and Google APIs (including Sign-In) will not
		// be available.
		Log.d(TAG, "onConnectionFailed:" + connectionResult);
		Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
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
