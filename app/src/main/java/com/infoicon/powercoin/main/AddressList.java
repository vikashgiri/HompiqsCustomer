package com.infoicon.powercoin.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.infoicon.powercoin.utils.SavePref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import infoicon.com.powercoin.R;

/**
 * Created by Anjali Giri on 12/7/2017.
 */

public class AddressList extends AppCompatActivity {
    ProgressDialog progressDialog;
    JSONObject jsonObject;
    ArrayList<AddressListBean> addressListBeanArrayList = new ArrayList<>();
    GetAddressAdapter addressAdapter;
    RecyclerView recyclerView;
    Button btn_addaddress;
    static AddressList addressList;


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getaddress_list);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_subcat);
        addressList = this;
        ImageView back = (ImageView) findViewById(R.id.toolbar_back);
        TextView title = (TextView) findViewById(R.id.txt_title);
        title.setText("Select Address");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressDialog = new ProgressDialog(AddressList.this);
        jsonObject = new JSONObject();
        btn_addaddress = (Button) findViewById(R.id.btn_addaddress);

        btn_addaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addressListBeanArrayList.size()<=5) {
                    Intent intent = new Intent(AddressList.this, AddressActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"you can  not add more than five address",Toast.LENGTH_SHORT).show();
                }
            }
        });


        try {
            jsonObject.put("method", "getaddresslist");
            jsonObject.put("user_id", SavePref.getPref(AddressList.this, SavePref.User_id));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getAddress();

    }

    private void getAddress() {
        progressDialog.show();
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        RequestQueue requestQueue = Volley.newRequestQueue(AddressList.this);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://52.67.139.216/lab/application/customer?", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("ProfileResopnse" + response);
                HelperMethods.addressListBeanArrayList.clear();
                try {
                    JSONObject jObject = new JSONObject(response).getJSONObject("data");
                    Iterator<String> keys = jObject.keys();
                    while (keys.hasNext()) {
                        AddressListBean addressListBean = new AddressListBean();
                        String key = keys.next();
                        JSONObject innerJObject = jObject.getJSONObject(key);
                        addressListBean.setId(innerJObject.getString("id"));
                        addressListBean.setUser_id(innerJObject.getString("user_id"));
//                                addressListBean.setAddress_nickname(innerJObject.getString("address_nickname"));
                        addressListBean.setContact_name(innerJObject.getString("contact_name"));
                        addressListBean.setCity_id(innerJObject.getString("city_id"));
                        addressListBean.setCity_name(innerJObject.getString("city_name"));
                        addressListBean.setHouse_number(innerJObject.getString("house_number"));
                        addressListBean.setStreet_detail(innerJObject.getString("street_detail"));
                        addressListBean.setGender(innerJObject.getString("gender"));
                        addressListBean.setZipcode(innerJObject.getString("zipcode"));
                        // addressListBean.setArea(innerJObject.getString("area"));
                        addressListBean.setCreated_date(innerJObject.getString("created_date"));
                        addressListBean.setUpdated_date(innerJObject.getString("updated_date"));
                        addressListBeanArrayList.add(addressListBean);
                        HelperMethods.addressListBeanArrayList.add(addressListBean);
                    }
                } catch (JSONException e) {
                    Intent intent = new Intent(AddressList.this, AddressActivity.class);
                    startActivity(intent);
                    e.printStackTrace();
                    return;
                }
                String flag = "none";
                if (getIntent().hasExtra("checkout")) {
                    flag = "checkout";
                }
                addressAdapter = new GetAddressAdapter(AddressList.this, addressListBeanArrayList, flag);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(addressAdapter);
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error" + error.getMessage());
                progressDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    HelperMethods.showSnackBar(AddressList.this, "Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    HelperMethods.showSnackBar(AddressList.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    HelperMethods.showSnackBar(AddressList.this, "Server Side Error!");
                } else if (error instanceof NetworkError) {
                    HelperMethods.showSnackBar(AddressList.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    HelperMethods.showSnackBar(AddressList.this, "Parse Error!");
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("parameters", jsonObject.toString());
                System.out.println("Parameters" + " " + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        sr.setShouldCache(false);
        requestQueue.add(sr);

    }
}