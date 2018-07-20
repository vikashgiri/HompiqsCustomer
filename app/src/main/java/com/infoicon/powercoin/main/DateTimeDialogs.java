package com.infoicon.powercoin.main;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.infoicon.powercoin.utils.HelperMethods;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import infoicon.com.powercoin.R;

/**
 * Created by HP-PC on 3/17/2018.
 */

public class DateTimeDialogs extends Activity implements AdapterView.OnItemSelectedListener {
    private DatePicker datePicker;
    private int year, month, day;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datetime);

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List categories = new ArrayList();
        categories.add("Select Time");
        categories.add("09:45 to 11:45");
        categories.add("12:00 to 14:45");
        categories.add("15:00 to 17:45");
        // Creating adapter for spinner
        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

      Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

    }

    @Override
    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView arg0) {
        // TODO Auto-generated method stub

    }

    @SuppressWarnings("deprecation")
    public void setDate() {
        showDialog(999);
        }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            DatePickerDialog datePickerDialog= new DatePickerDialog(this,
                    myDateListener, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
return datePickerDialog;

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }

            };

    private void showDate(int year, int month, int day) {
        TextView text_date=(TextView)findViewById(R.id.text_date);
        text_date.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year).toString());

text_date.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
setDate();
    }
});
    }

}