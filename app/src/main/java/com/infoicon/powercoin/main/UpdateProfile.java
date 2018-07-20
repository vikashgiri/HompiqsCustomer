package com.infoicon.powercoin.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infoicon.powercoin.bottomNavigation.LabBeans;
import com.infoicon.powercoin.utils.HelperMethods;
import com.infoicon.powercoin.utils.Keys;
import com.infoicon.powercoin.utils.SavePref;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.ActivityRegistrationBinding;
import infoicon.com.powercoin.databinding.UpdateprofileBinding;

/**
 * Created by HP-PC on 3/20/2018.
 */

public class UpdateProfile extends Fragment {
    private HashMap<String, String> HashMapForURL;
    private ArrayList<LabBeans> labListArrayList;
    private ArrayList<LabBeans> uniqueLabListArrayList;
    UpdateprofileBinding mBinding;
    ProgressDialog pDialog;
    JSONObject sendJson;
    public UpdateProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
       mBinding = DataBindingUtil.inflate(
                inflater, R.layout.updateprofile, container, false);

mBinding.fname.setText(SavePref.getPref(getActivity(),SavePref.Name));
        mBinding.email.setText(SavePref.getPref(getActivity(),SavePref.Email));
        mBinding.mobile.setText(SavePref.getPref(getActivity(),SavePref.Mobile));
        mBinding.address.setText(SavePref.getPref(getActivity(),SavePref.address));
mBinding.changePassword.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), ChangePassword.class);
        startActivity(intent);

    }
});
mBinding.btnUpdate.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        changePassword();
    }
});
        return mBinding.getRoot();

    }


    private void changePassword(){

        final String fname = mBinding.fname.getText().toString();
        final String email = mBinding.email.getText().toString();
        final String mobile = mBinding.mobile.getText().toString();
        final String address = mBinding.address.getText().toString();

        if(!HelperMethods.isNetworkAvailable(getActivity()))
        {
            Snackbar snackbar = Snackbar
                    .make(mBinding.fname,getString(R.string.connectio_error), Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }

        if (!HelperMethods.isEmpty(fname)) {
            mBinding.fname.setError("Enter First Name");
            mBinding.fname.requestFocus();

            return;
        }

        if (!HelperMethods.isEmpty(email)) {
            mBinding.email.setError("Enter Email");
            mBinding.email.requestFocus();

            return;
        }

        if (!HelperMethods.emailValidator(email)) {
            mBinding.email.setError("Enter Valid Email");
            mBinding.email.requestFocus();

            return;
        }

        if (!HelperMethods.isEmpty(mobile)) {
            mBinding.mobile.setError("Invalid Mobile");
            mBinding.mobile.requestFocus();

            return;
        }
        if (!HelperMethods.isEmpty(address)) {
            mBinding.address.setError("Enter  Address");
            mBinding.address.requestFocus();

            return;
        }


        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        try
        {
            sendJson = new JSONObject();
            sendJson.put("method", "addedituser");
            sendJson.put("city_id", SavePref.getPref(getActivity(),SavePref.city_id));
            sendJson.put("name", fname);
            sendJson.put("email", email);
            sendJson.put("mobile_number", mobile);
            sendJson.put("id", SavePref.getPref(getActivity(),SavePref.User_id));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());

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
                                SavePref.saveStringPref(getActivity(), SavePref.Name, fname);
                                SavePref.saveStringPref(getActivity(), SavePref.address, address);
                                SavePref.saveStringPref(getActivity(), SavePref.Email, email);
                                SavePref.saveStringPref(getActivity(), SavePref.Mobile, mobile);
                                HelperMethods.showSnackBar(getActivity(), "Profile Updated successfully");
                            } else {
                                pDialog.dismiss();
                                HelperMethods.showSnackBar(getActivity(), "Registration Error");
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

