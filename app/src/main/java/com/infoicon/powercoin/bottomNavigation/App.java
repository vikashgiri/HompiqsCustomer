package com.infoicon.powercoin.bottomNavigation;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.infoicon.powercoin.main.LruBitmapCache;

import java.util.ArrayList;

/**
 * Created by HP-PC on 3/15/2018.
 */

public class App extends Application {
    ArrayList<LabBeans> uniqueLabListArrayList;
    ArrayList<LabBeans> LabListArrayList;
    ArrayList<CardListResponce> cardListResponceArrayList;
    private static App instance;
    //private static MyApp instance;
    private static Context mContext;
    public static final String TAG = App.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    LruBitmapCache mLruBitmapCache;
String test_location;
    private static App mInstance;
    public static App getInstance() {
        return instance;
    }
String lab_id;
String selected_time_slots;
    String selected_date;

    public String getSelected_date() {
        return selected_date;
    }

    public void setSelected_date(String selected_date) {
        this.selected_date = selected_date;
    }

    public String getSelected_time_slots() {
        return selected_time_slots;
    }

    public void setSelected_time_slots(String selected_time_slots) {
        this.selected_time_slots = selected_time_slots;
    }

    public String getLab_id() {
        return lab_id;
    }

    public void setLab_id(String lab_id) {
        this.lab_id = lab_id;
    }

    public static Context getContext() {
        //  return instance.getApplicationContext();
        return mContext;
    }

    public String getTest_location()
    {
        return test_location;
    }

     public void setTest_location(String test_location)
    {
        this.test_location = test_location;
    }

    @Override
    public void onCreate() {
        super.onCreate();
         instance = this;
        mContext = getApplicationContext();
      /*  Bugfender.init(this, "mQ2ZnsLfDI5S2zp4xvAp5c19XeVHpzb5", BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(this);*/

    }

    public ArrayList<LabBeans> getUniqueLab() {
        return uniqueLabListArrayList;
    }

    public void setCardList(ArrayList<CardListResponce> cardListResponceArrayList) {
        this.cardListResponceArrayList = cardListResponceArrayList;
    }

    public  ArrayList<CardListResponce> getCardList()
    {
    return cardListResponceArrayList;
    }
    public void setUniqueLab(ArrayList<LabBeans> uniqueLabListArrayList) {
        this.uniqueLabListArrayList = uniqueLabListArrayList;
    }

    public ArrayList<LabBeans> getLab() {
        return LabListArrayList;
    }

    public void setLab(ArrayList<LabBeans> LabListArrayList) {
        this.LabListArrayList = LabListArrayList;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//       MultiDex.install(this);
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            getLruBitmapCache();
            mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
        }

        return this.mImageLoader;
    }

    public LruBitmapCache getLruBitmapCache() {
        if (mLruBitmapCache == null)
            mLruBitmapCache = new LruBitmapCache();
        return this.mLruBitmapCache;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}