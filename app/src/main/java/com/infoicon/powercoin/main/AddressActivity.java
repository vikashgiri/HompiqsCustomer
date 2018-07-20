package com.infoicon.powercoin.main;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infoicon.powercoin.login.CityListActivitys;
import com.infoicon.powercoin.login.DoctorNameHint;
import com.infoicon.powercoin.utils.HelperMethods;
import com.infoicon.powercoin.utils.Keys;
import com.infoicon.powercoin.utils.SavePref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.FragmentCheckOutBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddressActivity extends AppCompatActivity {

    JSONObject sendJson;
    public AddressActivity() {
        // Required empty public constructor
    }
    ArrayList<DoctorNameHint> aDoctorNameHints=new ArrayList<>();
    FragmentCheckOutBinding mBinding;
    ArrayList<AddressListBean> addressListBeanArrayList;

    @Override
    protected void onResume() {
        super.onResume();

        mBinding.txtLocality.setText(""+SavePref.getPref(AddressActivity.this,SavePref.address_list));
        mBinding.txtCity.setText(""+SavePref.getPref(AddressActivity.this,SavePref.City_name));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.fragment_check_out);
        mBinding.toolbar.txtTitle.setText(getString(R.string.check_out));
        //Creating the instance of ArrayAdapter containing list of language names
            //Getting the instance of AutoCompleteTextView

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        mBinding.toolbar.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.txtLocality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressActivity.this, CityListActivitys.class);
                intent.putExtra("city_type","address");

                startActivity(intent);
            }
        });
        // Spinner click listener

        // Spinner Drop down elements

        List categories = new ArrayList();
        categories.add("Gender");
        categories.add("Male");
        categories.add("Female");

        // Creating adapter for spinner
        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        addAddress();
        mBinding.btnBookTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAddress();
            }
        });
    }

   /*     if(getIntent().hasExtra("type"))
        {
            String ss = getIntent().getStringExtra("data");
            Gson gson = new Gson();
            Type type = new TypeToken<List<AddressListBean>>() {
            }.getType();
            addressListBeanArrayList=new ArrayList<>();
            addressListBeanArrayList = gson.fromJson(ss, type);
            int position = Integer.parseInt(getIntent().getStringExtra("position"));
            mBinding.txtFName.setText(""+addressListBeanArrayList.get(position).getContact_name());
            //txt_city_name.setText(addressListBeanArrayList.get(position).get);
            mBinding.txtFName.setText(addressListBeanArrayList.get(position).getHouse_number());
            txt_area.setText(addressListBeanArrayList.get(position).getArea());
            address_nickname.setText(addressListBeanArrayList.get(position).getAddress_nickname());
            street_detail.setText(addressListBeanArrayList.get(position).getStreet_detail());
            landmark.setText(addressListBeanArrayList.get(position).getLandmark());
            zipcode.setText(addressListBeanArrayList.get(position).getZipcode());
        }
    }
*/

    public  void addAddress()
    {
        if (!HelperMethods.isEmpty(mBinding.txtFName.getText().toString())) {
            mBinding.txtFName.setError("Please Enter Name");
            mBinding.txtFName.requestFocus();
            return;
        }
        if (!HelperMethods.isEmpty(mBinding.age.getText().toString())) {
            mBinding.age.setError("Please Enter Age");
            mBinding.age.requestFocus();
            return;
        }
        if (!HelperMethods.isEmpty(mBinding.txtMobile.getText().toString())) {
            mBinding.txtMobile.setError("Please Enter Mobile");
            mBinding.txtMobile.requestFocus();
            return;
        }
        if (!HelperMethods.isEmpty(mBinding.txtAds.getText().toString()))
        {
            mBinding.txtAds.setError("Please Enter Address");
            mBinding.txtAds.requestFocus();
            return;
        }
        if(mBinding.spinner.getSelectedItemPosition()<=0)
        {
            Toast.makeText(getApplicationContext(),"Please Select Gender",Toast.LENGTH_SHORT).show();
            return;
        }
        try
        {
            sendJson = new JSONObject();
            sendJson.put("method", "addeditdeliveryaddress");
            sendJson.put("contact_name",mBinding.txtFName.getText().toString());
            sendJson.put("city_id", SavePref.getPref(AddressActivity.this,SavePref.city_id));
            sendJson.put("city_name",SavePref.getPref(AddressActivity.this,SavePref.City_name));
            sendJson.put("house_number", mBinding.txtAds.getText().toString());
            sendJson.put("user_id", SavePref.getPref(AddressActivity.this,SavePref.User_id));
            sendJson.put("street_detail", mBinding.txtLocality.getText().toString());
            sendJson.put("zipcode", " ");
            sendJson.put("gender", mBinding.spinner.getSelectedItem().toString());
            sendJson.put("age", mBinding.age.getText().toString());
            sendJson.put("mobile", mBinding.txtMobile.getText().toString());
            sendJson.put("relation", mBinding.relation.getText().toString());
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
                            String jsonString = response;//your json string here
                           if(new JSONObject(jsonString).getString("status").equalsIgnoreCase("success"))
                            {

                             HelperMethods.showToastS(AddressActivity.this,"Address Save Successfully");
                              JSONObject jsonObject=  new JSONObject(jsonString).getJSONObject("data");
                                Intent intent = new Intent(AddressActivity.this, PlaceOrderActivity.class);
                                intent.putExtra("ads_id",jsonObject.getString("id"));
                                intent.putExtra("ads",mBinding.txtFName.getText().toString()+"\n"+mBinding.txtAds.getText().toString());
                                startActivity(intent);
                        }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("" + error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("parameters", sendJson.toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
