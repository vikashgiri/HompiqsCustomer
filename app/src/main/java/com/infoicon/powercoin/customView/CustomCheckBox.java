package com.infoicon.powercoin.customView;

import android.content.Context;
import android.util.AttributeSet;

import com.infoicon.powercoin.utils.HelperMethods;

/**
 * Created by infoicona on 14/3/17.
 */

public class CustomCheckBox extends android.support.v7.widget.AppCompatCheckBox{

    public CustomCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(HelperMethods.getTypeFaceMontserrat(context));
    }

    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(HelperMethods.getTypeFaceMontserrat(context));
    }

    public CustomCheckBox(Context context) {
        super(context);
        setTypeface(HelperMethods.getTypeFaceMontserrat(context));
    }
}
