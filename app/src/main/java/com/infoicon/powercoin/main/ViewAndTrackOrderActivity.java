package com.infoicon.powercoin.main;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.ViewandtrackorderactivityBinding;

/**
 * Created by HP-PC on 11/19/2017.
 */

public class ViewAndTrackOrderActivity extends Fragment
{
    ProgressDialog pDialog;
    JSONObject sendJson;
    ViewAndTrackOrderBeans viewAndTrackOrderBeans;
    ArrayList<ViewAndTrackOrderBeans>viewAndTrackOrderBeansArrayList=new ArrayList<>();
    ArrayList<ViewAndTrackOrderDetail>viewAndTrackItemOrderBeanArrayList;
    ViewandtrackorderactivityBinding mBinding;
    LinearLayoutManager mLayoutManager;
    ViewAndTrackOrderAdapter   fAdapter;
    boolean isLoading=true;
    int pageCount=1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.viewandtrackorderactivity, container, false);

        mBinding.recyclerViewSubcat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = mLayoutManager.findLastVisibleItemPosition();

                if (lastvisibleitemposition == fAdapter.getItemCount() - 1 && !isLoading) {

                    isLoading=true;
                    // bottomProgress.setVisibility(View.VISIBLE);
                    pageCount++;
                    mBinding.bottomProgressBar.setVisibility(View.VISIBLE);
                    getOrder();
                }
            }
        });
        return mBinding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (viewAndTrackOrderBeansArrayList.size() <= 0) {
            getOrder();
        }
    }

    void  getOrder()
    {
        try
        {
            sendJson = new JSONObject();
            sendJson.put("method", "orderlist");
            sendJson.put("user_id",  SavePref.getPref(getContext(),SavePref.User_id));
           //  sendJson.put("user_id","1");
            sendJson.put("pagination","1");
            sendJson.put("order_status", "");
            sendJson.put("page", pageCount);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");

        pDialog.show();
        pDialog.setCancelable(false);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://52.67.139.216/lab/application/customer?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString = "";//your json string here
                        try {
                            if (pageCount == 1) {
                                viewAndTrackOrderBeansArrayList.clear();
                            }
                            JSONObject jObject = new JSONObject(response).getJSONObject("data");
                            Iterator<String> keys = jObject.keys();
                            viewAndTrackOrderBeansArrayList.clear();

                            while (keys.hasNext()) {
                                viewAndTrackItemOrderBeanArrayList =
                                        new ArrayList<>();
                                viewAndTrackItemOrderBeanArrayList.clear();
                                String key = keys.next();
                                JSONObject innerJObject = jObject.getJSONObject(key);
                                viewAndTrackOrderBeans = new ViewAndTrackOrderBeans();
                                try {
                                    JSONObject jObjectRider = new JSONObject(response).getJSONObject("rider_list");
                                    Iterator<String> keysRider = jObjectRider.keys();

                                    while (keysRider.hasNext()) {
                                        String keyRide = keysRider.next();
                                        JSONObject innerJObjectRide = jObjectRider.getJSONObject(keyRide);
                                        viewAndTrackOrderBeans.setRider_id(innerJObjectRide.getString("id"));
                                    }
                                } catch (Exception e)

                                {
                                    e.printStackTrace();
                                }
                                JSONObject order_detailsJObject = innerJObject.getJSONObject("order_details");

                                viewAndTrackOrderBeans.setId(order_detailsJObject.getString("id"));
                                viewAndTrackOrderBeans.setUser_id(order_detailsJObject.getString("user_id"));
                                viewAndTrackOrderBeans.setOrder_id(order_detailsJObject.getString("order_id"));
                                viewAndTrackOrderBeans.setParent_order_id(order_detailsJObject.getString("parent_order_id"));
                                viewAndTrackOrderBeans.setOtp(order_detailsJObject.getString("otp"));

                                viewAndTrackOrderBeans.setShipping_address_id(order_detailsJObject.getString("shipping_address_id"));

                                JSONObject jObjectShipping = new JSONObject(response).getJSONObject("shipping_address_list");
                                Iterator<String> keysShipping = jObjectShipping.keys();

                                while (keysShipping.hasNext()) {

                                    String keyShipping = keysShipping.next();
                                    JSONObject innerJObjectShipping = jObjectShipping.getJSONObject(keyShipping);
                                    if (order_detailsJObject.getString("shipping_address_id").equalsIgnoreCase(innerJObjectShipping.getString("id"))) {
                                        viewAndTrackOrderBeans.setShipping_address(innerJObjectShipping.getString("house_number") + "," + innerJObjectShipping.getString("street_detail") + "," + innerJObjectShipping.getString("city_name"));
                                    }
                                }
                                viewAndTrackOrderBeans.setAmount(order_detailsJObject.getString("amount"));
                                viewAndTrackOrderBeans.setPayable_amount(order_detailsJObject.getString("payable_amount"));
                                viewAndTrackOrderBeans.setDiscount_amount(order_detailsJObject.getString("discount_amount"));
                                viewAndTrackOrderBeans.setCommission_amount(order_detailsJObject.getString("commission_amount"));

                                viewAndTrackOrderBeans.setTax_amount(order_detailsJObject.getString("tax_amount"));
                                viewAndTrackOrderBeans.setPayment_status(order_detailsJObject.getString("payment_status"));

                                viewAndTrackOrderBeans.setOrder_status(order_detailsJObject.getString("order_status"));
                                viewAndTrackOrderBeans.setCreated_date(order_detailsJObject.getString("created_date"));
                                viewAndTrackOrderBeans.setUpdated_date(order_detailsJObject.getString("updated_date"));

                                JSONObject orderitem_JObject = innerJObject.getJSONObject("orderitem");
                                Iterator<String> orderitem_keys = orderitem_JObject.keys();

                                while (orderitem_keys.hasNext()) {
                                    String orderitem_key = orderitem_keys.next();
                                    JSONObject orderitemJObject = orderitem_JObject.getJSONObject(orderitem_key);
                                    ViewAndTrackOrderDetail viewAndTrackItemOrderBean = new ViewAndTrackOrderDetail();
                                    viewAndTrackItemOrderBean.setOrder_id(orderitemJObject.getString("order_id"));
                                    viewAndTrackItemOrderBean.setLab_test_id(orderitemJObject.getString("lab_test_id"));
                                    viewAndTrackItemOrderBean.setNumber_of_item(orderitemJObject.getString("number_of_item"));

                                    viewAndTrackItemOrderBean.setNumber_of_item(orderitemJObject.getString("number_of_item"));
                                    viewAndTrackItemOrderBean.setAmount(orderitemJObject.getString("amount"));
                                    viewAndTrackItemOrderBean.setCommission_amount(orderitemJObject.getString("commission_amount"));

                                    viewAndTrackItemOrderBean.setDiscount_amount(orderitemJObject.getString("discount_amount"));
                                    viewAndTrackItemOrderBean.setTax_amount(orderitemJObject.getString("tax_amount"));
                                    viewAndTrackItemOrderBean.setStatus(orderitemJObject.getString("status"));
                                    viewAndTrackItemOrderBean.setCreated_by(orderitemJObject.getString("created_by"));
                                    viewAndTrackItemOrderBean.setUpdated_by(orderitemJObject.getString("updated_by"));
                                    JSONObject productdumpJObject = orderitemJObject.getJSONObject("test_dump");
                                    JSONObject productJObject = productdumpJObject.getJSONObject("product_details");
                                    viewAndTrackItemOrderBean.setTest_id(productJObject.getString("test_id"));
                                    viewAndTrackItemOrderBean.setLab_id(productJObject.getString("lab_id"));
                                    viewAndTrackItemOrderBean.setId(productJObject.getString("id"));

                                    viewAndTrackItemOrderBean.setPrice(productJObject.getString("price"));
                                    viewAndTrackItemOrderBean.setDiscount_type(productJObject.getString("discount_type"));
                                    viewAndTrackItemOrderBean.setDiscount_value(productJObject.getString("discount_value"));
                                    viewAndTrackItemOrderBean.setTest_name(productJObject.getString("test_name"));
                                    if (productJObject.has("lab_name")) {
                                        viewAndTrackItemOrderBean.setLab_name(productJObject.getString("lab_name"));
                                        viewAndTrackItemOrderBean.setLab_address(productJObject.getString("lab_address"));
                                    } else {
                                        viewAndTrackItemOrderBean.setLab_name(" ");
                                        viewAndTrackItemOrderBean.setLab_address(" ");
                                    }

                                    viewAndTrackItemOrderBean.setCreated_by(order_detailsJObject.getString("created_date"));
                                    viewAndTrackItemOrderBeanArrayList.add(viewAndTrackItemOrderBean);
                                }
                                viewAndTrackOrderBeans.setViewAndTrackItemOrderBeanArrayList(viewAndTrackItemOrderBeanArrayList);
                                viewAndTrackOrderBeansArrayList.add(viewAndTrackOrderBeans);
                            }
                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }
                        pDialog.dismiss();

                        if (pageCount > 1) {
                            fAdapter.notifyDataSetChanged();
                        } else {

                            fAdapter = new ViewAndTrackOrderAdapter(getActivity(),
                                    viewAndTrackOrderBeansArrayList, mBinding.recyclerViewSubcat);
                            mLayoutManager = new LinearLayoutManager(getActivity());
                            mBinding.recyclerViewSubcat.setLayoutManager(mLayoutManager);
                            mBinding.recyclerViewSubcat.setItemAnimator(new DefaultItemAnimator());
                            mBinding.recyclerViewSubcat.setAdapter(fAdapter);
                        }

                        mBinding.recyclerViewSubcat.setVisibility(View.VISIBLE);
                        mBinding.bottomProgressBar.setVisibility(View.GONE);
                        mBinding.bottomProgressBar.setVisibility(View.GONE);





                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    HelperMethods.showSnackBar(getActivity(),"Communication Error!");
                } else if (error instanceof AuthFailureError) {
                    HelperMethods.showSnackBar(getActivity(), "Authentication Error!");
                } else if (error instanceof ServerError) {
                    HelperMethods.showSnackBar(getActivity(),"Server Side Error!");
                } else if (error instanceof NetworkError) {
                    HelperMethods.showSnackBar(getActivity(), "Network Error!");
                } else if (error instanceof ParseError) {
                    HelperMethods.showSnackBar(getActivity(),"Parse Error!");
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