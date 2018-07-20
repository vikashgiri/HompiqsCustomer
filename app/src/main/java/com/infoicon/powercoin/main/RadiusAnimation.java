package com.infoicon.powercoin.main;

import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.google.android.gms.maps.model.GroundOverlay;

/**
 * Created by HP-PC on 6/3/2018.
 */

public class RadiusAnimation  extends Animation {
    private GroundOverlay groundOverlay;

    public RadiusAnimation(GroundOverlay groundOverlay) {
        this.groundOverlay = groundOverlay;

    }
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        groundOverlay.setDimensions( (100 * interpolatedTime) );
        groundOverlay.setTransparency( interpolatedTime );

    }


    @Override
    public void initialize(int width, int height, int parentWidth,int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }
}

