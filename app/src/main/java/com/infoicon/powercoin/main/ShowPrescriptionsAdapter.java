package com.infoicon.powercoin.main;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.ShowprescriptionsBinding;


/**
 * Created by infoicona on 02/8/17.
 */

public class ShowPrescriptionsAdapter extends RecyclerView.Adapter<ShowPrescriptionsAdapter.MyViewHolder>
{

    private Activity context;
String base_url;
    private final OnItemClickListener clickListener;
    ShowprescriptionsBinding showprescriptionsBinding;
ArrayList<ShowPrescriptionResponce> showPrescriptionResponceArrayList;
public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ShowPrescriptionsAdapter(Activity context, ArrayList<ShowPrescriptionResponce>showPrescriptionResponceArrayList,String base_url,OnItemClickListener itemClickListener) {
        this.context = context;
        clickListener = itemClickListener;
        this.base_url=base_url;
 this.showPrescriptionResponceArrayList=showPrescriptionResponceArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.showprescriptions, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        showprescriptionsBinding = DataBindingUtil.bind(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(clickListener, position);
    }


    @Override
    public int getItemCount() {
        return showPrescriptionResponceArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
         //   ButterKnife.bind(this, itemView);
        }


        public void bind(final OnItemClickListener clickListener, final int position) {
            try {
              showprescriptionsBinding.txtMsg.setText(showPrescriptionResponceArrayList.get(position).getRefered_by());
                Glide.with(context).load(base_url+"/"+showPrescriptionResponceArrayList.get(position).getImage_name())
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(showprescriptionsBinding.image);
            } catch (IllegalArgumentException |NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
