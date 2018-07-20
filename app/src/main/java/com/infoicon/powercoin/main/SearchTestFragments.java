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
import android.widget.ProgressBar;
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
import com.infoicon.powercoin.bottomNavigation.TestList;
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
import infoicon.com.powercoin.databinding.FragmentSearchTestBinding;

/**
 * Created by HP-PC on 3/2/2018.
 */

public class SearchTestFragments extends Fragment {
    ProgressBar mprogressBar;
int pageCount=1;
    ArrayList<TestList> beansArrayList = new ArrayList<>();
    ;
    boolean isLoading=true;
    SearchView search;

    HashMap<String, String> HashMapForURL;
    FragmentSearchTestBinding mBinding;
    private JSONObject sendJson;

    public SearchTestFragments() {
        // Required empty public constructor
    }

    private void initView() {
        mprogressBar = (ProgressBar) mBinding.getRoot().findViewById(R.id.progressBar);
        search = (SearchView) mBinding.getRoot().findViewById(R.id.searchView);
        final int mNoOfColumns = HelperMethods.calculateNoOfColumns(getActivity());
       final LinearLayoutManager lLayout = new LinearLayoutManager(getActivity());
        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setLayoutManager(lLayout);
        mprogressBar.setVisibility(View.VISIBLE);
        mBinding.recyclerView.setVisibility(View.GONE);
        getTestApi(" ");
        mBinding.recyclerView.setNestedScrollingEnabled(false);
        mBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = lLayout.findLastVisibleItemPosition();

                if (lastvisibleitemposition == serSearchTestFragmentAdapter.getItemCount() - 1 && !isLoading) {

isLoading=true;
                   // bottomProgress.setVisibility(View.VISIBLE);
pageCount++;
mBinding.bottomProgressBar.setVisibility(View.VISIBLE);
getTestApi(searchTest);
                }
            }
        });
    }
