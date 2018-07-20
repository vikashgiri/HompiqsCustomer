package com.infoicon.powercoin.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.infoicon.powercoin.bottomNavigation.App;
import com.infoicon.powercoin.bottomNavigation.CardListResponce;
import com.infoicon.powercoin.login.LoginActivity;
import com.infoicon.powercoin.utils.SavePref;
import com.infoicon.powercoin.utils.UpdateCardListInterface;
import com.razorpay.Checkout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.ActivityCartBinding;

public class CartActivity extends AppCompatActivity implements UpdateCardListInterface
{

    ProgressDialog progressDialog;
    HashMap<String, String> HashMapForURL ;
    ActivityCartBinding mBinding;
    JSONObject sendJson;
    ArrayList<CardListResponce> cardListResponceArrayList=new ArrayList<>();

    public CartActivity() {
        // Required empty public constructor
    }

    private void initView()
    {
        LinearLayoutManager lLayout = new LinearLayoutManager(this);
        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setLayoutManager(lLayout);
        mBinding.recyclerView.setNestedScrollingEnabled(false);
    }

    CartFragmentAdapter cartFragmentAdapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_cart);
        mBinding.toolbar.cart.setVisibility(View.VISIBLE);

        mBinding.btnCheckout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (SavePref.getPref(CartActivity.this,SavePref.is_loogedin).equalsIgnoreCase("true"))
                {
                    Intent intent = new Intent(CartActivity.this, AddressList.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(CartActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        mBinding.toolbar.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        mBinding.toolbar.txtTitle.setText(getString(R.string.cart_list));

           cartFragmentAdapter = new
                CartFragmentAdapter(this,((App)this.getApplication()).getCardList(), new
                CartFragmentAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(
                            int position) {
                        //updateCart();
                    }
                });


           mBinding.recyclerView.setAdapter(cartFragmentAdapter);
        try
        {
            int aa=  ((App) this.getApplication()).getCardList().size();
            mBinding.toolbar.cartCounts.setText(""+aa);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        };
    }

    @Override
    public void updateCard(ArrayList<CardListResponce> cardListResponceArrayList)
    {
        int aa=  cardListResponceArrayList.size();

        mBinding.toolbar.cartCounts.setText(""+aa);

        SearchTestFragments searchTestFragments=new SearchTestFragments();
        SearchTestFragments test = (SearchTestFragments) getSupportFragmentManager().findFragmentByTag( searchTestFragments.getClass().getName());
        if (test != null) {
            //DO STUFF
            test.refress();
        }
      // progressDialog.dismiss();
        if(cardListResponceArrayList.size()<=0)
        {
            finish();
        }

        cartFragmentAdapter = new
                CartFragmentAdapter(this,((App)this.getApplication()).getCardList(), new
                CartFragmentAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(
                            int position) {
                        //updateCart();
                    }
                });
        mBinding.recyclerView.setAdapter(cartFragmentAdapter);    }
}
