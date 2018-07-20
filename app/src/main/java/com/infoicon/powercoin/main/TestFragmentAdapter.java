package com.infoicon.powercoin.main;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infoicon.powercoin.bottomNavigation.LabBeans;

import java.util.ArrayList;

import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.ItemLabFragmentBinding;


/**
 * Created by infoicona on 02/8/17.
 */

public class TestFragmentAdapter extends RecyclerView.Adapter<TestFragmentAdapter.MyViewHolder> {

    private Activity context;
    ItemLabFragmentBinding mBinding;
   // private final List<GuestSearchListResponse.GuestsListBean> guestList;
    private ArrayList<Integer> selectedCountryName;
    private final OnItemClickListener clickListener;
    ArrayList<LabBeans> labBeansArrayList;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public TestFragmentAdapter(Activity context, ArrayList<LabBeans> labBeansArrayList, OnItemClickListener itemClickListener) {
        this.context = context;
        clickListener = itemClickListener;
        selectedCountryName = new ArrayList<>();
        this.labBeansArrayList=labBeansArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lab_fragment, parent, false);
         mBinding = DataBindingUtil.bind(view);

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
        try {
            return labBeansArrayList.get(0).getTestListArrayList()!=null?labBeansArrayList.size():0;

        }catch (Exception e)
        {
            return 0;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
         //   ButterKnife.bind(this, itemView);
        }


        public void bind(final OnItemClickListener clickListener, final int position) {
            try {
                mBinding.testPrice.setPaintFlags(mBinding.testPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
mBinding.testName.setText(labBeansArrayList.get(position).getTestListArrayList().get(0).getTest_name());
mBinding.txtLabName.setText(labBeansArrayList.get(position).getLab_name());
                mBinding.testPrice.setText("₹ "+labBeansArrayList.get(position).getPrice());
                Double price=Double.parseDouble(labBeansArrayList.get(position).getPrice())-Double.parseDouble(
                        labBeansArrayList.get(position).getDiscount_value()
                );
                mBinding.testpriceWithOffer.setText("₹ "+price);

                mBinding.addTest.setOnClickListener(new View.OnClickListener()
{
    @Override
    public void onClick(View v) {
        clickListener.onItemClick(position);
    }
});
            } catch (IllegalArgumentException |NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
