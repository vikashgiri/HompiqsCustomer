package com.infoicon.powercoin.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infoicon.powercoin.bottomNavigation.App;
import com.infoicon.powercoin.bottomNavigation.CardListDetailResponce;
import com.infoicon.powercoin.bottomNavigation.CardListResponce;
import com.infoicon.powercoin.main.AddressListBean;
import com.infoicon.powercoin.main.SearchFragment;
import com.infoicon.powercoin.main.SearchTestFragments;
import com.infoicon.powercoin.main.WeekDateBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import infoicon.com.powercoin.R;
import infoicon.com.powercoin.databinding.ActivityCartBinding;
/**

 * Created by info on 22/2/18.
 */

public class HelperMethods {

    public static ArrayList<AddressListBean>addressListBeanArrayList=new ArrayList<>();

    /**
     * Get ActionBar?Toolbar view
     */

    HashMap<String, String> HashMapForURL ;
    ActivityCartBinding mBinding;
    static JSONObject sendJson;
    public static View getActionBarView(Activity mActivity)
    {
        Window window = mActivity.getWindow();
        View v = window.getDecorView();
        int resId = mActivity.getResources().getIdentifier("action_bar_container", "id", "android");
        return v.findViewById(resId);
    }
        public static int calculateNoOfColumns(Context context)
        {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            int noOfColumns = (int) (dpWidth / 180);
            return noOfColumns;
        }

    public  static void showDialogs(final Activity context, final String type)
    {
        // Create custom dialog object
        final Dialog dialog = new Dialog(context);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_test_collection);
        // Set dialog title
        dialog.setTitle("Custom Dialog");
        RadioGroup radioGroup = (RadioGroup)dialog.findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if(rb.getText().toString().equalsIgnoreCase("Visit Lab"))
                {
                    ((App)context.getApplication()).setTest_location("lab");

                }
                else {
                    ((App)context.getApplication()).setTest_location("home");
                }
                    if (type.equalsIgnoreCase("lab")) {
                   /// Toast.makeText(context, rb.getText(), Toast.LENGTH_SHORT).show();
                    Fragment fragment =new SearchTestFragments();
                    HelperMethods.loadFragment(context,fragment);
                }
                else {
                    SearchFragment fragment = new SearchFragment();
                    HelperMethods.loadFragment(context, fragment);
                }
                dialog.dismiss();

            }
        });
