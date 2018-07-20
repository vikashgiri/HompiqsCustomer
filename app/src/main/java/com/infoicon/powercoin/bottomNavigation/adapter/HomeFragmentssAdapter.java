package com.infoicon.powercoin.bottomNavigation.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.infoicon.powercoin.bottomNavigation.LabBeans;

import java.util.ArrayList;

import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.HomeFragmentAdapterBinding;

/**
 * Created by infoicona on 02/8/17.
 */

public class HomeFragmentssAdapter extends RecyclerView.Adapter<HomeFragmentssAdapter.MyViewHolder> {

    private Activity context;
    RelativeLayout root;
   // private final List<GuestSearchListResponse.GuestsListBean> guestList;
    private ArrayList<Integer> selectedCountryName;
    private final OnItemClickListener clickListener;
    HomeFragmentAdapterBinding binding;
    ArrayList<LabBeans> uniqueLabListArrayList;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public HomeFragmentssAdapter(Activity context,  ArrayList<LabBeans> uniqueLabListArrayList, OnItemClickListener itemClickListener) {
        this.context = context;
        clickListener = itemClickListener;
        selectedCountryName = new ArrayList<>();
        this.uniqueLabListArrayList=uniqueLabListArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragment_adapter, null);
        binding = DataBindingUtil.bind(view);
       MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(clickListener, position);
    }
/*
    public List<GuestSearchListResponse.GuestsListBean> getItems() {
        return guestList;
    }*/

    @Override
    public int getItemCount() {
        return uniqueLabListArrayList.size()>0 ? uniqueLabListArrayList.size():0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
         //   ButterKnife.bind(this, itemView);
        }

        public void bind(final OnItemClickListener clickListener, final int position) {
            try {
                binding.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickListener.onItemClick(position);
                    }
                });
               binding.labName.setText(uniqueLabListArrayList.get(position).getLab_name());
                binding.ads.setText(uniqueLabListArrayList.get(position).getLab_address());

                // binding.distane.setText(beansArrayList.get(position).getLab_distance());

            } catch (IllegalArgumentException |NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
