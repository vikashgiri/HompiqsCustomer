package com.infoicon.powercoin.customView;

import android.content.Context;
import android.util.AttributeSet;

import com.infoicon.powercoin.utils.HelperMethods;


/**
 * Created by infoicona on 9/3/17.
 */

public class CustomCheckedTextView extends android.support.v7.widget.AppCompatCheckedTextView {
    public CustomCheckedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(HelperMethods.getTypeFaceMontserrat(context));
    }

    public CustomCheckedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(HelperMethods.getTypeFaceMontserrat(context));
    }

    public CustomCheckedTextView(Context context) {
        super(context);
        setTypeface(HelperMethods.getTypeFaceMontserrat(context));
    }
}