String searchTest=" ";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_search_test, container, false);
        initView();


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
pageCount=1;
                searchTest=newText;
                if (newText.length() > 2) {
                    mprogressBar.setVisibility(View.VISIBLE);
                    mBinding.recyclerView.setVisibility(View.GONE);

                    getTestApi(searchTest);
                } else {
                    if (newText.length() <= 0) {
                        mprogressBar.setVisibility(View.VISIBLE);
                        mBinding.recyclerView.setVisibility(View.GONE);
                        getTestApi(searchTest);
                    }
                }
                return false;
            }
        });
        return mBinding.getRoot();
    }



    private DatePicker datePicker;

    SearchTestFragmentAdapter serSearchTestFragmentAdapter;
    private void getTestApi(String newString) {
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Keys.BASE_URL_MANAGE_CONTROLLER+"getTestList?page="+pageCount+"&test_by_lab=1&lab_id="+((App)getActivity().getApplication()).getLab_id() + "&test_name=" + newString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            isLoading=false;
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if(pageCount==1)
                            {
                                beansArrayList.clear();
                            }

                          //  beansArrayList.clear();
                            for (int j = 0; j < jsonArray.length(); j++)
                            {
                                JSONObject testObject = jsonArray.getJSONObject(j);
                                TestList testList = new TestList();
                                testList.setId(testObject.getString("id"));
                                testList.setTest_name(testObject.getString("test_name"));
                                testList.setTest_desc(testObject.getString("test_desc"));
                                testList.setTest_available_at(testObject.getString("test_available_at"));
                                testList.setStatus(testObject.getString("status"));
                                testList.setCreated_date(testObject.getString("created_date"));
                                testList.setUpdated_date(testObject.getString("updated_date"));
                                testList.setPrice(testObject.getString("price"));
                                testList.setDiscount_type(testObject.getString("discount_type"));
                                testList.setDiscount_value(testObject.getString("discount_value"));
                                testList.setTime_consumed_by_test(testObject.getString("time_consumed_by_test"));
                                testList.setInventry_id(testObject.getString("inventry_id"));
                                testList.setCustom_info(testObject.getString("custom_info"));


                                    beansArrayList.add(testList);

                            }
                            if(pageCount>1)
                            {
                                serSearchTestFragmentAdapter.notifyDataSetChanged();
                            }else {
                                setAdapter();
                            }

                            mBinding.recyclerView.setVisibility(View.VISIBLE);
                            mprogressBar.setVisibility(View.GONE);
                            mBinding.bottomProgressBar.setVisibility(View.GONE);

                        } catch (Exception e) {
                            e.printStackTrace();
                            mprogressBar.setVisibility(View.GONE);
                            mBinding.bottomProgressBar.setVisibility(View.GONE);

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        mprogressBar.setVisibility(View.GONE);
                        mBinding.bottomProgressBar.setVisibility(View.GONE);

                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
    ;


void setAdapter()
{
    serSearchTestFragmentAdapter = new
            SearchTestFragmentAdapter(getActivity(), beansArrayList, new
            SearchTestFragmentAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(
                        int position) {
                                         /*HelperMethods.updateCart(getActivity(), beansArrayList.get(position).getInventry_id(), "add", ((App) getActivity().getApplication()).getSelected_date(),
                                                    ((App) getActivity().getApplication()).getSelected_time_slots());

                             */
                    if(((App)getActivity().getApplication()).getCardList().size()>0)
                    {
                        if(!((App)getActivity().getApplication()).getCardList().get(0).getCardListDetailResponceArrayList().getLab_id().equalsIgnoreCase(((App)getActivity().getApplication()).getLab_id())) {
                            ClearCartDialogs(position);
                        }
                        else {
                            HelperMethods.updateCart(getActivity(), beansArrayList.get(position).getInventry_id(), "add", ((App) getActivity().getApplication()).getSelected_date(),
                                    ((App) getActivity().getApplication()).getSelected_time_slots());
                        }
                        return;
                    }
                    getTestTimeSlotsApi(beansArrayList.get(position).getInventry_id(),position);

                }
                //  getTestTimeSlotsApi(position);// showDialog(position);
//addToCart(position);
            });


    mBinding.recyclerView.setAdapter(serSearchTestFragmentAdapter);
}
    public   void showDialogs(final int position)
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
                HelperMethods.updateCart(getActivity(), beansArrayList.get(position).getInventry_id(), "add", ((App) getActivity().getApplication()).getSelected_date(),
                        ((App) getActivity().getApplication()).getSelected_time_slots());
                /*Fragment fragment =new SearchTestFragments();
                HelperMethods.loadFragment(getActivity(),fragment);*/
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



    String weekDayName;
    List timeSlots;
    ArrayAdapter dataAdapter;

    ArrayList<String>closeDateArrayList=new ArrayList<>();
    ArrayList<WeekDateBean> weekDateBeanArrayList;

    private int year, month, day;

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

        for (int i = 0; i < 200; i++)
        {
            int start_hours = calendars.get(Calendar.HOUR_OF_DAY);
            int start_minuts = calendars.get(Calendar.MINUTE);
            String starttime = start_hours + ":" + start_minuts;
            calendars.add(Calendar.HOUR_OF_DAY, 2);
            int end_hour = calendars.get(Calendar.HOUR_OF_DAY);
            int end_minut = calendars.get(Calendar.MINUTE);


            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            String formattedDate = df.format(c);

            if (date.equals(formattedDate)) {
                Calendar rightNow = Calendar.getInstance();
                int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
                if(currentHour>=start_hours)
                {
                    continue;
                }
            }

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
    ProgressDialog pDialog;

    private void ClearCartDialogs(final int position)
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
                        ClearCartList(position);
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

    private void ClearCartList(final int position)
    {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
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



    private void getTestTimeSlotsApi(String inventry_id, final int position) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
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
                            pDialog.dismiss();
                            showDialogs(position);
                        } catch (Exception e) {
                            pDialog.dismiss();

                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        pDialog.dismiss();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
public  void refress()
{
setAdapter();}
}