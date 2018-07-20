package com.infoicon.powercoin.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.ThanksactivityBinding;

/**
 * Created by HP-PC on 3/19/2018.
 */

public class ThanksActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       ThanksactivityBinding thanksactivityBinding= DataBindingUtil.setContentView(this,R.layout.thanksactivity);
thanksactivityBinding.mainToolbar.txtTitle.setText("Thanks");
thanksactivityBinding.mainToolbar.toolbarBack.setVisibility(View.GONE);
thanksactivityBinding.textOrderId.setText("Order_id  : "+getIntent().getStringExtra("order_id"));
thanksactivityBinding.home.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i = new Intent(ThanksActivity.this, MainActivity.class);
// set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    finish();}
});
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ThanksActivity.this, MainActivity.class);
// set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}
