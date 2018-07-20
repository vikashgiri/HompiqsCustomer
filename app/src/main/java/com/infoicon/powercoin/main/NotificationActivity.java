package com.infoicon.powercoin.main;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.infoicon.powercoin.utils.SavePref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import infoicon.com.powercoin.R;


/**
 * Created by HP-PC on 11/19/2017.
 */

public class NotificationActivity extends AppCompatActivity {
    private NotificationAdapter fAdapter;
    int pageCount=1;
    ProgressDialog pDialog;
    ArrayList<NotificationBean>notificationBeanArrayList=new ArrayList<>();
    RecyclerView recyclerView;
    JSONObject sendJson;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.notificatinactivity);
         recyclerView=(RecyclerView)findViewById(R.id.recycler_view_subcat);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        ImageView back = (ImageView) findViewById(R.id.toolbar_back);
        TextView title = (TextView) findViewById(R.id.txt_title);
title.setText("Notification");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

                fAdapter = new NotificationAdapter(NotificationActivity.this,
                        notificationBeanArrayList,recyclerView);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(NotificationActivity.this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(fAdapter);
        getNotification(pageCount);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();
                 if (lastvisibleitemposition == fAdapter.getItemCount() - 1)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    getNotification(++pageCount);
                }
             }
        });

    }

    void  getNotification(int pageCount)
    {

        try {
            sendJson = new JSONObject();
            sendJson.put("type", "admin");
            sendJson.put("method", "getnotification");
            sendJson.put("all_notification", "1");
            sendJson.put("user_type", "customer");
            sendJson.put("pagination","1");
            sendJson.put("page",pageCount);
            sendJson.put("user_id", SavePref.getPref(NotificationActivity.this,SavePref.User_id));

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        if(!HelperMethods.isNetworkAvailable(this))
        {
          /* Snackbar snackbar = Snackbar
                   .make, "Please Connect Internet", Snackbar.LENGTH_LONG);
           snackbar.show();*/
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://52.67.139.216/lab/application/customer?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString="";//your json string here
                        try {

                            JSONArray jsonArray = new JSONObject(response).getJSONArray("data");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                NotificationBean notificationBean=new NotificationBean();
                                notificationBean.setId(jsonObject.getString("id"));
                                notificationBean.setCreated_date(jsonObject.getString("created_date"));
                                notificationBean.setMsg(jsonObject.getString("msg"));
                                notificationBean.setSubject(jsonObject.getString("subject"));
notificationBeanArrayList.add(notificationBean);
                            }


                        }
                        catch (JSONException e)
                        {
                            HelperMethods.showSnackBar(NotificationActivity.this,"Notification Not Found");
                            e.printStackTrace();
                        }
                        progressBar.setVisibility(View.GONE);

                        fAdapter .notifyDataSetChanged();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    HelperMethods.showSnackBar(NotificationActivity.this,"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    HelperMethods.showSnackBar(NotificationActivity.this, "Authentication Error!");
                } else if (error instanceof ServerError) {
                    HelperMethods.showSnackBar(NotificationActivity.this,"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    HelperMethods.showSnackBar(NotificationActivity.this, "Network Error!");
                } else if (error instanceof ParseError) {
                    HelperMethods.showSnackBar(NotificationActivity.this,"Parse Error!");
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