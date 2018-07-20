package com.infoicon.powercoin.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infoicon.powercoin.bottomNavigation.App;
import com.infoicon.powercoin.bottomNavigation.CardListResponce;
import com.infoicon.powercoin.login.DoctorNameHint;
import com.infoicon.powercoin.navigationdrawer.adapter.CustomerAdapter;
import com.infoicon.powercoin.utils.HelperMethods;
import com.infoicon.powercoin.utils.SavePref;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.ActivityPlaceorderBinding;

/**
 * Created by HP-PC on 3/17/2018.
 */

public class PlaceOrderActivity extends AppCompatActivity implements PaymentResultListener {
    // ...
    String order_id;
    String tranjection_id;
    ArrayList<DoctorNameHint> aDoctorNameHints=new ArrayList<>();
   // http://localhost/lab/application/cron/updatepaymentstatus?TransactionId=e6e62f8aee95d767e7e6a512d04af4b4
    ProgressDialog pDialog;
    int totalAmmount;

    HashMap<String, String> HashMapForURL;
    JSONObject sendJson;
    ArrayList<CardListResponce> cardListResponceArrayList = new ArrayList<>();
    ActivityPlaceorderBinding placeorderBinding;
    @Override
    public void onPaymentSuccess(String razorpayPaymentID)
    {
        /**
         * Add your logic here for a successfull payment response
         */
        sendTranjectionId(tranjection_id);


    }

    @Override
    public void onPaymentError(int code, String response)
    {
        Intent i = new Intent(PlaceOrderActivity.this, ThanksActivity.class);
// set the new task and clear flags
        i.putExtra("logout","logout");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("order_id", order_id);
        startActivity(i);
    }

