package com.infoicon.powercoin.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infoicon.powercoin.customView.CustomTextView;
import com.infoicon.powercoin.main.Config;
import com.infoicon.powercoin.main.MainActivity;
import com.infoicon.powercoin.utils.HelperMethods;
import com.infoicon.powercoin.utils.Keys;
import com.infoicon.powercoin.utils.SavePref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity
{
    JSONObject sendJson;
    ProgressDialog pDialog;
 ActivityLoginBinding activityLoginBinding;
    String regId;

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", null);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        activityLoginBinding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        activityLoginBinding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(LoginActivity.this, MainActivity.class));
                login();
            }
        });
        CustomTextView forgetPassword=(CustomTextView) findViewById(R.id.forgetPassword);
    forgetPassword.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(LoginActivity.this, ForgetPassword.class));
        }
    });
        displayFirebaseRegId();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SavePref.get_credential(LoginActivity.this,SavePref.Email).equalsIgnoreCase("0")) {
            activityLoginBinding.email.setText(SavePref.get_credential(LoginActivity.this,
                    SavePref.Email));
            activityLoginBinding.password.setText(SavePref.get_credential(LoginActivity.this,
                    SavePref.Password));
            activityLoginBinding.checkBox.setChecked(true);
        }
    }
    private void login ()
    {

        if (!HelperMethods.isEmpty(activityLoginBinding.email.getText().toString())) {
            activityLoginBinding.email.setError("Please Enter Email");
            activityLoginBinding.email.requestFocus();

            return;
        }
        if (!HelperMethods.emailValidator(activityLoginBinding.email.getText().toString())) {
            activityLoginBinding.email.setError("Please Enter Valid Email");
            activityLoginBinding.email.requestFocus();
            return;
        }
        if (!HelperMethods.isEmpty(activityLoginBinding.password.getText().toString())) {
            activityLoginBinding.password.setError("Please Enter Password");
            activityLoginBinding.password.requestFocus();
            return;
        }

        if(!HelperMethods.isNetworkAvailable(this))
        {
            HelperMethods.showSnackBar(LoginActivity.this,getString(R.string.connectio_error));
            return;
        }
        if (activityLoginBinding.checkBox.isChecked()) {
            SavePref.save_credential(LoginActivity.this,SavePref.Email,activityLoginBinding.email.getText().toString());
            SavePref.save_credential(LoginActivity.this,SavePref.Password,activityLoginBinding.password.getText().toString());

        }
        String email = activityLoginBinding.email.getText().toString();
        final String password = activityLoginBinding.password.getText().toString();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        try {
            sendJson = new JSONObject();
            sendJson.put("method", "login");
            sendJson.put("email",email);
            sendJson.put("password",password);
            sendJson.put("fcm_reg_id",regId);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Keys.BASE_URL +"application/customer?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString="";//your json string here
                        try {
                            JSONObject jObject = new JSONObject(response);

                            if (jObject.getString("status").equalsIgnoreCase("success"))
                            {
                                JSONObject jsonObject = jObject.getJSONObject("data");
                                Iterator<String> keys = jsonObject.keys();
                                  while (keys.hasNext())
                                  {
                                    String key = keys.next();
                                    JSONObject innerJObject = jsonObject.getJSONObject(key);
                                    SavePref.saveStringPref(LoginActivity.this,SavePref.User_id,innerJObject.getString("id"));
                                    SavePref.saveStringPref(LoginActivity.this,SavePref.Name,innerJObject.getString("name"));
                                    SavePref.saveStringPref(LoginActivity.this,SavePref.address,innerJObject.getString("address"));
                                    SavePref.saveStringPref(LoginActivity.this,SavePref.Email,innerJObject.getString("email"));
                                      SavePref.save_credential(LoginActivity.this,SavePref.Password,activityLoginBinding.password.getText().toString());
                                    SavePref.saveStringPref(LoginActivity.this,SavePref.Mobile,innerJObject.getString("mobile_number"));
                                    SavePref.saveStringPref(LoginActivity.this,SavePref.is_loogedin,"true");
                                    HelperMethods.showSnackBar(LoginActivity.this, "Login SuccessFully");
                                    pDialog.dismiss();
                                    HelperMethods.updateCarts(LoginActivity.this);
                                      if(getIntent().hasExtra("logout"))
                                      {startActivity(new Intent(LoginActivity.this, MainActivity.class));

                                      }
                                    finish();
                                }
                            }
                            else
                                {
                                    HelperMethods.showSnackBar(LoginActivity.this, "Login Invalid");
                                    pDialog.dismiss();
                                }

                            }

                        catch (JSONException e)
                        {
                            pDialog.dismiss();
                            HelperMethods.showSnackBar(LoginActivity.this,"Login Error");
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    HelperMethods.showSnackBar(LoginActivity.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    HelperMethods.showSnackBar(LoginActivity.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    HelperMethods.showSnackBar(LoginActivity.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    HelperMethods.showSnackBar(LoginActivity.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    HelperMethods.showSnackBar(LoginActivity.this,"Parse Error!");
                }

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String,String>();
                params.put("parameters",sendJson.toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }

}
