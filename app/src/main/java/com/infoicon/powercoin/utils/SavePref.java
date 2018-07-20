package com.infoicon.powercoin.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SavePref {

    public static final String is_loogedin = "is_logins";
    public static final String Email="emails";
    public static final String Password="passwords";
    public static final String Mobile="mobiles";
    public static final String Name="names";
    public static final String User_id="user_ids";
    public static final String City_name="city_names";
    public static final String city_id="city_ids";
    public static final String city_locality="city_locality";
    public static final String address_list="address_list";

    public static final String current_lat="lats";
    public static final String current_lon="lons";
    public static final String address="ads";

/*
    public static final String user_id="user_id";
*/


    public static void saveStringPref(Context context, String key, String value) {
        SharedPreferences sharedPref = context. getSharedPreferences("user_detail", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public static void save_credential(Context context, String key, String value) {
        SharedPreferences sharedPref = context. getSharedPreferences("user_credential", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public  static String get_credential(Context context, String key) {
        SharedPreferences sharedPref = context. getSharedPreferences("user_credential", Context.MODE_PRIVATE);
        return sharedPref.getString(key, "0");
    }

    public static String getPref(Context context, String key) {
        SharedPreferences sharedPref = context. getSharedPreferences("user_detail", Context.MODE_PRIVATE);
        return sharedPref.getString(key, "0");
    }

    public static String getprefCity(Context context, String key) {
        SharedPreferences sharedPref = context. getSharedPreferences("user_detail", Context.MODE_PRIVATE);
        return sharedPref.getString(key, null);
    }


    public  static boolean removePref(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("user_detail", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        return editor.commit();
    }
}