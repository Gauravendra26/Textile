package com.example.nsso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    RelativeLayout rllogin;
    ImageView imgshow,imghide;
    EditText etuserid,etPass;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    TextView tvForgot;
    String email, password;
    private static final int INTERNET_PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.blackIconStatusBar(MainActivity.this, R.color.LoginPage);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        init();
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        SharedPreferences sh = getSharedPreferences("MyData", MODE_PRIVATE);
        Boolean Login_Status = sh.getBoolean("Login_Status", false);
        if (Login_Status) {
            Intent i = new Intent(getApplicationContext(), ProfilePage.class);
            startActivity(i);
            finish();
        }

        rllogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (isValid()){
                  if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.INTERNET)
                          != PackageManager.PERMISSION_GRANTED) {
                      // Permission is not granted, request it
                      ActivityCompat.requestPermissions(MainActivity.this,
                              new String[]{android.Manifest.permission.INTERNET}, INTERNET_PERMISSION_REQUEST_CODE);
                  } else {
                      // Permission already granted, proceed with your code
                      getLogIn(email, password);
                  }
//                  Intent i = new Intent(getApplicationContext(), Mobile_Activity.class);
//                  startActivity(i);

              }
                hideDefaultKeyboard();
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                rllogin.startAnimation(myAnim);

            }
        });
        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Forgot_Mobile_Activity.class);
                startActivity(i);
                finish();
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                tvForgot.startAnimation(myAnim);

            }
        });
        imgshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etPass.setTransformationMethod(null);
                imgshow.setVisibility(View.INVISIBLE);
                imghide.setVisibility(View.VISIBLE);
                hideDefaultKeyboard();
                imgshow.startAnimation(clickAnimation());
            }
        });
        imghide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPass.setTransformationMethod(new PasswordTransformationMethod());

                imghide.setVisibility(View.INVISIBLE);
                imgshow.setVisibility(View.VISIBLE);
                hideDefaultKeyboard();
                imghide.startAnimation(clickAnimation());
            }
        });

    }

    void init() {
        tvForgot = findViewById(R.id.tvForgot);
        rllogin = findViewById(R.id.rllogin);
        etuserid = findViewById(R.id.etuserid);
        etPass = findViewById(R.id.etPass);
        imgshow = findViewById(R.id.imgshow);
        imghide = findViewById(R.id.imghide);
    }
    public AlphaAnimation clickAnimation() {
        return new AlphaAnimation(1F, 0.1F); // Change "0.4F" as per your recruitment.
    }

    private void hideDefaultKeyboard() {
        //  MainActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //you have got lot of methods here
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    private void getLogIn(String email, String password) {
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.new_progress);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                ApiData.Login,  // Change the URL to use HTTPS
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();

                        try {
                            if (response.getBoolean("status")) {
                                Toast.makeText(MainActivity.this, "" + response.getString("message"),
                                        Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(getApplicationContext(), OtpPage.class);
                                i.putExtra("email", etuserid.getText().toString().trim());
                                i.putExtra("password", etPass.getText().toString());
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "Please Enter Correct Email and Password",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("errordata", "" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e("VolleyError", "Error: " + error.getMessage());

                        if (error instanceof TimeoutError) {
                            Toast.makeText(MainActivity.this, "Timeout Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NoConnectionError) {
                            Toast.makeText(MainActivity.this, "No Connection Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(MainActivity.this, "Parse Error", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    boolean isValid() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        email= etuserid.getText().toString().trim();
       password= etPass.getText().toString();

        if (email.isEmpty()) {
            etuserid.setError("Please enter Userid");
            return false;
        }
        if (password.isEmpty()) {
            etPass.setError("Enter Password");
            return false;
        }
        return true;
    }
}