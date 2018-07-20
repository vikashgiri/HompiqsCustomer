package com.infoicon.powercoin.main;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.infoicon.powercoin.bottomNavigation.App;
import com.infoicon.powercoin.bottomNavigation.LabBeans;
import com.infoicon.powercoin.bottomNavigation.TestList;
import com.infoicon.powercoin.bottomNavigation.adapter.HomeFragmentAdapter;
import com.infoicon.powercoin.bottomNavigation.adapter.HomeFragmentssAdapter;
import com.infoicon.powercoin.utils.HelperMethods;
import com.infoicon.powercoin.utils.Keys;
import com.infoicon.powercoin.utils.SavePref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.FragmentSearchBinding;

/**
 * A simple {@link Fragment} subclass.
 */


public class SearchFragment extends Fragment
{
private HashMap<String, String> HashMapForURL ;
    private FragmentSearchBinding mBinding;
    private  ArrayList<LabBeans> labListArrayList;

String searchTest=" ";
boolean isLoading=true;
    public SearchFragment()
    {
        // Required empty public constructor
    }
    LinearLayoutManager lLayout;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        labListArrayList=new ArrayList<>();

        initView();

getLabList(searchTest);
      /* mBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = lLayout.findLastVisibleItemPosition();

                if (lastvisibleitemposition == productFragmentAdapter.getItemCount() - 1 && !isLoading) {

                    isLoading=true;
pageNo++;
    getLabList(searchTest);


                }
            }
        });*/
        mBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
searchTest=newText;
                if (newText.length() > 2) {
                    getLabList(newText);
                } else {
                    if (newText.length() <= 0) {

                        getLabList(searchTest);
                    }

                }
                return false;
            }
        });

        return mBinding.getRoot();
   }
    String weekDayName;
    List timeSlots;
    ArrayAdapter dataAdapter;

    ArrayList<String>closeDateArrayList=new ArrayList<>();
    ArrayList<WeekDateBean> weekDateBeanArrayList;

    private int year, month, day;

    private void initView()
    {
        int mNoOfColumns = HelperMethods.calculateNoOfColumns(getActivity());
         lLayout = new LinearLayoutManager(getActivity());
        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setLayoutManager(lLayout);
        mBinding.recyclerView.setNestedScrollingEnabled(false);


    }


    HomeFragmentAdapter productFragmentAdapter;
    private void setProductAdapter() {

         productFragmentAdapter = new
                HomeFragmentAdapter(getActivity(),labListArrayList, new
                HomeFragmentAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(
                            int position) {
                        ((App)getActivity().getApplication()).setLab_id(labListArrayList.get(position).getLab_id());
                        Fragment fragment =new SearchTestFragments();
                        HelperMethods.loadFragment(getActivity(),fragment);
                    }

                });
        mBinding.recyclerView.setAdapter(productFragmentAdapter);
}

    ProgressDialog pDialog;


    void ShowSpinner(Spinner spinner, int position, String date)
    {

        timeSlots.clear();
        if (weekDateBeanArrayList.get(position).getStatus().equalsIgnoreCase("close")) {
            spinner.setEnabled(false);
            timeSlots.add("Lab Closed");
            dataAdapter.notifyDataSetChanged();
            return;
            // Spinner Drop down elements
        }
        for (int i = 0; i < closeDateArrayList.size(); i++) {
            final Calendar myCalendar = Calendar.getInstance();

            // TODO Auto-generated method stub
            String dates[] = closeDateArrayList.get(i).split("-");
            myCalendar.set(Calendar.YEAR, Integer.parseInt(dates[0]));
            myCalendar.set(Calendar.MONTH, Integer.parseInt(dates[1]) - 1);
            myCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dates[2]));
            String myFormat = "MM/dd/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            String close_date = sdf.format(myCalendar.getTime());

            if (date.equals(close_date)) {
                spinner.setEnabled(false);
                timeSlots.add("Lab Closed");
                dataAdapter.notifyDataSetChanged();
                return;
            }
        }
        timeSlots.add("Select Time");
        spinner.setEnabled(true);

        Calendar calendars = Calendar.getInstance();
        String[] start_time = weekDateBeanArrayList.get(position).getOpening_time().split(":");
        String[] end_time = weekDateBeanArrayList.get(position).getClosing_time().split(":");
        calendars.set(Calendar.HOUR_OF_DAY, Integer.parseInt(start_time[0]));
        calendars.set(Calendar.MINUTE, Integer.parseInt(start_time[1]));

        for (int i = 0; i < 200; i++) {
            int start_hours = calendars.get(Calendar.HOUR_OF_DAY);
            int start_minuts = calendars.get(Calendar.MINUTE);
            String starttime = start_hours + ":" + start_minuts;
            calendars.add(Calendar.HOUR_OF_DAY, 2);
            int end_hour = calendars.get(Calendar.HOUR_OF_DAY);
            int end_minut = calendars.get(Calendar.MINUTE);
            if (end_hour >= Integer.parseInt(end_time[0])) {
                calendars.set(Calendar.HOUR_OF_DAY, Integer.parseInt(end_time[0]));
                calendars.set(Calendar.MINUTE, Integer.parseInt(end_time[1]));
                end_hour = calendars.get(Calendar.HOUR_OF_DAY);
                end_minut = calendars.get(Calendar.MINUTE);
                String end_times = end_hour + ":" + end_minut;
                timeSlots.add(starttime + " to " + end_times);
                break;
            }

            String end_times = end_hour + ":" + end_minut;
            timeSlots.add(starttime + " to " + end_times);
        }
        dataAdapter.notifyDataSetChanged();

    }

    private void getTestTimeSlotsApi(String inventry_id) {
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://52.67.139.216:3000/managetestcontroller/getTimeSlotList?lab_inventry_id="+inventry_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            closeDateArrayList.clear();

                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonData = jsonObject.getJSONObject("data");
                            JSONObject lab_open_close_details_JsonOjects = jsonData.getJSONObject("lab_open_close_details");
                            Iterator<String> keys = lab_open_close_details_JsonOjects.keys();
                            weekDateBeanArrayList = new ArrayList<>();
                            while (keys.hasNext()) {
                                WeekDateBean weekDateBean = new WeekDateBean();
                                String key = keys.next();
                                JSONObject innerJObject = lab_open_close_details_JsonOjects.getJSONObject(key);
                                weekDateBean.setStatus(innerJObject.getString("status"));
                                if (innerJObject.has("opening_time")) {
                                    weekDateBean.setOpening_time(innerJObject.getString("opening_time"));
                                }
                                if (innerJObject.has("closing_time")) {
                                    weekDateBean.setClosing_time(innerJObject.getString("closing_time"));
                                }
                                weekDateBean.setWeekDayName(key);
                                weekDateBeanArrayList.add(weekDateBean);
                            }

                            try {
                                JSONArray lab_closed_dateArray = jsonData.getJSONArray("lab_closed_date");
                                // jsonString is a string variable that holds the JSON
                                for (int i = 0; i < lab_closed_dateArray.length(); i++) {
                                    closeDateArrayList.add(lab_closed_dateArray.getString(i));

                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            showDialogs();
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

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

    void Dialogs(final int position)
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }

        builder.setTitle("Change Lab")
                .setMessage("Change Lab will delete your added items?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        ((App)getActivity().getApplication()).setLab_id(labListArrayList.get(position).getLab_id());
                        getTestTimeSlotsApi(labListArrayList.get(position).getInventry_id());
                        updateCart();


                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
JSONObject sendJson;
    private void updateCart()
    {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        try {
            sendJson = new JSONObject();
            sendJson.put("method", "addtocart");

            if(SavePref.getPref(getActivity(),SavePref.is_loogedin).equalsIgnoreCase("true"))
            {
                sendJson.put("user_id", SavePref.getPref(getActivity(),SavePref.User_id));
            }
            else
            {
                sendJson.put("guest_user_id",Keys.device_id);


            }
            sendJson.put("number_of_item","1");
            sendJson.put("merchant_inventry_id", "");
            sendJson.put("action", "clearcart");
            sendJson.put("item_name", "");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Keys.BASE_URL+"application/customer",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(""+response);
                        try
                        {
                            HelperMethods.getCartItems(getActivity());
                            // showDialogs();
                            pDialog.dismiss();
                        }
                        catch (Exception e)
                        {
                            pDialog.dismiss();

                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), "Communication Error!", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getActivity(), "Authentication Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getActivity(), "Server Side Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getActivity(), "Network Error!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getActivity(), "Parse Error!", Toast.LENGTH_SHORT).show();
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



    public   void showDialogs()
    {
        // Create custom dialog object
        final Dialog dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_test_collection);
        // Set dialog title
        dialog.setTitle("Custom Dialog");
        final RadioGroup radioGroup = (RadioGroup)dialog.findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if(rb.getText().toString().equalsIgnoreCase("Visit Lab"))
                {
                    ((App)getActivity().getApplication()).setTest_location("lab");

                }
                else {
                    ((App)getActivity().getApplication()).setTest_location("home");
                }
                    /*Fragment fragment =new SearchTestFragments();
                    HelperMethods.loadFragment(getActivity(),fragment);
*/
                // dialog.dismiss();
            }
        });
        // Spinner element
        final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);

        dialog.setCancelable(false);
        timeSlots = new ArrayList();
        timeSlots.clear();
        timeSlots.add("Select Date First");

        spinner.setEnabled(false);
        dataAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, timeSlots);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        // Spinner click listener

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        final TextView text_date = (TextView) dialog.findViewById(R.id.text_date);

        final TextView back = (TextView) dialog.findViewById(R.id.text_date);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final Calendar myCalendar = Calendar.getInstance();
        final Button submit = (Button) dialog.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItemPosition() <= 0) {
                    Toast.makeText(getActivity(), "Please Select Time", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (radioGroup.getCheckedRadioButtonId()== -1) {
                    Toast.makeText(getActivity(), "Please Select Test Locations", Toast.LENGTH_SHORT).show();
                    return;
                }
                ((App)getActivity().getApplication()).setSelected_time_slots(timeSlots.get(spinner.getSelectedItemPosition()).toString());
                ((App)getActivity().getApplication()).setSelected_date(text_date.getText().toString());

                dialog.dismiss();
                Fragment fragment =new SearchTestFragments();
                HelperMethods.loadFragment(getActivity(),fragment);
            }
        });

        final Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                Date date = new Date(year, monthOfYear, dayOfMonth - 1);
                String dayOfWeek = simpledateformat.format(date);

                text_date.setText(sdf.format(myCalendar.getTime()));
                weekDayName = dayOfWeek;
                for (int i = 0; i < weekDateBeanArrayList.size(); i++) {
                    if (weekDateBeanArrayList.get(i).getWeekDayName().equalsIgnoreCase(weekDayName)) {

                        ShowSpinner(spinner, i, sdf.format(myCalendar.getTime()));
                        break;
                    }
                }

            }

        };

        text_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });

        dialog.show();
    }

     ProgressDialog progressDialog;

    private void getLabList(String newText)
    {
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);

        //creating a string request to send request to the url/*

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Keys.BASE_URL_MANAGE_CONTROLLER+"searchLab?lat="+ SavePref.getPref(getContext(),SavePref.current_lat)+"&lng="+SavePref.getPref(getContext(),SavePref.current_lon)+"&get_all_lab_from_city=1&city_id="+SavePref.getPref(getContext(),SavePref.city_id)+"&name="+newText,
//http://52.67.139.216:3000/managetestcontroller/searchLab?lat="+ SavePref.getPref(getContext(),SavePref.current_lat)+"&lng="+SavePref.getPref(getContext(),SavePref.current_lon)+"&get_all_lab_from_city=1&city_id="+SavePref.getPref(getContext(),SavePref.city_id)+"&name="+newText,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            String imageRootPath=jsonObject.getString("imageRootPath");
                            String type=jsonObject.getString("type");

    labListArrayList.clear();

                            aa:          for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                LabBeans labBeans=new LabBeans();
                                labBeans.setLab_id(jsonObject1.getString("lab_id"));
                                labBeans.setLab_name(jsonObject1.getString("lab_name"));
                                labBeans.setReg_number(jsonObject1.getString("reg_number"));
                                labBeans.setLab_address(jsonObject1.getString("lab_address"));
                                labBeans.setCity_id(jsonObject1.getString("city_id"));
                                labBeans.setLab_google_address(jsonObject1.getString("lab_google_address"));
                                labBeans.setLat(jsonObject1.getString("lat"));
                                labBeans.setLng(jsonObject1.getString("lng"));
                                labBeans.setLabImage(imageRootPath+"/"+type+"/"+jsonObject1.getString("lab_image"));

                                // labBeans.setTest_id(jsonObject1.getString("test_id"));
                                //  labBeans.setPrice(jsonObject1.getString("price"));
                                //labBeans.setDiscount_type(jsonObject1.getString("discount_type"));
                                //labBeans.setDiscount_value(jsonObject1.getString("discount_value"));
                                //labBeans.setTime_consumed_by_test(jsonObject1.getString("time_consumed_by_test"));
                             //   labBeans.setLab_distance(jsonObject1.getString("lab_distance").substring(0,2)+"\n Km");

                                for(int j=0;j<labListArrayList.size();j++)
                                {
                                    if (labListArrayList.get(j).getLab_id().equalsIgnoreCase(jsonObject1.getString("lab_id")))
                                    {
                                        continue aa;
                                    }
                                }

                                labListArrayList.add(labBeans);
                            }
                            progressDialog.dismiss();
                                setProductAdapter();
                        }catch (Exception e)
                        {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        progressDialog.dismiss();
                    }
                });
        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
}
