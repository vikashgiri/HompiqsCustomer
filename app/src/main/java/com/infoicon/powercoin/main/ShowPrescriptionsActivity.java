package com.infoicon.powercoin.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import com.infoicon.powercoin.bottomNavigation.App;
import com.infoicon.powercoin.bottomNavigation.CardListResponce;
import com.infoicon.powercoin.login.LoginActivity;
import com.infoicon.powercoin.utils.HelperMethods;
import com.infoicon.powercoin.utils.Keys;
import com.infoicon.powercoin.utils.SavePref;
import com.infoicon.powercoin.utils.UpdateCardListInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.ActivityCartBinding;
import infoicon.com.powercoin.databinding.ActivityShowprescriptionsBinding;

public class ShowPrescriptionsActivity extends AppCompatActivity  {
    ProgressDialog progressDialog;

    HashMap<String, String> HashMapForURL;
    ActivityShowprescriptionsBinding mBinding;

ArrayList<ShowPrescriptionResponce> showPrescriptionResponceArrayList=new ArrayList<>();
    public ShowPrescriptionsActivity() {
        // Required empty public constructor
    }

    private void initView() {
        LinearLayoutManager lLayout = new LinearLayoutManager(this);
        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setLayoutManager(lLayout);
        mBinding.recyclerView.setNestedScrollingEnabled(false);
    }

    ShowPrescriptionsAdapter cartFragmentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_showprescriptions);

        mBinding.toolbar.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        mBinding.toolbar.txtTitle.setText(getString(R.string.prescription_list));

callDescriptionListApi();
    }















    String base_url;
    ProgressDialog pDialog;
    JSONObject sendJson;
    private void callDescriptionListApi(){
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
          try {
            sendJson = new JSONObject();
            sendJson.put("method", "getsubscription");
          //  sendJson.put("user_id","3");
            sendJson.put("page", "1");
            sendJson.put("user_id", SavePref.getPref(ShowPrescriptionsActivity.this,SavePref.User_id));
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://52.67.139.216/lab/application/customer?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString="";//your json string here
                        try {
                            JSONObject jObject = new JSONObject(response);
                            if (jObject.getString("status").equalsIgnoreCase("success"))
                            {
                                JSONArray jsonArray=jObject.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    ShowPrescriptionResponce showPrescriptionResponce=new ShowPrescriptionResponce();
                                    showPrescriptionResponce.setImage_name(jsonObject.getString("image_name"));
                                    showPrescriptionResponce.setRefered_by(jsonObject.getString("refered_by"));
                                    showPrescriptionResponceArrayList.add(showPrescriptionResponce);
                                }

                                 base_url=jObject.getString("imageRootPath");

                                cartFragmentAdapter = new
                                        ShowPrescriptionsAdapter(ShowPrescriptionsActivity.this, showPrescriptionResponceArrayList,base_url,new
                                        ShowPrescriptionsAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(
                                                    int position) {
                                                //updateCart();
                                            }
                                        });
                                mBinding.recyclerView.setAdapter(cartFragmentAdapter);
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
                    HelperMethods.showSnackBar(ShowPrescriptionsActivity.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    HelperMethods.showSnackBar(ShowPrescriptionsActivity.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    HelperMethods.showSnackBar(ShowPrescriptionsActivity.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    HelperMethods.showSnackBar(ShowPrescriptionsActivity.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    HelperMethods.showSnackBar(ShowPrescriptionsActivity.this,"Parse Error!");
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