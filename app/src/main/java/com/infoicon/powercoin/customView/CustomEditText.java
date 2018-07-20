package com.infoicon.powercoin.customView;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.infoicon.powercoin.utils.HelperMethods;


/**
 * Created by infoicon on 21/3/16.
 */
public class CustomEditText extends AppCompatEditText {


    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(HelperMethods.getTypeFaceMontserrat(context));
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(HelperMethods.getTypeFaceMontserrat(context));
    }

    public CustomEditText(Context context) {
        super(context);
        setTypeface(HelperMethods.getTypeFaceMontserrat(context));
    }


}