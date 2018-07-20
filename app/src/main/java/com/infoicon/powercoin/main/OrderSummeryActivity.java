package com.infoicon.powercoin.main;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.infoicon.powercoin.bottomNavigation.App;
import com.infoicon.powercoin.utils.HelperMethods;
import com.infoicon.powercoin.utils.Keys;
import com.infoicon.powercoin.utils.SavePref;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.OrdersummaryBinding;

/**
 * Created by HP-PC on 11/20/2017.
 */

public class OrderSummeryActivity extends AppCompatActivity {
    private OrderSummaryAdapter fAdapter;
    JSONObject jsonObject;
ProgressDialog progressDialog;
    String order_id;
    int position;
    OrdersummaryBinding ordersummaryBinding;
    List<ViewAndTrackOrderBeans> viewAndTrackOrderBeansList;

    @Override
    protected void onResume() {
        super.onResume();
        try
        {
            ordersummaryBinding.mainToolbar.cartCount.setText("" + ((App) this.getApplication()).getCardList().size());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         ordersummaryBinding = DataBindingUtil.setContentView(this, R.layout.ordersummary);
        ordersummaryBinding.mainToolbar.cartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    if(((App)getApplication()).getCardList().size()>0)
                    {
                        Intent intent = new Intent(OrderSummeryActivity.this, CartActivity.class);
                        startActivity(intent);
                    }

                    else
                    {
                        HelperMethods.showSnackBar(OrderSummeryActivity.this,"Please Select " +
                                "Test First");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        });

ordersummaryBinding.download.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(),"Downloading Start within 10 second",Toast.LENGTH_LONG).show();
        Uri Download_Uri = Uri.parse("http://52.67.139.216/lab/public/images/report/2.pdf");
        long downloadReference;
        // Create request for android download manager
        DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        //Setting title of request
        request.setTitle("Data Download");
        //Setting description of request
        request.setDescription("Report DownLoading");
        request.setDestinationInExternalFilesDir(OrderSummeryActivity.this, Environment.DIRECTORY_DOWNLOADS,+System.currentTimeMillis()+"AndroidTutorialPoint.pdf");

        //Enqueue download and save into referenceId
        downloadReference = downloadManager.enqueue(request);
        //return downloadReference;


    }
});
ordersummaryBinding.mainToolbar.cityName.setVisibility(View.GONE);
        ordersummaryBinding.reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ordersummaryBinding.reorder.getText().toString().equalsIgnoreCase("Cancel"))
                {
                    getCancelApi();
                }
                else
                    {
                    Intent intent = new Intent(OrderSummeryActivity.this, ReOrderActivity.class);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<ViewAndTrackOrderBeans>>() {
                    }.getType();
                    String json = gson.toJson(viewAndTrackOrderBeansList, type);
                    intent.putExtra("data", json);
                    intent.putExtra("position", getIntent().getStringExtra("position"));
                    //intent.putExtra("",recyclerView.getChildAdapterPosition(itemView))
                    startActivity(intent);
                }
            } });
                //Toast.makeText(getApplicationContext(),"Coming Soon",Toast.LENGTH_LONG).show();



        System.out.println("fffff"+getIntent().getStringExtra("data"));
              Gson gson = new Gson();
        Type type = new TypeToken<List<ViewAndTrackOrderBeans>>() {}.getType();
         viewAndTrackOrderBeansList = gson.fromJson(getIntent().getStringExtra("data"), type);
         position=Integer.parseInt(getIntent().getStringExtra("position"));
        ordersummaryBinding.orderId.setText(viewAndTrackOrderBeansList.get(position).getOrder_id());
        ordersummaryBinding.date.setText(viewAndTrackOrderBeansList.get(position).getCreated_date());
        ordersummaryBinding.status.setText(viewAndTrackOrderBeansList.get(position).getOrder_status());
        ordersummaryBinding.ammount.setText("₹ "+viewAndTrackOrderBeansList.get(position).getPayable_amount());

        if(viewAndTrackOrderBeansList.get(position).getOrder_status().equalsIgnoreCase("order_placed"
        ) || viewAndTrackOrderBeansList.get(position).getOrder_status().equalsIgnoreCase("assigned_to_rider"
        ))
        {
            ordersummaryBinding.reorder.setText("Cancel");

        }

        if(viewAndTrackOrderBeansList.get(position).getPayment_status().equalsIgnoreCase("unpaid"))
        {
            ordersummaryBinding.paymentMade.setText("COD");
        }
        else {
            ordersummaryBinding.paymentMade.setText("Razorpay");

        }
       // ordersummaryBinding.timeSlots.setText(viewAndTrackOrderBeansList.get(position).getTime_slots());
        ordersummaryBinding.items.setText(viewAndTrackOrderBeansList.get(position).getViewAndTrackItemOrderBeanArrayList().size()+" Items");
        ordersummaryBinding.mainToolbar.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ordersummaryBinding.mainToolbar.txtTitle.setText("Order Details");

        order_id=viewAndTrackOrderBeansList.get(position).getOrder_id();
      ordersummaryBinding.shipping.setText("GHC "+viewAndTrackOrderBeansList.get(position));//.getShipping_charge());
        ordersummaryBinding.tax.setText("₹ "+viewAndTrackOrderBeansList.get(position).getTax_amount());
        ordersummaryBinding.otherTotal.setText("₹ "+Integer.parseInt(viewAndTrackOrderBeansList.get(position)
        .getPayable_amount()));
        ordersummaryBinding.promo.setText("₹ "+viewAndTrackOrderBeansList.get(position).getDiscount_amount());
        fAdapter = new OrderSummaryAdapter(OrderSummeryActivity.this, viewAndTrackOrderBeansList.get(position).getViewAndTrackItemOrderBeanArrayList(),"");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        ordersummaryBinding.recyclerView.setLayoutManager(mLayoutManager);
        ordersummaryBinding. recyclerView.setItemAnimator(new DefaultItemAnimator());
        ordersummaryBinding.recyclerView.setAdapter(fAdapter);
     }



JSONObject sendJson;
    ProgressDialog pDialog;
    private void getCancelApi() {


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        try
        {
            sendJson = new JSONObject();
            sendJson.put("method", "updateOrderstatus");
            sendJson.put("order_id",viewAndTrackOrderBeansList.get(position).getOrder_id() );
            sendJson.put("order_status", "cancelled");
            sendJson.put("user_id", SavePref.getPref(this,SavePref.User_id));

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
                        try {
                            JSONObject jObject = null;
                            jObject = new JSONObject(response);
                            if (jObject.getString("status").equalsIgnoreCase("success"))
                            {
                                pDialog.dismiss();
                               finish();
                            } else {
                                pDialog.dismiss();
                            }
                        }catch (Exception e)
                        {

                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();


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