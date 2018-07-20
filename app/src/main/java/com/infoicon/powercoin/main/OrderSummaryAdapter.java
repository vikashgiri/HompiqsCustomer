package com.infoicon.powercoin.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import infoicon.com.powercoin.R;

/**
 * Created by asdfgh on 10/13/2017.
 */

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.MyViewHolder> {
    Context context;
    String image_url;
    ArrayList<ViewAndTrackOrderDetail>viewAndTrackItemOrderBeanList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,lab_name;
       public TextView attribute;
        public TextView price;
        public TextView textView;
        ImageView image;

                public MyViewHolder(View view) {
            super(view);
            name=(TextView)  view.findViewById(R.id.name);
                    lab_name=(TextView)  view.findViewById(R.id.lab_name);
            price=(TextView)  view.findViewById(R.id.price);
                    image=(ImageView)  view.findViewById(R.id.product_image);



                }

    }
    public OrderSummaryAdapter(Context context, ArrayList<ViewAndTrackOrderDetail> viewAndTrackItemOrderBeanList, String image_url) {
        this.viewAndTrackItemOrderBeanList = viewAndTrackItemOrderBeanList;
        this.context=context;
        this.image_url=image_url;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orderlistitemrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.name.setText(viewAndTrackItemOrderBeanList.get(position).getTest_name());
        holder.lab_name.setText(viewAndTrackItemOrderBeanList.get(position).getLab_name());

        // holder.attribute.setText("1");
        holder.price.setText(" ₹ "+viewAndTrackItemOrderBeanList.get(position).getAmount());
       // holder.qty.setText(viewAndTrackItemOrderBeanList.get(position).getNumber_of_item()+"Items");
       /* Glide.with(context).load(image_url+"/"+viewAndTrackItemOrderBeanList.get(position).getImage_url())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);*/
    }

    @Override
    public int getItemCount() {
        return viewAndTrackItemOrderBeanList.size();
    }

}
