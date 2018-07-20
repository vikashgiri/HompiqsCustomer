package com.infoicon.powercoin.login;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.infoicon.powercoin.utils.HelperMethods;
import com.infoicon.powercoin.utils.Keys;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity
{

    ActivityRegistrationBinding registrationBinding;
JSONObject sendJson;
ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registrationBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
registrationBinding.headToolbar.txtTitle.setText(getString(R.string.signup));
        registrationBinding.textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        registrationBinding.headToolbar.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        registrationBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        registrationBinding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();            }
        });
    }




    private void SignUp(){

        String fname = registrationBinding.fname.getText().toString();
        String lname = registrationBinding.lname.getText().toString();
       String password = registrationBinding.password.getText().toString();
        String email = registrationBinding.email.getText().toString();
        String confirm_password = registrationBinding.cnfmpassword.getText().toString();
        String mobile = registrationBinding.mobile.getText().toString();
        String address = registrationBinding.address.getText().toString();
        String age = registrationBinding.age.getText().toString();

        if(!HelperMethods.isNetworkAvailable(this))
        {
            Snackbar snackbar = Snackbar
                    .make(registrationBinding.fname,getString(R.string.connectio_error), Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }

        if (!HelperMethods.isEmpty(fname)) {
            registrationBinding.fname.setError("Enter First Name");
            registrationBinding.fname.requestFocus();

            return;
        }
        if (!HelperMethods.isEmpty(lname)) {
            registrationBinding.fname.setError("Enter Last Name");
            registrationBinding.fname.requestFocus();

            return;
        }

        if (!HelperMethods.isEmpty(email)) {
            registrationBinding.email.setError("Enter Email");
            registrationBinding.email.requestFocus();

            return;
        }

        if (!HelperMethods.emailValidator(email)) {
            registrationBinding.email.setError("Enter Valid Email");
            registrationBinding.email.requestFocus();

            return;
        }

        if (!HelperMethods.isEmpty(age))
        {
            registrationBinding.age.setError("Enter Age");
            registrationBinding.age.requestFocus();
            return;
        }
        if (Integer.parseInt(age)<18) {
            registrationBinding.age.setError("you are not eligible to use.");
            registrationBinding.age.requestFocus();

            return;
        }
        if (!HelperMethods.isEmpty(password)) {
            registrationBinding.password.setError("Enter password");
            registrationBinding.password.requestFocus();

            return;
        }
        if (!HelperMethods.isEmpty(confirm_password)) {
            registrationBinding.cnfmpassword.setError("Enter Password");
            registrationBinding.cnfmpassword.requestFocus();

            return;
        }

        if (!HelperMethods.isEmpty(mobile)) {
            registrationBinding.mobile.setError("Invalid Mobile");
            registrationBinding.mobile.requestFocus();

            return;
        }
        if (!HelperMethods.isEmpty(address)) {
            registrationBinding.address.setError("Enter  Address");
            registrationBinding.address.requestFocus();

            return;
        }
        if (!password.equals(confirm_password)) {
            registrationBinding.password.setError("Password not matched");
            registrationBinding.password.requestFocus();

            registrationBinding.cnfmpassword.setText("");
            registrationBinding.password.setText("");
            return;
        }

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        try
        {
            sendJson = new JSONObject();
            sendJson.put("method", "addedituser");
           sendJson.put("city_id", "1");
            sendJson.put("password",password);
            sendJson.put("name", fname+" "+lname);
            sendJson.put("email", email);
            sendJson.put("mobile_number", mobile);
            sendJson.put("address", address);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Keys.BASE_URL+"application/customer?",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jObject=null;
                        String jsonString="";//your json string here
                        try{
                            jObject= new JSONObject(response);
                            if(jObject.getString("status").equalsIgnoreCase("success"))
                            {
                                HelperMethods.showSnackBar(RegistrationActivity.this,"Registration successfully");
                                finish();
                            }
                            else
                            {
                                pDialog.dismiss();
                                HelperMethods.showSnackBar(RegistrationActivity.this,"Registration Error");
                            }
                        }
                        catch (JSONException e)
                        {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    HelperMethods.showSnackBar(RegistrationActivity.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    HelperMethods.showSnackBar(RegistrationActivity.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    HelperMethods.showSnackBar(RegistrationActivity.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    HelperMethods.showSnackBar(RegistrationActivity.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    HelperMethods.showSnackBar(RegistrationActivity.this,"Parse Error!");
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
