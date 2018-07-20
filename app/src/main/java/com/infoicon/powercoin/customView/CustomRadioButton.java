package com.infoicon.powercoin.customView;

import android.content.Context;
import android.util.AttributeSet;

import com.infoicon.powercoin.utils.HelperMethods;

/**
 * Created by infoicona on 14/3/17.
 */

public class CustomRadioButton extends android.support.v7.widget.AppCompatRadioButton{

    public CustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(HelperMethods.getTypeFaceMontserrat(context));
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(HelperMethods.getTypeFaceMontserrat(context));
    }

    public CustomRadioButton(Context context) {
        super(context);
        setTypeface(HelperMethods.getTypeFaceMontserrat(context));
    }
}