dialog.show();
   }

    public static Typeface getTypeFaceMontserrat(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "Montserrat_Light.otf");
    }

    /**
     * Setting screen StatusBarTranslucent
     *
     * @param mActivity
     * @param mToolbar        name of the toolbar of which we get the height
     * @param makeTranslucent value identifying whether we've to
     *                        set StatusBarTranslucent or not
     */
    public static void setStatusBarTranslucent(@Nullable Toolbar mToolbar, Activity mActivity, boolean makeTranslucent) {
        //View v = findViewById(R.id.bellow_actionbar);
        if (mToolbar != null) {
            int paddingTop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? getStatusBarHeight(mActivity) : 0;
            TypedValue tv = new TypedValue();
            mActivity.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, tv, true);
            paddingTop += TypedValue.complexToDimensionPixelSize(tv.data, mActivity.getResources().getDisplayMetrics());
            //mToolbar.setPadding(0, makeTranslucent ? paddingTop : 0, 0, 0);
        }
        if (makeTranslucent) {
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    /**
     * Get Status bar height
     */
    public static int getStatusBarHeight(Activity mActivity) {

        Rect rectangle = new Rect();
        Window window = mActivity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        int contentViewTop =
                window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentViewTop - statusBarHeight;

        return statusBarHeight;
    }

    public static  void showSnackBar(Activity activity, String message){
        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }
   /**
     * show short length toast
     */
    public static void showToastS(Context context, String message) {
        if (context != null) {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            //toast.setGravity(Gravity.BOTTOM, 0,0);
            toast.show();
        }
    }


    public static void loadFragment(Context context,Fragment fragment) {
        // load fragment

        String backStateName = fragment.getClass().getName();
        FragmentManager fragmentManager=null;
        try {
             fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        } catch (ClassCastException e) {
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frame,fragment,backStateName);
        transaction.addToBackStack(backStateName);
        transaction.commit();
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static final String EMAIL_PATTERN ="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(HelperMethods.EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isEmpty(String etText) {
        if (etText.trim().length() > 0)
            return true;

        return false;
    }



    public static void updateCarts(final Context context)
    {
        try {
            sendJson = new JSONObject();
            sendJson.put("method", "updatecart");
            sendJson.put("user_id", SavePref.getPref(context, SavePref.User_id));
            sendJson.put("guest_user_id",Keys.device_id);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://52.67.139.216/lab/application/customer",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(""+response);
                       /* try {
                            JSONObject Object = new JSONObject(response);
                            if (Object.getString("status").equalsIgnoreCase("success")) {
                                pDialog.dismiss();
                                finish();
                            }
                            else
                            {
                                Constants.showSnackBar(LoginActivity.this,"Cart Not Updated");

                            }
                        }catch (Exception e)
                        {
                            Constants.showSnackBar(LoginActivity.this,"Cart Not Updated");
                            pDialog.dismiss();
                            e.printStackTrace();
                        }*/

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String,String>();
                params.put("parameters",sendJson.toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }
    public  static void getCartItems(final Activity context)
    {
        final ProgressDialog pDialog;

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        try
        {
            sendJson = new JSONObject();
            sendJson.put("method", "getitemintocart");
          if(SavePref.getPref(context,SavePref.is_loogedin).equalsIgnoreCase("true"))
            {
                sendJson.put("user_id", SavePref.getPref(context, SavePref.User_id));
            }
            else
            {
                sendJson.put("guest_user_id", Keys.device_id);

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://52.67.139.216/lab/application/customer?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<CardListResponce> cardListResponceArrayList=new ArrayList<>();
                        try {
                            String jsonString = response;//your json string here
                            JSONObject jObject = new JSONObject(jsonString).getJSONObject("data");
                            Iterator<String> keys = jObject.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject innerJObject = jObject.getJSONObject(key);
                                CardListResponce cardListResponce = new CardListResponce();
                                cardListResponce.setId(innerJObject.getString("id"));
                                cardListResponce.setUser_id(innerJObject.getString("user_id"));
                                cardListResponce.setGuest_user_id(innerJObject.getString("guest_user_id"));
                                cardListResponce.setMerchant_inventry_id(innerJObject.getString("merchant_inventry_id"));
                                cardListResponce.setTest_date(innerJObject.getString("test_date"));
                                cardListResponce.setTime_slot_start_time(innerJObject.getString("time_slot_start_time"));
                                cardListResponce.setTime_slot_end_time(innerJObject.getString("time_slot_end_time"));
                                cardListResponce.setTest_location(innerJObject.getString("test_location"));
                                cardListResponce.setNumber_of_item(innerJObject.getString("number_of_item"));
                                cardListResponce.setCreated_date(innerJObject.getString("created_date"));
                                JSONObject jProductDetailsObjects = new JSONObject(jsonString).getJSONObject("productDetails");
                                JSONObject jProductDetailsObject = jProductDetailsObjects.getJSONObject("data");
                                Iterator<String> ProductDetailskeys = jProductDetailsObject.keys();
                                while (ProductDetailskeys.hasNext()) {
                                    String ProductDetailskey = ProductDetailskeys.next();
                                    JSONObject ProductDetailskeyJObject = jProductDetailsObject.getJSONObject(ProductDetailskey);
                                    if(ProductDetailskeyJObject.getString("id").equalsIgnoreCase(innerJObject.getString("merchant_inventry_id")))
                                    {
                                        CardListDetailResponce cardListDetailResponce=new CardListDetailResponce();
                                        cardListDetailResponce.setId(ProductDetailskeyJObject.getString("id"));
                                        cardListDetailResponce.setTest_id(ProductDetailskeyJObject.getString("test_id"));
                                        cardListDetailResponce.setLab_id(ProductDetailskeyJObject.getString("lab_id"));
                                        cardListDetailResponce.setPrice(ProductDetailskeyJObject.getString("price"));
                                        cardListDetailResponce.setDiscount_type(ProductDetailskeyJObject.getString("discount_type"));
                                        cardListDetailResponce.setDiscount_value(ProductDetailskeyJObject.getString("discount_value"));
                                        cardListDetailResponce.setTest_name(ProductDetailskeyJObject.getString("test_name"));cardListDetailResponce.setLab_name(ProductDetailskeyJObject.getString("lab_name"));
                                        cardListDetailResponce.setLab_address(ProductDetailskeyJObject.getString("lab_address"));

                                        cardListResponce.setCardListDetailResponceArrayList(cardListDetailResponce);
                                    }

                                }
                                cardListResponceArrayList.add(cardListResponce);

                            }
                            ((App) context.getApplication()).setCardList(cardListResponceArrayList);
                            UpdateCardListInterface   mListener = (UpdateCardListInterface) context;

                            mListener.updateCard(cardListResponceArrayList);
                            pDialog.dismiss();
                        }
                        catch (JSONException e)
                        {
                            pDialog.dismiss();
                            ((App) context.getApplication()).setCardList(cardListResponceArrayList);

                            UpdateCardListInterface   mListener = (UpdateCardListInterface) context;

                            mListener.updateCard(cardListResponceArrayList);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
pDialog.dismiss();
                System.out.println("" + error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("parameters", sendJson.toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }



    public static void updateCart(final Activity mActivity, String inventr_id, final String action,
                                  String date, String time)
    {
        final ProgressDialog pDialog;

        pDialog = new ProgressDialog(mActivity);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        try {

            sendJson = new JSONObject();
            sendJson.put("method", "addtocart");
            if(SavePref.getPref(mActivity,SavePref.is_loogedin).equalsIgnoreCase("true"))
            {
                sendJson.put("user_id", SavePref.getPref(mActivity, SavePref.User_id));
            }
            else
            {
                sendJson.put("guest_user_id", Keys.device_id);

            }
            sendJson.put("number_of_item", "1");
            sendJson.put("merchant_inventry_id", inventr_id);
            sendJson.put("action", action);
            sendJson.put("test_date", date);
            String times[]=time.split("to");
            sendJson.put("start_time", times[0]);
            sendJson.put("end_time", times[1]);
            sendJson.put("test_location",((App)mActivity.getApplication()).getTest_location());

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Keys.BASE_URL+"application/customer?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pDialog.dismiss();
                            String jsonString = response;//your json string here
                            if(new JSONObject(jsonString).getString("status").equalsIgnoreCase("success"))
                            {
                                if(action.equalsIgnoreCase("delete"))
                                {
                                    HelperMethods.showSnackBar(mActivity, "Test deleted successfully");
                                }
                                else {
                                    HelperMethods.showSnackBar(mActivity, "Test added successfully");
                                }
                                getCartItems(mActivity);

                            }
                            else
                            {
                                HelperMethods.showSnackBar(mActivity,new JSONObject(jsonString).getString("msg"));
                            }
                        }
                        catch (JSONException e)
                        {
                            pDialog.dismiss();

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                System.out.println("" + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("parameters", sendJson.toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }

}
