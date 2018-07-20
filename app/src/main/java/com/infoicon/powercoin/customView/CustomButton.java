package com.infoicon.powercoin.customView;

import android.content.Context;
import android.util.AttributeSet;

import com.infoicon.powercoin.utils.HelperMethods;


/**
 * Created by infoicon on 9/5/16.
 */
public class CustomButton extends android.support.v7.widget.AppCompatButton {


    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(HelperMethods.getTypeFaceMontserrat(context));
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(HelperMethods.getTypeFaceMontserrat(context));
    }

    public CustomButton(Context context) {
        super(context);
        setTypeface(HelperMethods.getTypeFaceMontserrat(context));
    }




}