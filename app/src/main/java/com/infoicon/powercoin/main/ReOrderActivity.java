package com.infoicon.powercoin.main;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.infoicon.powercoin.bottomNavigation.CardListResponce;
import com.infoicon.powercoin.bottomNavigation.TestList;
import com.infoicon.powercoin.login.LoginActivity;
import com.infoicon.powercoin.utils.HelperMethods;
import com.infoicon.powercoin.utils.SavePref;
import com.infoicon.powercoin.utils.UpdateCardListInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.ActivityCartBinding;
import infoicon.com.powercoin.databinding.ActivityReOrderBinding;
import infoicon.com.powercoin.databinding.FragmentSearchTestBinding;

public class ReOrderActivity extends AppCompatActivity implements UpdateCardListInterface{
    ProgressDialog progressDialog;
    HashMap<String, String> HashMapForURL;
    ActivityReOrderBinding mBinding;
    JSONObject sendJson;
    ArrayList<WeekDateBean> weekDateBeanArrayList;
    ArrayList<String>closeDateArrayList=new ArrayList<>();

    ArrayList<CardListResponce> cardListResponceArrayList = new ArrayList<>();
    int arrayPosition;

     ReOrderAdapter reOrderActivity;
    ProgressBar mprogressBar;

    SearchView search;

    private int year, month, day;
    ArrayAdapter dataAdapter;
    public ReOrderActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onResume() {
        super.onResume();
        try
        {
            mBinding.mainToolbar.cartCount.setText("" + ((App) this.getApplication()).getCardList().size());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        };
    }

    @Override
    public void updateCard(ArrayList<CardListResponce> cardListResponceArrayList) {
        mBinding.mainToolbar.cartCount.setText(""+cardListResponceArrayList.size());
        if(viewAndTrackOrderBeansList.get(arrayPosition).getViewAndTrackItemOrderBeanArrayList().size()<=0)
            finish();
    }
    private void initView() {
        LinearLayoutManager lLayout = new LinearLayoutManager(this);
        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setLayoutManager(lLayout);
        mBinding.recyclerView.setNestedScrollingEnabled(false);
    }

