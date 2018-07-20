package com.infoicon.powercoin.login;

import android.app.Activity;
import android.widget.Toast;

import com.razorpay.PaymentResultListener;

/**
 * Created by HP-PC on 5/20/2018.
 */

public class MerchantActivity extends Activity implements PaymentResultListener {
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
}
