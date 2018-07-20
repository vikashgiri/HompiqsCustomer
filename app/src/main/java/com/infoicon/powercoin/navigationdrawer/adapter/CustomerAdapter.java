package com.infoicon.powercoin.navigationdrawer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.infoicon.powercoin.login.DoctorNameHint;

import java.util.ArrayList;

import infoicon.com.powercoin.R;

/**
 * Created by HP-PC on 4/10/2018.
 */
public class CustomerAdapter extends ArrayAdapter<DoctorNameHint> {
    private final String MY_DEBUG_TAG = "CustomerAdapter";
    private ArrayList<DoctorNameHint> items;
    private ArrayList<DoctorNameHint> itemsAll;
    private ArrayList<DoctorNameHint> suggestions;
    private int viewResourceId;

    public CustomerAdapter(Context context, int viewResourceId, ArrayList<DoctorNameHint> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<DoctorNameHint>) items.clone();
        this.suggestions = new ArrayList<DoctorNameHint>();
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        DoctorNameHint customer = items.get(position);
        if (customer != null) {
            TextView customerNameLabel = (TextView) v.findViewById(R.id.customerNameLabel);
            if (customerNameLabel != null)
            {

                customerNameLabel.setText(customer.getDoctor_name());

            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((DoctorNameHint)(resultValue)).getDoctor_name();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (DoctorNameHint customer : itemsAll) {
                    if(customer.getDoctor_name().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(customer);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<DoctorNameHint> filteredList = (ArrayList<DoctorNameHint>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (DoctorNameHint c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

}