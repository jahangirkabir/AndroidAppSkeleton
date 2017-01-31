package com.jahanbabu.AndroidAppSkeleton;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignupActivity extends AppCompatActivity {

	private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, mobileEditText, dobEditText;
	private TextView signinTextView;
    private RadioGroup genderRadioGroup;
    private Spinner nationalitySpinner;
    private String fName, lName, mobile, dob, gender = "male", nationality, email, password, status, session;
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
        setContentView(R.layout.activity_signup);

        context = this;

        getSupportActionBar().setTitle("Create Account");

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/Raleway-Light.ttf")
                        .setDefaultFontPath("fonts/ClassicRobot.otf")
//                .setDefaultFontPath("fonts/Roboto-Light.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        mobileEditText = (EditText) findViewById(R.id.mobileEditText);
        dobEditText = (EditText) findViewById(R.id.dobEditText);
        genderRadioGroup = (RadioGroup) findViewById(R.id.genderRadioGroup);
        nationalitySpinner = (Spinner) findViewById(R.id.nationalitySpinner);
        signinTextView = (TextView) findViewById(R.id.signinTextView);
        submitButton = (Button) findViewById(R.id.submitButton);

        nationalitySpinner.setSelection(17);

        dobEditText.setFocusable(false);
        dobEditText.setClickable(true);

        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);
                if (index == 0) {
                    gender = "male";
                } else {
                    gender = "female";
                }
            }
        });

        dobEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SignupActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        signinTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent signinIntent = new Intent(SignupActivity.this, SigninActivity.class);
                signinIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(signinIntent);
				overridePendingTransition(R.anim.right_in, R.anim.right_out);
                finish();
			}
		});

        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(SplashActivity.isNetworkAvailable(context)){
                    fName = firstNameEditText.getText().toString();
                    lName = lastNameEditText.getText().toString();
                    mobile = mobileEditText.getText().toString();
                    email = emailEditText.getText().toString();
                    password = passwordEditText.getText().toString();
                    dob = dobEditText.getText().toString();
                    nationality = nationalitySpinner.getSelectedItem().toString();

                    Timber.e("INPUT : " + email+" ---- "+password);

                    if (!fName.isEmpty() && ! lName.isEmpty() && !email.isEmpty()
                            && !password.isEmpty() && !mobile.isEmpty() && !gender.isEmpty() && !dob.isEmpty() && !nationality.contains("Select")){
                        final JSONObject mainObj = new JSONObject();
                        JSONObject objbody = new JSONObject();
                        try {
                            objbody.put("fname", fName);
                            objbody.put("lname", lName);
                            objbody.put("password", password);
                            objbody.put("email", email);
                            objbody.put("mobile", mobile);
                            objbody.put("dob", dob);
                            objbody.put("nationality", nationality);

                            //          main.put("token", token);
                            mainObj.put("body", objbody);
                            Timber.e("JSON : " + mainObj.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        requestSingup(mainObj.toString());
                    } else
                        Toast.makeText(SignupActivity.this, "Please fill all fields to continue", Toast.LENGTH_LONG).show();

                } else {
                    SplashActivity.showNoInternetDialog(context, SignupActivity.this);
                }
            }
        });
    }

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dobEditText.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	Intent signinIntent = new Intent(SignupActivity.this, SigninActivity.class);
        signinIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(signinIntent);
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();
    	super.onBackPressed();
    }

    public void requestSingup(final String body){
        progressDialog = ProgressDialog.show(this, "","Creating account please wait...", true);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://api.mysmartciti.com/iot/user/createUser";

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
                                Toast.makeText(SignupActivity.this, "Invalid User!!!", Toast.LENGTH_LONG).show();
                            } else {
                                status = mainJsonObject.optString("status");
                                if (status.equalsIgnoreCase("1111")){
                                    Toast.makeText(SignupActivity.this, "Account creation successful!!!", Toast.LENGTH_LONG).show();
                                    Intent mainIntent = new Intent(SignupActivity.this, SigninActivity.class);
                                    startActivity(mainIntent);
                                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                                    finish();
                                } else if (status.equalsIgnoreCase("2222")){
                                    Toast.makeText(SignupActivity.this, "This user is already exist!!!", Toast.LENGTH_LONG).show();
                                } else
                                    Toast.makeText(SignupActivity.this, "Error in account creation!!!", Toast.LENGTH_LONG).show();
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
                Toast.makeText(SignupActivity.this, "Server Error, Please try again!", Toast.LENGTH_LONG).show();
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
