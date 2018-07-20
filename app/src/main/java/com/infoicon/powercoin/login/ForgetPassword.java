package com.infoicon.powercoin.login;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
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
import infoicon.com.powercoin.databinding.ForgetpasswordBinding;

/**
 * Created by HP-PC on 12/17/2017.
 */

public class ForgetPassword extends AppCompatActivity
{

    ForgetpasswordBinding forgetpasswordBinding;
    ProgressDialog pDialog;
    JSONObject sendJson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forgetpasswordBinding = DataBindingUtil.setContentView(this, R.layout.forgetpassword);
        ImageView back = (ImageView) findViewById(R.id.toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        forgetpasswordBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPassword();
            }
        });
    }

    private void forgetPassword()
    {
        if (!HelperMethods.isEmpty(forgetpasswordBinding.email.getText().toString())) {
            forgetpasswordBinding.email.setError("Please Enter Email Id");
            forgetpasswordBinding.email.requestFocus();
            return;
        }
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        http://54.233.182.212/basketapi/application/customer?parameters=%7B%22method%22%3A%22changepassword%22%2C%22password%22%3A%2212345%22%2C%22new_password%22%3A%22123%22%2C%22user_id%22%3A1%7D

        try {
            sendJson = new JSONObject();
            sendJson.put("method", "forgetpassword");
            sendJson.put("email",forgetpasswordBinding.email.getText().toString());

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Keys.BASE_URL+"application/customer?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString="";//your json string here
                        try{
                            JSONObject jObject= new JSONObject(response);
                            if(jObject.getString("status").equalsIgnoreCase("success"))
                            {
 Toast.makeText(getApplicationContext(),"Email sent On your register mail id",Toast.LENGTH_SHORT).show();
finish();
                            }
                            else
                            {
                                HelperMethods.showSnackBar(ForgetPassword.this,jObject.getString("msg"));
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    HelperMethods.showSnackBar(ForgetPassword.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    HelperMethods.showSnackBar(ForgetPassword.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    HelperMethods.showSnackBar(ForgetPassword.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    HelperMethods.showSnackBar(ForgetPassword.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    HelperMethods.showSnackBar(ForgetPassword.this,"Parse Error!");
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