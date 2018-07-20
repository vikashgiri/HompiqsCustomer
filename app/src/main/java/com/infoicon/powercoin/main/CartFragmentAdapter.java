package com.infoicon.powercoin.main;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infoicon.powercoin.bottomNavigation.CardListResponce;
import com.infoicon.powercoin.utils.HelperMethods;

import java.util.ArrayList;

import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.ItemCartFragmentAdapterBinding;


/**
 * Created by infoicona on 02/8/17.
 */

public class CartFragmentAdapter extends RecyclerView.Adapter<CartFragmentAdapter.MyViewHolder> {

    private Activity context;
   // private final List<GuestSearchListResponse.GuestsListBean> guestList;
    private ArrayList<Integer> selectedCountryName;
    private final OnItemClickListener clickListener;
    ArrayList<CardListResponce> cardListResponceArrayList;
    ItemCartFragmentAdapterBinding itemCartFragmentAdapterBinding;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public CartFragmentAdapter(Activity context,ArrayList<CardListResponce> cardListResponceArrayList,
                               OnItemClickListener itemClickListener)
    {
        this.context = context;
        clickListener = itemClickListener;
        selectedCountryName = new ArrayList<>();
        this.cardListResponceArrayList=cardListResponceArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_fragment_adapter, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        itemCartFragmentAdapterBinding = DataBindingUtil.bind(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        holder.bind(clickListener, position);
    }
/*
    public List<GuestSearchListResponse.GuestsListBean> getItems() {
        return guestList;
    }*/

    @Override
    public int getItemCount()
    {
        return cardListResponceArrayList.size()>0?cardListResponceArrayList.size():0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
      /*  @BindView(R.id.tvGuestFirstName) CustomTextView tvGuestFirstName;
        @BindView(R.id.tvGuestLastName) CustomTextView tvGuestLastName;
        @BindView(R.id.tvEmail) CustomTextView tvEmail;
        @BindView(R.id.tvMobileNo) CustomTextView tvMobileNo;
        @BindView(R.id.tvIncomingDate) CustomTextView tvIncomingDate;
        @BindView(R.id.tvOutGoingDate) CustomTextView tvOutGoingDate;
        @BindView(R.id.tvBookingDate) CustomTextView tvBookingDate;
        @BindView(R.id.root)    RelativeLayout root;
*/
        public MyViewHolder(View itemView)
        {
            super(itemView);
         //   ButterKnife.bind(this, itemView);
        }

        public void bind(final OnItemClickListener clickListener, final int position)
        {

            try
            {

                itemCartFragmentAdapterBinding.txtTestName.setText(cardListResponceArrayList.get(position).getCardListDetailResponceArrayList().getTest_name());

                itemCartFragmentAdapterBinding.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HelperMethods.updateCart(context,cardListResponceArrayList.get(position).getMerchant_inventry_id(),"delete","","");
                    }
                });

                itemCartFragmentAdapterBinding.labName.setText(cardListResponceArrayList.get(position).getCardListDetailResponceArrayList().getLab_name());

                itemCartFragmentAdapterBinding.labAds.setText(cardListResponceArrayList.get(position).getCardListDetailResponceArrayList().getLab_address());

                itemCartFragmentAdapterBinding.dateTime.setText(cardListResponceArrayList.get(position).getTest_date()+"  "+cardListResponceArrayList.get(position).getTime_slot_start_time()+" to "+cardListResponceArrayList.get(position).getTime_slot_end_time());

            } catch (IllegalArgumentException |NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
