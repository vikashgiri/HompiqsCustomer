package com.infoicon.powercoin.bottomNavigation.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infoicon.powercoin.main.TestFragmentAdapter;

import java.util.ArrayList;

import infoicon.com.powercoin.R;


/**
 * Created by infoicona on 02/8/17.
 */

public class HealthTipsFragmentAdapter extends RecyclerView.Adapter<HealthTipsFragmentAdapter.MyViewHolder> {

    private Activity context;
    // private final List<GuestSearchListResponse.GuestsListBean> guestList;
    private ArrayList<Integer> selectedCountryName;
    private final OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public HealthTipsFragmentAdapter(Activity context, OnItemClickListener itemClickListener) {
        this.context = context;
        clickListener = itemClickListener;
        selectedCountryName = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_health_tips_fragment_adapter, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(holder, clickListener, position);
    }
/*
    public List<GuestSearchListResponse.GuestsListBean> getItems() {
        return guestList;
    }*/

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.txt_msg);
        }


        public void bind(MyViewHolder holder, final OnItemClickListener clickListener, final int position) {
            try {
                if (position == 0) {
                    holder.date.setText(" Don’t have soft drinks or energy drinks while you're exercising");
                }
                if (position == 1) {
                    holder.date.setText(" Don’t have soft drinks or energy drinks while you're exercisingWhen you stretch, ease your body into position until you feel the stretch and hold it for about 25 seconds. ");


                }
                if (position == 2) {
                    holder.date.setText("Prevent low blood sugar as it stresses you out..");


                }
                if (position == 3) {
                    holder.date.setText("We need at least 90 mg of vitamin C per day.");

                }
               /* tvGuestFirstName.setText(data.getFirst_name().trim()+" "+data.getFirst_name().trim());
                *//*tvGuestFirstName.setText(context.getResources().getString(R.string.label_first_name)+data.getFirst_name().trim());
                tvGuestLastName.setText(context.getResources().getString(R.string.label_last_name)+data.getLast_name().trim());*//*
                tvEmail.setText(context.getResources().getString(R.string.label_email)+" "+data.getEmail().trim());
                tvMobileNo.setText(context.getResources().getString(R.string.label_phone)+" "+data.getTelephone().trim());
                tvIncomingDate.setText(context.getResources().getString(R.string.hint_check_in)+" : "+data.getCheck_in());
                tvOutGoingDate.setText(context.getResources().getString(R.string.hint_check_out)+" : "+data.getCheck_out());
                tvBookingDate.setText(context.getResources().getString(R.string.label_booked_on)+" "+data.getBooked_at());
                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //clickListener.onItemClick(data, position);
                    }
                });*/
            } catch (IllegalArgumentException | NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}