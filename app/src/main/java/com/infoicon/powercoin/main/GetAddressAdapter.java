package com.infoicon.powercoin.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.infoicon.powercoin.login.CityListActivitys;
import com.infoicon.powercoin.utils.HelperMethods;
import com.infoicon.powercoin.utils.SavePref;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import infoicon.com.powercoin.R;


/**
 * Created by asdfgh on 10/17/2017.
 */

public class GetAddressAdapter extends RecyclerView.Adapter<GetAddressAdapter.MyViewHolder> {
    ArrayList<AddressListBean>addressListBeanArrayList;
    Context context;
String flag;
ProgressDialog pDialog;

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }


            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.name.setText(addressListBeanArrayList.get(position).getContact_name());
        /*if(flag.equalsIgnoreCase("checkout"))
        {
            holder.checkbox.setVisibility(View.VISIBLE);
        }
        else {
            holder.checkbox.setVisibility(View.GONE);

        }*/
        holder.text_address.setText(addressListBeanArrayList.get(position).getGender() + "\n" +
                addressListBeanArrayList.get(position).getCity_name() + "," +addressListBeanArrayList.get(position).getHouse_number());
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!addressListBeanArrayList.get(position).getStreet_detail().isEmpty()){
                LatLng latLng=getLocationFromAddress(context,addressListBeanArrayList.get(position).getStreet_detail());
                getCity(addressListBeanArrayList.get(position).getStreet_detail(),String.valueOf(latLng.latitude),String.valueOf(latLng.longitude),position, holder.checkbox);}else
                {
                holder.checkbox.setChecked(false);
                }


            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
deleteAddress(addressListBeanArrayList.get(position).getId(),position);
            }
        });
    }



    @Override
    public int getItemCount() {
        return addressListBeanArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,text_address,items;
        Button button_minus, button_plus;
        Spinner item_spinner;

        ImageView delete;
        CheckBox checkbox;
        public MyViewHolder(View itemView) {
            super(itemView);
            delete=(ImageView)itemView.findViewById(R.id.delete);

            name = (TextView) itemView.findViewById(R.id.name);
            text_address = (TextView) itemView.findViewById(R.id.text_address);
            checkbox = (CheckBox) itemView.findViewById(R.id.check_box);
        }
    }

    public GetAddressAdapter(Context context, ArrayList<AddressListBean> addressListBeanArrayList, String flag) {
        this.addressListBeanArrayList = addressListBeanArrayList;
        this.context=context;
        this.flag=flag;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.getaddress_list_adapter, parent, false);
        return new MyViewHolder(itemView);

    }



JSONObject manJson;

    private void getCity(String city, final String lat, final String lan, final int position, final CheckBox checkBox)
    {

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        try {
            manJson = new JSONObject();
            manJson.put("method", "getCityIdByAddressOrLatLng");
            manJson.put("address", city);
            manJson.put("lat", lat);
            manJson.put("lng", lan);
            manJson.put("user_id",  SavePref.getPref(context,SavePref.User_id));
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://52.67.139.216/lab/application/index?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString = "";//your json string here
                        try {
                            // {"status":"success","data":{"id":5,"city_name":"Bengaluru","city_synonym":"banglore","country_id":1,"created_on":"2018-03-20 22:58:43","updated_on":"2018-05-06 19:24:29"},"service":{"status":"success"}}
                            JSONObject Object = new JSONObject(response);
                            JSONObject jObject = new JSONObject(response).getJSONObject("data");

                            if(!new JSONObject(response).has("service")) {

                                pDialog.dismiss();
                                Toast.makeText(context,"Service Not Available in this city",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            JSONObject servicejObject = new JSONObject(response).getJSONObject("service");
                            // 77.59456269443035
                            // 12.97161246899558
                            if (!servicejObject.getString("status").equalsIgnoreCase("success")) {
                                pDialog.dismiss();
                                 Toast.makeText(context, "Service Not Available", Toast.LENGTH_SHORT).show();
                                 checkBox.setChecked(false);
                                return;
                            }
                            Intent intent = new Intent(context, PlaceOrderActivity.class);
                            intent.putExtra("ads_id", addressListBeanArrayList.get(position).getId());
                            intent.putExtra("ads", addressListBeanArrayList.get(position).getContact_name()+ "\n" + addressListBeanArrayList.get(position).getHouse_number()+" "+addressListBeanArrayList.get(position).getCity_id());
                            context.startActivity(intent);

                        } catch (Exception e) {
                            checkBox.setChecked(false);
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                checkBox.setChecked(false);

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String,String>();
                params.put("parameters",manJson.toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }




    private void deleteAddress(String id,final int position)
    {

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        try {
            manJson = new JSONObject();
            manJson.put("method", "deleteshippingaddress");
            manJson.put("id", id);
            manJson.put("user_id",  SavePref.getPref(context,SavePref.User_id));
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://52.67.139.216/lab/application/customer?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonString = "";//your json string here
                        try {
                            // {"status":"success","data":{"id":5,"city_name":"Bengaluru","city_synonym":"banglore","country_id":1,"created_on":"2018-03-20 22:58:43","updated_on":"2018-05-06 19:24:29"},"service":{"status":"success"}}
                            JSONObject Object = new JSONObject(response);


                            if (!Object.getString("status").equalsIgnoreCase("success")) {
                                pDialog.dismiss();
                                addressListBeanArrayList.remove(position);
                                notifyDataSetChanged();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String,String>();
                params.put("parameters",manJson.toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }



}
