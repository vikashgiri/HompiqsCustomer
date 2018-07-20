package com.infoicon.powercoin.bottomNavigation.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.infoicon.powercoin.bottomNavigation.LabBeans;

import java.util.ArrayList;

import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.HomeFragmentAdapterBinding;

/**
 * Created by infoicona on 02/8/17.
 */

public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.MyViewHolder> {

    private Activity context;
    RelativeLayout root;
   // private final List<GuestSearchListResponse.GuestsListBean> guestList;
    private ArrayList<Integer> selectedCountryName;
    private final OnItemClickListener clickListener;
    HomeFragmentAdapterBinding binding;
    ArrayList<LabBeans> beansArrayList;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public HomeFragmentAdapter(Activity context, ArrayList<LabBeans> beansArrayList, OnItemClickListener itemClickListener) {
        this.context = context;
        clickListener = itemClickListener;
        selectedCountryName = new ArrayList<>();
        this.beansArrayList=beansArrayList;
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
        return beansArrayList.size();
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
               // binding.distance.setText(beansArrayList.get(position).getLab_distance());
                binding.labName.setText(beansArrayList.get(position).getLab_name());
                binding.ads.setText(beansArrayList.get(position).getLab_address());
               // binding.re.setText(beansArrayList.get(position).getLab_distance());
               // binding.distane.setText(beansArrayList.get(position).getLab_distance());
                Glide.with(context).load(beansArrayList.get(position).getLabImage() )
                        .thumbnail(0.5f)
                        .crossFade()
                        .placeholder(R.drawable.labimage)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.imageLab);
            } catch (IllegalArgumentException |NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
