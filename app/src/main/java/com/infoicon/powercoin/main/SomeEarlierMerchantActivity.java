package com.infoicon.powercoin.main;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import infoicon.com.powercoin.R;

/**
 * Created by HP-PC on 5/20/2018.
 */

public class SomeEarlierMerchantActivity extends Activity  implements PaymentResultListener {
    // ...

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        /**
         * Add your logic here for a successfull payment response
         */
        Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentError(int code, String response) {

        Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
        /**
         * Add your logic here for a failed payment response
         */
    }
    // ...

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Preload payment resources
         */
        Checkout.preload(getApplicationContext());
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startPayment();
        // ...
    }
    public void startPayment()
    {

        Checkout checkout = new Checkout();
        checkout.setImage(R.drawable.bg_edit_text);
        final Activity activity = this;
        try {
              JSONObject options = new JSONObject();
            options.put("name", "Merchant Name");
            options.put("description", "Order #123456");
            options.put("currency", "INR");
            options.put("amount", "500");
            checkout.open(activity, options);
            }
         catch(Exception e)
         {
            e.printStackTrace();
        }
    }
    // ...

}