    public void placeDataOnServer()
    {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        try {
            sendJson = new JSONObject();
            sendJson.put("doctor_name",placeorderBinding.doctorName.getText().toString());
            sendJson.put("method", "placeorder");
            sendJson.put("user_id", SavePref.getPref(PlaceOrderActivity.this,SavePref.User_id));
            sendJson.put("shipping_address_id", getIntent().getStringExtra("ads_id"));
            sendJson.put("payment_type","razorpay");
            sendJson.put("clinic_name", placeorderBinding.clinicAds.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://52.67.139.216/lab/application/customer?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String jsonString = response;//your json string here
                            JSONObject Object = new JSONObject(jsonString).getJSONObject("data");
                            if(new JSONObject(jsonString).getString("status").equalsIgnoreCase("success"))
                            {

                                ((App)getApplication()).getCardList().clear();
                                order_id=Object.getString("order_id");
                                JSONObject jsonObject=Object.getJSONObject("razorpay");
                                tranjection_id=jsonObject.getString("transaction_id");
                                startPayment(jsonObject.getString("order_id"),jsonObject.getString("key"));

                            }
                            pDialog.dismiss();
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
                System.out.println("" + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("parameters", sendJson.toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }


    public PlaceOrderActivity()
    {
        // Required empty public constructor
    }


    private void Hint (String encodedImage)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://52.67.139.216:3000/usercontroller/getDoctors?doctor_name=v",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString="";//your json string here
                        try {
                            JSONObject jObject = new JSONObject(response);
                            aDoctorNameHints.clear();
                            if (jObject.getString("status").equalsIgnoreCase("true")) {
                                JSONArray jsonArray = jObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    DoctorNameHint doctorNameHint = new DoctorNameHint();
                                    doctorNameHint.setDoctor_name(jsonObject.getString("doctor_name"));
                                    doctorNameHint.setClinic_name(jsonObject.getString("clinic_name"));
                                    doctorNameHint.setId(jsonObject.getString("id"));

                                    aDoctorNameHints.add(doctorNameHint);

                                }
                            }
                            CustomerAdapter customerAdapter = new CustomerAdapter(PlaceOrderActivity.this, R.layout.customer_auto, aDoctorNameHints);
                            placeorderBinding.doctorName.setAdapter(customerAdapter);
                        }
                        catch (JSONException e)
                        {
                            HelperMethods.showSnackBar(PlaceOrderActivity.this,"Login Error");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String,String>();
                params.put("doctor_name",placeorderBinding.doctorName.getText().toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
         placeorderBinding=DataBindingUtil.setContentView(this,R.layout.activity_placeorder);
         placeorderBinding.apply.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (placeorderBinding.apply.getText().toString().equalsIgnoreCase("Apply")) {
                     applyCoupon();
                 }
                 else {
                     placeorderBinding.coupon.setText("");
                     applyCoupon();
                 }
             }
         });
        placeorderBinding.mainToolbar.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        placeorderBinding.cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeorderBinding.cash.setChecked(true);
                placeorderBinding.cart.setChecked(false);
            }
        });

        placeorderBinding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                placeorderBinding.cash.setChecked(false);

                placeorderBinding.cart.setChecked(true);

            }
        });


        placeorderBinding.placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(placeorderBinding.cash.isChecked() || placeorderBinding.cart.isChecked()))
                {
                    Toast.makeText(getApplicationContext(),"Please select Payment Option",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(placeorderBinding.cash.isChecked() )
                {
                    placeOrder();
                    return;
                }
                if(placeorderBinding.cart.isChecked() )
                {
                    placeDataOnServer();
                }
            }
        });
        placeorderBinding.doctorName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                // TODO Auto-generated method stub
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();
                if (newText.length() >= 1)
                    Hint(newText);
            }
        });
        placeorderBinding.doctorName.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
                placeorderBinding.clinicAds.setText(aDoctorNameHints.get(position).getClinic_name());
            }
        });
        checkOutCart();
    }

    public void checkOutCart() {
        try {
            sendJson = new JSONObject();
            sendJson.put("method", "checkout");
            sendJson.put("user_id", SavePref.getPref(PlaceOrderActivity.this,SavePref.User_id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(this
        );
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://52.67.139.216/lab/application/customer?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String jsonString = response;//your json string here
                            JSONObject Object = new JSONObject(jsonString).getJSONObject("data");
                            JSONObject jObject = Object.getJSONObject("totalOrderDetails");
                           // Iterator<String> keys = jObject.keys();
                            //while (keys.hasNext()) {
                                //String key = keys.next();
                                //JSONObject innerJObject = jObject.getJSONObject(key);
                             placeorderBinding.total.setText("₹ "+jObject.getString("amount"));
                            placeorderBinding.discountAmount.setText("₹ "+jObject.getString("discount_amount"));
                            placeorderBinding.commissionAmount.setText("₹ "+jObject.getString("commission_amount"));
                            placeorderBinding.taxAmount.setText("₹ "+jObject.getString("tax_amount"));
                            placeorderBinding.payableAmount.setText("₹ "+jObject.getString("payable_amount"));
                            totalAmmount=Integer.valueOf(jObject.getString("payable_amount"));
                             placeorderBinding.mainToolbar.txtTitle.setText("Place Order");
placeorderBinding.ads.setText(getIntent().getStringExtra("ads"));

                            //}

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("" + error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("parameters", sendJson.toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }


    public void applyCoupon() {
        try {
            sendJson = new JSONObject();
            sendJson.put("method", "applycoupon");
            sendJson.put("user_id", SavePref.getPref(PlaceOrderActivity.this,SavePref.User_id));
            sendJson.put("coupon_code",placeorderBinding.coupon.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(this
        );
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://52.67.139.216/lab/application/customer?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(placeorderBinding.coupon.getText().toString().length()>0)
                            {
                                placeorderBinding.apply.setText("Clear");
                            }
                            else {
                                placeorderBinding.apply.setText("Apply");
                                checkOutCart();
                                return;

                            }
                            String jsonString = response;//your json string here
                            JSONObject Object = new JSONObject(jsonString).getJSONObject("data");
                            JSONObject jObject = Object.getJSONObject("totalOrderDetails");
                            // Iterator<String> keys = jObject.keys();
                            //while (keys.hasNext()) {
                            //String key = keys.next();
                            //JSONObject innerJObject = jObject.getJSONObject(key);
                            placeorderBinding.total.setText("₹ "+jObject.getString("amount"));
                            placeorderBinding.discountAmount.setText("₹ "+jObject.getString("discount_amount"));
                            placeorderBinding.commissionAmount.setText("₹ "+jObject.getString("commission_amount"));
                            placeorderBinding.taxAmount.setText("₹ "+jObject.getString("tax_amount"));
                            placeorderBinding.payableAmount.setText("₹ "+jObject.getString("payable_amount"));
                            totalAmmount=Integer.valueOf(jObject.getString("payable_amount"));


                            //}

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("" + error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("parameters", sendJson.toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }


    public void placeOrder() {

        try {
            sendJson = new JSONObject();
            sendJson.put("method", "placeorder");
            sendJson.put("user_id", SavePref.getPref(PlaceOrderActivity.this,SavePref.User_id));
            sendJson.put("shipping_address_id", getIntent().getStringExtra("ads_id"));
            sendJson.put("doctor_name",placeorderBinding.doctorName.getText().toString());
            sendJson.put("clinic_name", placeorderBinding.clinicAds.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://52.67.139.216/lab/application/customer?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String jsonString = response;//your json string here
                            JSONObject Object = new JSONObject(jsonString).getJSONObject("data");
                         if(new JSONObject(jsonString).getString("status").equalsIgnoreCase("success"))
                         {
                             Intent intent = new Intent(PlaceOrderActivity.this, ThanksActivity.class);
                             intent.putExtra("order_id",Object.getString("order_id"));
                             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                             startActivity(intent);
                         }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("" + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("parameters", sendJson.toString());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }

    private void sendTranjectionId(String tran_id) {
        //creating a string request to send request to the url
        RequestQueue queue = Volley.newRequestQueue(this
        );
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://52.67.139.216/lab/application/cron/updatepaymentstatus?transaction_id="+tran_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            if(new JSONObject(response).getString("status").equalsIgnoreCase("success")) {
                                Intent i = new Intent(PlaceOrderActivity.this, ThanksActivity.class);
// set the new task and clear flags
                                i.putExtra("logout","logout");
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.putExtra("order_id", order_id);

                                startActivity(i);
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Payment fail",Toast.LENGTH_LONG).show();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        //adding the string request to request queue
        queue.add(stringRequest);
    }
    public void startPayment(String order_id,String apiKey)
    {
        Checkout checkout = new Checkout();
        checkout.setImage(R.drawable.bg_edit_text);
        final Activity activity = this;
        try
        {
            JSONObject options = new JSONObject();
            options.put("name",getIntent().getStringExtra("ads"));
            options.put("description", "For Test");
           // options.put("currency", "INR");
            options.put("order_id", order_id);
          //  options.put("amount", ""+totalAmmount*100);
            checkout.setKeyID(apiKey);
            checkout.open(activity, options);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}