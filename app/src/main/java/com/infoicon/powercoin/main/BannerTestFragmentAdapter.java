package com.infoicon.powercoin.main;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infoicon.powercoin.bottomNavigation.App;
import com.infoicon.powercoin.bottomNavigation.CardListResponce;
import com.infoicon.powercoin.bottomNavigation.TestList;

import java.util.ArrayList;

import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.ItemBannerTestFragmentAdapterBinding;


/**
 * Created by infoicona on 02/8/17.
 */

public class BannerTestFragmentAdapter extends RecyclerView.Adapter<BannerTestFragmentAdapter.MyViewHolder> {

    private Activity context;
ItemBannerTestFragmentAdapterBinding mBinding;
   // private final List<GuestSearchListResponse.GuestsListBean> guestList;
    private ArrayList<Integer> selectedCountryName;
    private final OnItemClickListener clickListener;
    ArrayList<TestList> beansArrayList;
    ArrayList<CardListResponce> cartList;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public BannerTestFragmentAdapter(Activity context, ArrayList<TestList> beansArrayList, OnItemClickListener itemClickListener) {
        this.context = context;
        clickListener = itemClickListener;
        selectedCountryName = new ArrayList<>();
        this.beansArrayList=beansArrayList;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_test_fragment_adapter, parent, false);
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
        return beansArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
         //   ButterKnife.bind(this, itemView);
        }


        public void bind(final OnItemClickListener clickListener, final int position) {
            try {
                cartList= ((App)context.getApplicationContext()).getCardList();
                mBinding.testPrice.setPaintFlags(mBinding.testPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mBinding.testName.setText(beansArrayList.get(position).getTest_name());
                mBinding.testDiscountPrice.setText("₹ "+String.valueOf((Double.parseDouble(beansArrayList.get(position).getPrice())-Double.parseDouble(beansArrayList.get(position).getDiscount_value()))));
                mBinding.testPrice.setText("₹ "+beansArrayList.get(position).getPrice());
                //mBinding.testInfo.setVisibility(View.GONE);
                if(!beansArrayList.get(position).getCustom_info().isEmpty()) {
                    mBinding.testInfo.setText(beansArrayList.get(position).getCustom_info());
                    mBinding.testInfo.setVisibility(View.VISIBLE);
                }
if(position==0)
{
    mBinding.btnAddtoCard.setVisibility(View.GONE);
}



                for(int i=0;i<cartList.size();i++)
{
    if(beansArrayList.get(position).getId().equalsIgnoreCase(cartList.get(i).getCardListDetailResponceArrayList().getTest_id()))
    {
        mBinding.btnAddtoCard.setAlpha(.5f);
        mBinding.btnAddtoCard.setClickable(false);
        break;
   }
}
/*
    mBinding.btnAddtoCard.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
          clickListener.onItemClick(position);
        }
    });*/
            } catch (IllegalArgumentException |NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