    ReOrderAdapter cartFragmentAdapter;
     List<ViewAndTrackOrderBeans> viewAndTrackOrderBeansList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_re_order);
       // mBinding.mainToolbar.cityName.setVisibility(View.GONE);
        //mBinding.mainToolbar.attachement.setVisibility(View.GONE);
        mBinding.mainToolbar.toolbarBack.setVisibility(View.VISIBLE);
        mBinding.mainToolbar.cartLayout.setVisibility(View.VISIBLE);
        mBinding.mainToolbar.cartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    if(((App)getApplication()).getCardList().size()>0)
                    {
                        Intent intent = new Intent(ReOrderActivity.this, CartActivity.class);
                        startActivity(intent);
                    }

                    else
                    {
                        HelperMethods.showSnackBar(ReOrderActivity.this,"Please Select " +
                                "Test First");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        });

        mBinding.mainToolbar.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        mBinding.mainToolbar.txtTitle.setText("ReOrder");
        Gson gson = new Gson();
        Type type = new TypeToken<List<ViewAndTrackOrderBeans>>() {
        }.getType();
         viewAndTrackOrderBeansList = gson.fromJson(getIntent().getStringExtra("data"), type);
        arrayPosition = Integer.parseInt(getIntent().getStringExtra("position"));

   reOrderActivity = new
                ReOrderAdapter(this, viewAndTrackOrderBeansList.get(arrayPosition).getViewAndTrackItemOrderBeanArrayList(), new
                ReOrderAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        ArrayList<CardListResponce> cardListResponceArrayList= ((App)getApplication()).getCardList()    ;
                        if(cardListResponceArrayList!=null) {
                            for (int i = 0; i < cardListResponceArrayList.size(); i++) {
                                if (cardListResponceArrayList.get(i).getCardListDetailResponceArrayList().getLab_id()
                                        .equalsIgnoreCase(viewAndTrackOrderBeansList.get(arrayPosition).getViewAndTrackItemOrderBeanArrayList().get(position).getLab_id()) && cardListResponceArrayList.get(i).getTest_location()
                                        .equalsIgnoreCase(((App)getApplication()).getTest_location())
                                        ) {
                                    ((App)getApplication()).setTest_location("lab");
                                    HelperMethods.updateCart(ReOrderActivity.this, viewAndTrackOrderBeansList.get(arrayPosition).getViewAndTrackItemOrderBeanArrayList().get(position).getId(), "add", cardListResponceArrayList.get(i).getTest_date(),
                                            cardListResponceArrayList.get(i).getTime_slot_start_time() + "to" +
                                                    cardListResponceArrayList.get(i).getTime_slot_start_time());
                                    viewAndTrackOrderBeansList.get(arrayPosition).getViewAndTrackItemOrderBeanArrayList().remove(position);
                                    reOrderActivity.notifyDataSetChanged();
                                    return;
                                }

                            }
                        }
                        getTestTimeSlotsApi(position);

                        }

                });
        mBinding.recyclerView.setAdapter(reOrderActivity);
                }




    void ShowSpinner(Spinner spinner, int position, String date) {

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
    List timeSlots;

    private void getTestTimeSlotsApi(final int position) {
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://52.67.139.216:3000/managetestcontroller/getTimeSlotList?lab_inventry_id="+viewAndTrackOrderBeansList.get(0).getViewAndTrackItemOrderBeanArrayList().get(0).getLab_test_id(),
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
                            showDialogs(position);
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }


     /*void showDialogs(final int position) {
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        View view = getLayoutInflater().inflate(R.layout.datetime, null);
        dialog.setContentView(view);
        // Spinner element
        final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);

        dialog.setCancelable(false);
        timeSlots = new ArrayList();
        timeSlots.clear();
        timeSlots.add("Select Date First");

        spinner.setEnabled(false);
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, timeSlots);
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
        final TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
        txt_title.setText("Select Date");

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
                    Toast.makeText(ReOrderActivity.this, "Please Select Time", Toast.LENGTH_SHORT).show();
                    return;
                }
                ((App)getApplication()).setTest_location("lab");

                HelperMethods.updateCart(ReOrderActivity.this,viewAndTrackOrderBeansList.get(arrayPosition).getViewAndTrackItemOrderBeanArrayList().get(position).getId(), "add", text_date.getText().toString(), timeSlots.get(spinner.getSelectedItemPosition()).toString());
                viewAndTrackOrderBeansList.get(arrayPosition).getViewAndTrackItemOrderBeanArrayList().remove(position);
                reOrderActivity.notifyDataSetChanged();
                   dialog.dismiss();
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
                DatePickerDialog mDatePicker = new DatePickerDialog(ReOrderActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });
        dialog.show();
    }

    ;*/
    String weekDayName;





    public   void showDialogs(final int position)
    {
        // Create custom dialog object
        final Dialog dialog = new Dialog(this);
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
                    ((App)getApplication()).setTest_location("lab");

                }
                else {
                    ((App)getApplication()).setTest_location("home");
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
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, timeSlots);
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
                    Toast.makeText(ReOrderActivity.this, "Please Select Time", Toast.LENGTH_SHORT).show();
                    return;
                }
                ((App)getApplication()).setTest_location("lab");

                HelperMethods.updateCart(ReOrderActivity.this,viewAndTrackOrderBeansList.get(arrayPosition).getViewAndTrackItemOrderBeanArrayList().get(position).getId(), "add", text_date.getText().toString(), timeSlots.get(spinner.getSelectedItemPosition()).toString());
                viewAndTrackOrderBeansList.get(arrayPosition).getViewAndTrackItemOrderBeanArrayList().remove(position);
                reOrderActivity.notifyDataSetChanged();
                dialog.dismiss();
                /*if (spinner.getSelectedItemPosition() <= 0) {
                    Toast.makeText(ReOrderActivity.this, "Please Select Time", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (radioGroup.getCheckedRadioButtonId()== -1) {
                    Toast.makeText(ReOrderActivity.this, "Please Select Test Locations", Toast.LENGTH_SHORT).show();
                    return;
                }
                ((App)getApplication()).setSelected_time_slots(timeSlots.get(spinner.getSelectedItemPosition()).toString());
                ((App)getApplication()).setSelected_date(text_date.getText().toString());

                dialog.dismiss();*/
               /* Fragment fragment =new SearchTestFragments();
                HelperMethods.loadFragment(ReOrderActivity.this,fragment);*/
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
                DatePickerDialog mDatePicker = new DatePickerDialog(ReOrderActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });

        dialog.show();
    }
}